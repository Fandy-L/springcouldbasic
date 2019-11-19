package com.to8to.tbt.msc.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "销毁凭证")
public class VerificationCertificateDTO {
    @ApiModelProperty(value = "凭证", example = "5afd90ae56210a7fb3a07fee033ad3a7")
    private String certificate;
}
