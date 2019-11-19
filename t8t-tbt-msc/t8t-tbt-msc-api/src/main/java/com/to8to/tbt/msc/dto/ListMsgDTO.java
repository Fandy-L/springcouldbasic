package com.to8to.tbt.msc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.to8to.tbt.msc.entity.PageInfo;
import com.to8to.tbt.msc.entity.SearchTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author juntao.guo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "查询消息列表")
public class ListMsgDTO {
    public String ns;

    @ApiModelProperty(value = "分页信息")
    public PageInfo pageInfo;

    @ApiModelProperty(value = "查询起止时间")
    public SearchTime searchTime;

    @ApiModelProperty(value = "消息类型1-短信 4-APP 5-PC")
    @Range(min = 1)
    @JsonProperty(value = "send_type")
    public Integer sendType;

    @ApiModelProperty(value = "接收目标")
    @JsonProperty(value = "target_contact")
    public String targetContact;

    @ApiModelProperty(value = "目标用户类型")
    @JsonProperty(value = "to_user_type")
    public Integer toUserType;

    @ApiModelProperty(value = "发送状态")
    @JsonProperty(value = "send_status")
    public Integer sendStatus;

    @ApiModelProperty(value = "消息内容")
    public String content;

    @ApiModelProperty(value = "消息标题")
    public String title;

    @ApiModelProperty(value = "节点ID")
    @JsonProperty(value = "note_id")
    public Integer noteId;

    @ApiModelProperty(value = "目标用户列表")
    @JsonProperty(value = "contactlist")
    public List<String> contactList;

    @JsonProperty(value = "yjindu")
    public Integer process;

    @ApiModelProperty(value = "模板ID")
    public Integer tid;

    @ApiModelProperty(value = "项目ID")
    public Integer yid;

    @ApiModelProperty(value = "是否已读")
    @JsonProperty(value = "is_read")
    public Integer isRead;

    @ApiModelProperty(value = "用户ID")
    @JsonProperty(value = "user_id")
    public String userId;
}
