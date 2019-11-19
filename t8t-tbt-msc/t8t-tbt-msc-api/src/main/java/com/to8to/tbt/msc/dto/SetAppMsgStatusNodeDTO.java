package com.to8to.tbt.msc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author juntao.guo
 */
@Data
@ApiModel(value = "设置消息已读-根据用户及节点ID")
public class SetAppMsgStatusNodeDTO {

    @ApiModelProperty(value = "作者ID", example = "6881")
    @NotNull(message = "请输入作者ID")
    @Min(value = 1, message = "请输入有效的作者ID")
    private Integer uid;

    @ApiModelProperty(value = "是否已读0-未读1-已读", example = "1")
    @NotNull(message = "请输入已读状态")
    @Range(min = 0, max = 1, message = "已读状态取值不合法0-未读1-已读")
    @JsonProperty(value = "is_read")
    private Integer isRead;

    @ApiModelProperty(value = "节点列表", example = "[154]")
    @NotNull(message = "请输入节点ID")
    @Size(min = 1, message = "节点列表不能为空")
    @JsonProperty(value = "node_ids")
    private List<@NotNull @Min(value = 0) Integer> nodeIds;
}
