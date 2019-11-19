package com.to8to.tbt.msc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.to8to.tbt.msc.entity.AppMsgWrapper;
import com.to8to.tbt.msc.entity.PcWrapper;
import com.to8to.tbt.msc.entity.RecordTarget;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author juntao.guo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "旧版综合消息发送请求体")
public class SendMsgDTO {
    private String ns;

    @ApiModelProperty(value = "目标用户列表")
    private List<RecordTarget> targets;

    @ApiModelProperty(value = "模板ID")
    private Integer tid;

    @ApiModelProperty(value = "消息内容")
    @NotBlank
    private String content;

    @ApiModelProperty(value = "触发人用户ID")
    @JsonProperty(value = "from_user_id")
    private String fromUserId;

    @JsonProperty(value = "bizdata")
    @ApiModelProperty(value = "业务数据")
    private PcWrapper.BizData bizData;

    @ApiModelProperty(value = "消息标题")
    private String title;

    @ApiModelProperty(value = "有效时间")
    @JsonProperty(value = "expire_time")
    private Long expireTime;

    @JsonProperty(value = "bizid")
    private Integer bizId;

    @JsonProperty(value = "yjindu")
    private Integer process;

    private Integer deal;

    private Integer ywId;

    private Integer ywType;

    @ApiModelProperty(value = "发送通道")
    private Integer channel;

    private Integer zid;

    @ApiModelProperty(value = "城市ID")
    @JsonProperty(value = "cityid")
    private Integer cityId;

    @JsonProperty(value = "ispass")
    private Integer isPass;

    private String personal;
}
