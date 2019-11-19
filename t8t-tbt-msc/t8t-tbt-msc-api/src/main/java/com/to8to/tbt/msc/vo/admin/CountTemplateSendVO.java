package com.to8to.tbt.msc.vo.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.to8to.tbt.msc.entity.admin.CountTemplateSendItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author juntao.guo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "模板消息发送统计响应")
public class CountTemplateSendVO {

    @ApiModelProperty(value = "结果集")
    @JsonProperty(value = "template_count")
    private Map<String, CountTemplateSendItem> templateCount;
}
