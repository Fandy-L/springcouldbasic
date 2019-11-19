package com.to8to.tbt.msc.entity.admin;

import com.to8to.tbt.msc.entity.template.TemplateInfo;
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
public class CountTemplateSendItem {

    @ApiModelProperty(value = "模板信息")
    private TemplateInfo result;

    @ApiModelProperty(value = "短信发送总数")
    private Long total;
}
