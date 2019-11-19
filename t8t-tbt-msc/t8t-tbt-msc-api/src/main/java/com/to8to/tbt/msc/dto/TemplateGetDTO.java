package com.to8to.tbt.msc.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author juntao.guo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "获取单个短信及APP模板")
public class TemplateGetDTO {
    @NotNull(message = "请填写模板ID")
    @Min(value = 1L, message = "请填写有效的模板ID")
    private Integer tid;
}
