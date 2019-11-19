package com.to8to.tbt.msc.service;

import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.dto.VerificationCertificateDTO;
import com.to8to.tbt.msc.dto.VerificationCodeCheckDTO;
import com.to8to.tbt.msc.dto.VerificationCodeGenerateDTO;
import com.to8to.tbt.msc.entity.response.StatusResultResponse;
import com.to8to.tbt.msc.vo.VerificationCodeCheckVO;
import com.to8to.tbt.msc.vo.VerificationCodeGenerateVO;

/**
 * @author juntao.guo
 */
public interface VerificationCodeService {

    /**
     * 验证码生成
     *
     * @param verificationCodeGenerateDTO
     * @return
     */
    VerificationCodeGenerateVO generateVerificationCode(VerificationCodeGenerateDTO verificationCodeGenerateDTO);

    /**
     * 验证码校验并生成凭证
     *
     * @param verificationCodeCheckDTO
     * @return
     */
    ResResult checkVerificationCode(VerificationCodeCheckDTO verificationCodeCheckDTO);

    /**
     * 凭证校验接口
     *
     * @param verificationCertificateDTO
     * @return
     */
    StatusResultResponse<Boolean> checkCertificateForVerificationCode(VerificationCertificateDTO verificationCertificateDTO);

    /**
     * 凭证销毁接口
     *
     * @param verificationCertificateDTO
     * @return
     */
    StatusResultResponse<Boolean> certificateDestroy(VerificationCertificateDTO verificationCertificateDTO);
}
