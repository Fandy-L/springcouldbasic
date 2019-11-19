package com.to8to.tbt.msc.service.impl;

import com.to8to.sc.component.redis.operation.AbstractDefaultOperation;
import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;
import com.to8to.tbt.msc.common.MyExceptionStatus;
import com.to8to.tbt.msc.constant.MsgConstant;
import com.to8to.tbt.msc.dto.VerificationCertificateDTO;
import com.to8to.tbt.msc.dto.VerificationCodeCheckDTO;
import com.to8to.tbt.msc.dto.VerificationCodeGenerateDTO;
import com.to8to.tbt.msc.entity.response.StatusMessageResponse;
import com.to8to.tbt.msc.entity.response.StatusResultResponse;
import com.to8to.tbt.msc.service.VerificationCodeService;
import com.to8to.tbt.msc.vo.VerificationCodeCheckVO;
import com.to8to.tbt.msc.vo.VerificationCodeGenerateVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author juntao.guo
 */
@Slf4j
@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Autowired(required = false)
    private AbstractDefaultOperation<String> abstractDefaultOperation;

    @Override
    public VerificationCodeGenerateVO generateVerificationCode(VerificationCodeGenerateDTO verificationCodeGenerateDTO) {
        String platform = verificationCodeGenerateDTO.getPlatform();
        Integer generateType = verificationCodeGenerateDTO.getGenerateType();
        Integer codeType = verificationCodeGenerateDTO.getCodeType();
        String uniqueObject = verificationCodeGenerateDTO.getUniqueObject();
        Integer expiresTime = verificationCodeGenerateDTO.getExpiresTime();
        if (StringUtils.isEmpty(platform)) {
            platform = MsgConstant.DEFAULT_PLATFORM;
        }
        if (expiresTime == null) {
            expiresTime = MsgConstant.CODE_DEFAULT_EXPIRES_TIME;
        }
        VerificationCodeGenerateVO verificationCodeGenerateVO = generateVerificationCode(platform, generateType, codeType, uniqueObject, expiresTime);
        log.info("[generateVerificationCode] resp:{},code:{}", verificationCodeGenerateDTO, verificationCodeGenerateVO);
        return verificationCodeGenerateVO;
    }

    @Override
    public ResResult checkVerificationCode(VerificationCodeCheckDTO verificationCodeCheckDTO) {
        String platform = verificationCodeCheckDTO.getPlatform();
        Integer generateType = verificationCodeCheckDTO.getGenerateType();
        Integer codeType = verificationCodeCheckDTO.getCodeType();
        String uniqueObject = verificationCodeCheckDTO.getUniqueObject();
        Integer code = verificationCodeCheckDTO.getCode();
        Integer certificateExpiresTime = verificationCodeCheckDTO.getCertificateExpiresTime();
        if (StringUtils.isEmpty(platform)) {
            platform = MsgConstant.DEFAULT_PLATFORM;
        }
        if (certificateExpiresTime == null) {
            certificateExpiresTime = MsgConstant.CODE_CERTIFICATE_DEFAULT_EXPIRES_TIME;
        }
        return checkVerificationCode(platform, generateType, codeType, uniqueObject, code, certificateExpiresTime);
    }

    @Override
    public StatusResultResponse<Boolean> checkCertificateForVerificationCode(VerificationCertificateDTO verificationCertificateDTO) {
        String certificate = verificationCertificateDTO.getCertificate();
        return checkCertificateForVerificationCode(certificate);
    }

    @Override
    public StatusResultResponse<Boolean> certificateDestroy(VerificationCertificateDTO verificationCertificateDTO) {
        String certificate = verificationCertificateDTO.getCertificate();
        abstractDefaultOperation.delete(certificate);
        return StatusResultResponse.<Boolean>builder()
                .status(MyExceptionStatus.OLD_MSG_CENTER_RESPONSE_SUCCESS.getCode())
                .result(Boolean.TRUE)
                .build();
    }

    /**
     * 验证码生成
     *
     * @param generateType 验证码生成类型	说明：短信-1、邮件-2、微信-3、APP消息-4、PC消息-5、其他-0
     * @param codeType     验证码功能类型	说明：注册-1、登录-2、绑定-3、解绑-4、修改密码-5、其他 -0
     * @param uniqueObject 唯一标识对象	如："15926214373"、"diaoge@corp.to8to.com"
     * @param platform     平台类型标识	可自定义 ,同 checkVerificationCode 接口 platform 检验使用。如：web-app，默认："msgc" , 建议自定义。
     * @param expiresTime  默认 300 （单位：秒）.
     * @return code 生成验证码内容.
     */
    private VerificationCodeGenerateVO generateVerificationCode(String platform, int generateType, int codeType, String uniqueObject, int expiresTime) {
        String verifyCode = RandomStringUtils.randomNumeric(6);
        String cacheKey = MsgConstant.PREFIX_VERIFY_CODE + platform + "_" + generateType + "_" + codeType + "_" + uniqueObject;
        abstractDefaultOperation.set(cacheKey, verifyCode, expiresTime, TimeUnit.SECONDS);
        return VerificationCodeGenerateVO.builder()
                .status(MyExceptionStatus.OLD_MSG_CENTER_RESPONSE_SUCCESS.getCode())
                .code(verifyCode)
                .build();
    }

    /**
     * 验证码校验并生成凭证
     *
     * @param generateType           验证码生成类型
     * @param codeType               验证码功能类型
     * @param uniqueObject           唯一标识对象
     * @param platform               平台类型标识
     * @param code                   验证码
     * @param certificateExpiresTime 验证码验证通过凭证失效时间
     * @return certificate 验证码验证通过凭证
     */
    private ResResult checkVerificationCode(String platform, int generateType, int codeType, String uniqueObject, Integer code, int certificateExpiresTime) {
        String key = MsgConstant.PREFIX_VERIFY_CODE + platform + "_" + generateType + "_" + codeType + "_" + uniqueObject;
        String value = abstractDefaultOperation.get(key);
        if (!StringUtils.isEmpty(value)) {
            if (value.equals(String.valueOf(code))) {

                String certificate = DigestUtils.md5Hex(uniqueObject + UUID.randomUUID().toString());
                abstractDefaultOperation.delete(key);
                abstractDefaultOperation.set(certificate, "true", certificateExpiresTime, TimeUnit.SECONDS);
                log.info("[checkVerificationCode] resp:{}", certificate);
                VerificationCodeCheckVO verificationCodeCheckVO = VerificationCodeCheckVO.builder()
                        .status(MyExceptionStatus.OLD_MSG_CENTER_RESPONSE_SUCCESS.getCode())
                        .certificate(certificate)
                        .build();
                return ResUtils.data(verificationCodeCheckVO);
            }
        }
        StatusMessageResponse statusMessageResponse = StatusMessageResponse.builder()
                .status(MyExceptionStatus.VERIFICATION_CODE_CHECK_INVALID.getCode())
                .message(MyExceptionStatus.VERIFICATION_CODE_CHECK_INVALID.getMessage())
                .build();
        return new ResResult(statusMessageResponse.getStatus(), statusMessageResponse.getMessage(), statusMessageResponse);
    }

    /**
     * 凭证校验
     *
     * @param certificate 验证码验证通过凭证
     * @return true OR false
     */
    private StatusResultResponse<Boolean> checkCertificateForVerificationCode(String certificate) {
        String value = abstractDefaultOperation.get(certificate);
        log.info("[checkCertificateForVerificationCode]: certificate :{},value:{}", certificate, value);
        return StatusResultResponse.<Boolean>builder()
                .status(MyExceptionStatus.OLD_MSG_CENTER_RESPONSE_SUCCESS.getCode())
                .result(Boolean.valueOf(value))
                .build();
    }
}
