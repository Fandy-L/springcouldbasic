package com.to8to.tbt.msc.vo;

import com.alibaba.fastjson.JSONObject;
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
@ApiModel(value = "查询短信及APP消息记录结果集")
public class SearchMessageRecordVO {
    @ApiModelProperty(value = "总数")
    private Long total;

    @ApiModelProperty(value = "结果集")
    private List<JSONObject> result;
}
