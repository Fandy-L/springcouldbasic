package com.to8to.tbt.msc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

/**
 * @author juntao.guo
 */
public class ConfigureWrapper {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "配置项结果集")
    public static class Configure{
        private int cid;

        @ApiModelProperty(value = "节点类型:1.业务类型 2.业务项 3.发送节点 4.接收对象 5.产品模块")
        @JsonProperty(value = "config_type")
        private int configType;

        @ApiModelProperty(value = "节点描述")
        @JsonProperty(value = "config_describe")
        private String configDescribe;

        @ApiModelProperty(value = "父节点ID")
        @JsonProperty(value = "father_id")
        private int fatherId;

        @ApiModelProperty(value = "创建时间")
        @JsonProperty(value = "create_time")
        private int createTime;

        @ApiModelProperty(value = "创建人ID")
        @JsonProperty(value = "create_id")
        private int createId;

        @ApiModelProperty(value = "修改人ID")
        @JsonProperty(value = "modify_id")
        private int modifyId;

        @ApiModelProperty(value = "修改时间")
        @JsonProperty(value = "modify_time")
        private int modifyTime;
    }
}
