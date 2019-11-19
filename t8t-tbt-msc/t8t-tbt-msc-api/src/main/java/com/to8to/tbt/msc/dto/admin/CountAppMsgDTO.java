package com.to8to.tbt.msc.dto.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author juntao.guo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "统计APP消息发送量")
public class CountAppMsgDTO {
    @ApiModelProperty(value = "用户ID列表")
    private List<Integer> uids;

    @ApiModelProperty(value = "节点ID列表")
    @JsonProperty(value = "node_ids")
    private List<Integer> nodeIds;

    @ApiModelProperty(value = "是否已读0-未读1-已读")
    @JsonProperty(value = "is_read")
    private Integer isRead;
}
