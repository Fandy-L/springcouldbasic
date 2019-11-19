package com.to8to.tbt.msc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.to8to.tbt.msc.entity.RecordTarget;
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
@ApiModel(value = "综合消息记录结果项")
public class MsgRecordVO {
    @JsonProperty(value = "_id")
    private String id;

    @JsonProperty(value = "id")
    private String attachedId;

    private Integer tid;

    private String ns;

    private RecordTarget target;

    private String content;

    private Integer status;

    @JsonProperty(value = "send_time")
    private Long sendTime;

    @JsonProperty(value = "from_user_id")
    private String fromUserId;

    @JsonProperty(value = "is_read")
    private Integer isRead;

    @JsonProperty(value = "bizdata")
    private String bizData;

    private String title;

    @JsonProperty(value = "bizid")
    private Integer bizId;

    @JsonProperty(value = "yjindu")
    private Integer process;

    private Integer deal;

    private Integer channel;

    @JsonProperty(value = "error_code")
    private Integer errorCode;

    @JsonProperty(value = "error_describle")
    private String errorDescribe;
}
