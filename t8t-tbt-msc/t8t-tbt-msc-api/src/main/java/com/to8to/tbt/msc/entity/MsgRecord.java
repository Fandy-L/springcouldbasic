package com.to8to.tbt.msc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class MsgRecord {
    @JsonProperty(value = "_id")
    public String id;

    @JsonProperty(value = "id")
    private String nickId;

    public int tid;

    public String ns;

    public RecordTarget target;

    public String content;

    public int status;

    @JsonProperty(value = "send_time")
    public long sendTime;

    @JsonProperty(value = "from_user_id")
    public String fromUserId;

    @JsonProperty(value = "is_read")
    public int isRead;

    public String bizdata;

    public String title;

    public int bizid;

    public int yjindu;

    public int deal;

    public int channel;

    @JsonProperty(value = "error_code")
    public int errorCode;

    @JsonProperty(value = "error_describle")
    public String errorDescribe;

}
