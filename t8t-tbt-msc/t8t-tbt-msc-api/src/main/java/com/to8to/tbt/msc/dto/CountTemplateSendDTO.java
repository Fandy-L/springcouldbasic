package com.to8to.tbt.msc.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

/**
 * @author juntao.guo
 */
@Data
@Valid
@NoArgsConstructor
@ApiModel(value = "统计模板发送情况")
public class CountTemplateSendDTO {
}
