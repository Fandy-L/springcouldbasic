package com.to8to.tbt.msc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author yason.li
 */
@Data
public class GetAppMsgRecordCountByTid {
    @ApiModelProperty(value = "用户ID", example = "[6881]")
    @NotEmpty
    private List<@NotNull @Min(value = 0) Integer> uids;

    @ApiModelProperty(value = "是否已读0-未读1-已读", example = "1")
    @Range(min = 0, max = 1, message = "已读状态取值不合法0-未读1-已读")
    @JsonProperty(value = "is_read")
    private Integer isRead;

    @ApiModelProperty(value = "模板ID列表", example = "[154]")
    @JsonProperty(value = "tids")
    private List<@NotNull @Min(value = 0) Integer> tids;
}
