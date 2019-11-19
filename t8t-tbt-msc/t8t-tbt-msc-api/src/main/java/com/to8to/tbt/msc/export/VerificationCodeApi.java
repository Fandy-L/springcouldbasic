package com.to8to.tbt.msc.export;

import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.ServiceNameConst;
import com.to8to.tbt.msc.dto.VerificationCertificateDTO;
import com.to8to.tbt.msc.dto.VerificationCodeCheckDTO;
import com.to8to.tbt.msc.dto.VerificationCodeGenerateDTO;
import com.to8to.tbt.msc.entity.response.StatusResultResponse;
import com.to8to.tbt.msc.vo.VerificationCodeCheckVO;
import com.to8to.tbt.msc.vo.VerificationCodeGenerateVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author juntao.guo
 */
@Validated
@FeignClient(value = ServiceNameConst.SERVICE_NAME)
public interface VerificationCodeApi {

    /**
     * 验证码生成
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "验证码生成", response = VerificationCodeGenerateVO.class)
    @PostMapping(value = "/generateVerificationCode")
    ResResult<VerificationCodeGenerateVO> generateVerificationCode(@RequestBody @NotEmpty @Valid List<@NotNull VerificationCodeGenerateDTO> params);

    /**
     * 验证码校验并生成凭证
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "验证码校验并生成凭证", response = VerificationCodeCheckVO.class)
    @PostMapping(value = "/checkVerificationCode")
    ResResult<VerificationCodeCheckVO> checkVerificationCode(@RequestBody @NotEmpty @Valid List<@NotNull VerificationCodeCheckDTO> params);

    /**
     * 凭证校验接口
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "凭证校验接口", response = ResResult.class)
    @PostMapping(value = "/checkCertificateForVerificationCode")
    ResResult<StatusResultResponse<Boolean>> checkCertificateForVerificationCode(@RequestBody @NotEmpty @Valid List<@NotNull VerificationCertificateDTO> params);

    /**
     * 凭证销毁接口
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "凭证销毁接口", response = ResResult.class)
    @PostMapping(value = "/certificateDestroy")
    ResResult<StatusResultResponse<Boolean>> certificateDestroy(@RequestBody @NotEmpty @Valid List<@NotNull VerificationCertificateDTO> params);
}
