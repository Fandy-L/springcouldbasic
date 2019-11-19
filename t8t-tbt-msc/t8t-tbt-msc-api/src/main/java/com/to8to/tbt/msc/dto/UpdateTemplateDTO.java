package com.to8to.tbt.msc.dto;

import com.to8to.tbt.msc.entity.TemplateWrapper;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * @author juntao.guo
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "短信及APP模板更新")
public class UpdateTemplateDTO {
    @Min(value = 1, message = "请指定有效的模板ID")
    private Integer tid;

    private Integer type;

    private TemplateWrapper.Template common;

    private TemplateWrapper.MsgTemplate msg;
}
