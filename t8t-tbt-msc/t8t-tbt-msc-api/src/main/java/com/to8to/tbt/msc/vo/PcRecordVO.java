package com.to8to.tbt.msc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.to8to.tbt.msc.entity.RecordTarget;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author edmund.yu
 */
@Data
public class PcRecordVO {

    @ApiModelProperty(value = "_id")
    @JsonProperty(value = "_id")
    private String id;

    @ApiModelProperty(value = "id")
    @JsonProperty(value = "id")
    private String nickId;

    @ApiModelProperty(example = "pc")
    private String ns;

    @ApiModelProperty(value = "发送目标")
    private RecordTarget target;

    private Integer tid;

    @ApiModelProperty(value = "发送内容")
    private String content;

    @ApiModelProperty(value = "状态",example = "2")
    private Integer status;

    @ApiModelProperty(value = "发送时间",example = "1420893119")
    @JsonProperty(value = "send_time")
    private Integer sendTime;

    @ApiModelProperty(value = "发送方ID",example = "0")
    @JsonProperty(value = "from_user_id")
    private String fromUserId;

    @ApiModelProperty(value = "是否已读",example = "0")
    @JsonProperty(value = "is_read")
    private Integer isRead;

    @ApiModelProperty(value = "bizData")
    @JsonProperty(value = "bizdata")
    private String bizDataString;

    @ApiModelProperty(value = "标题",example = "业主保障金到账")
    private String title;

    private Integer bizId;

    private Integer yjindu;

    private Integer deal;

    private Integer channel;

    @JsonProperty(value = "error_code")
    private Integer errorCode;

    @ApiModelProperty(example = "")
    @JsonProperty(value = "error_describle")
    private String errorDescribe;

}
