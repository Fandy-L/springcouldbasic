package com.to8to.tbt.msc.entity;

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
public class ResponseWrapper {
    @Data
    @Builder
    @ApiModel(value = "聚合结果集")
    public static class ResultVO {
        @ApiModelProperty(value = "总数")
        private Integer total;

        @ApiModelProperty(value = "数据列表")
        private List list;

        @ApiModelProperty(value = "结果集")
        private JSONObject data;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MsgResponse {
        private Integer result;

        private String status;
    }
}
