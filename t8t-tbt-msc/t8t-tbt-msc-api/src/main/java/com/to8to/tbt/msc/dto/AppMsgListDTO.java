package com.to8to.tbt.msc.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.Min;

/**
 * @author juntao.guo
 */
@Valid
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "APP消息列表筛选项")
public class AppMsgListDTO{
    @ApiModelProperty(value = "业主ID", example = "172176021")
    private Integer uid;

    @ApiModelProperty(value = "页码", example = "1")
    @Min(value = 1, message = "页码最小值为1")
    private Integer page;

    @ApiModelProperty(value = "每页加载条数", example = "20")
    @Range(min = 1, max = 100, message = "每页加载条数限制在1-100")
    private Integer pageSize;

    @ApiModelProperty(value = "每页加载条数", example = "20")
    @Range(min = 1, max = 100, message = "每页加载条数限制在1-100")
    private Integer perPage;
}
