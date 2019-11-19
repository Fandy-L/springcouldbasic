package com.to8to.tbt.msc.controller;

import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;
import com.to8to.tbt.msc.dto.VerificationCertificateDTO;
import com.to8to.tbt.msc.dto.VerificationCodeCheckDTO;
import com.to8to.tbt.msc.dto.VerificationCodeGenerateDTO;
import com.to8to.tbt.msc.entity.response.StatusResultResponse;
import com.to8to.tbt.msc.export.VerificationCodeApi;
import com.to8to.tbt.msc.service.VerificationCodeService;
import com.to8to.tbt.msc.vo.VerificationCodeCheckVO;
import com.to8to.tbt.msc.vo.VerificationCodeGenerateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author juntao.guo
 */
@RestController
public class VerificationCodeController implements VerificationCodeApi {

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Override
    public ResResult<VerificationCodeGenerateVO> generateVerificationCode(@RequestBody List<VerificationCodeGenerateDTO> params) {
        return ResUtils.data(verificationCodeService.generateVerificationCode(params.get(0)));
    }

    @Override
    public ResResult<VerificationCodeCheckVO> checkVerificationCode(@RequestBody List<VerificationCodeCheckDTO> params) {
        return verificationCodeService.checkVerificationCode(params.get(0));
    }

    @Override
    public ResResult<StatusResultResponse<Boolean>> checkCertificateForVerificationCode(@RequestBody List<VerificationCertificateDTO> params) {
        return ResUtils.data(verificationCodeService.checkCertificateForVerificationCode(params.get(0)));
    }

    @Override
    public ResResult<StatusResultResponse<Boolean>> certificateDestroy(@RequestBody List<VerificationCertificateDTO> params) {
        return ResUtils.data(verificationCodeService.certificateDestroy(params.get(0)));
    }
}
