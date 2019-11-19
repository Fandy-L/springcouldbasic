package com.to8to.tbt.msc.vo;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
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
@ApiModel(value = "装修进度消息列表项")
public class AppProcessMsgVO {
    private Integer id;

    private String title;

    @JsonProperty(value = "send_time")
    private Long sendTime;

    private String content;

    @JsonProperty(value = "is_read")
    private Integer isRead;

    @JsonProperty(value = "bizdata")
    private JSONObject bizData;
}
