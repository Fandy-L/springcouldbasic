package com.to8to.tbt.msc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author edmund.yu
 */
@Data
public class KeywordVO {

    @ApiModelProperty(value = "_id")
    @JsonProperty(value = "_id")
    private String id;

    @ApiModelProperty(value = "id")
    @JsonProperty(value = "id")
    private String nickId;

    @ApiModelProperty(value = "关键词")
    private String keyword;

    @ApiModelProperty(value = "数据库")
    private String database;

    @ApiModelProperty(value = "表名")
    private String table;

    @ApiModelProperty(value = "栏")
    private String column;

    @ApiModelProperty(value = "选择栏")
    @JsonProperty(value = "select_column")
    private String selectColumn;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty(value = "create_time")
    private Long createTime;

}
