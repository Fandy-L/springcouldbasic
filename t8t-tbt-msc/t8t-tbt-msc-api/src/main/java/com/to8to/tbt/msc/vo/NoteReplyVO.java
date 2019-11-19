package com.to8to.tbt.msc.vo;

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
@ApiModel(value = "查询短信上行记录结果")
public class NoteReplyVO {

    @JsonProperty(value = "send_time")
    private Integer sendTime;

    @JsonProperty(value = "msg_content")
    private String msgContent;

    @JsonProperty(value = "node_type_des")
    private String nodeTypeDes;

    private Integer type;
}
