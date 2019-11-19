package com.to8to.tbt.msc.entity.admin;

/**
 * @author juntao.guo
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountTemplateSendTemplate<T> {
    private Integer tid;

    private Integer type;

    @ApiModelProperty(value = "模板配置表中的发送节点id")
    @JsonProperty(value = "nodeid")
    private Integer nodeId;

    @ApiModelProperty(value = "模版名称")
    private String title;

    @ApiModelProperty(value = "启用状态 1.启用 2.停用")
    @JsonProperty(value = "is_active")
    private Integer isActive;

    @ApiModelProperty(value = "触发方式 1.手动 2.自动")
    @JsonProperty(value = "is_auto")
    private Integer isAuto;

    @ApiModelProperty(value = "接收角色（现在为发送对象)")
    @JsonProperty(value = "target_type")
    private Integer targetType;

    @ApiModelProperty(value = "发送方式 1.短信，2.pc消息 3.邮件 4.微信")
    @JsonProperty(value = "send_type")
    private int sendType;

    @ApiModelProperty(value = "产品模块")
    @JsonProperty(value = "pm_module")
    private String pmModule;

    @ApiModelProperty(value = "备注")
    @JsonProperty(value = "msg_remark")
    private String msgRemark;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty(value = "create_time")
    private Integer createTime;

    @ApiModelProperty(value = "创建人ID")
    @JsonProperty(value = "create_id")
    private Integer createId;

    @ApiModelProperty(value = "修改人id")
    @JsonProperty(value = "modify_id")
    private Integer modifyId;

    @ApiModelProperty(value = "修改时间")
    @JsonProperty(value = "modify_time")
    private Integer modifyTime;

    @ApiModelProperty(value = "专属模板信息")
    @JsonProperty(value = "msgtemplate")
    private T msgTemplate;
}
