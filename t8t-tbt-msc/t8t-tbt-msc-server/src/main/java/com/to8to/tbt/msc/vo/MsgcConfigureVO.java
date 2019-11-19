package com.to8to.tbt.msc.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "配置信息集果集")
public class MsgcConfigureVO {
    @ApiModelProperty(value = "主键ID")
    private int cid;

    @ApiModelProperty(value = "节点类型:1.业务类型 2.业务项 3.发送节点 4.接收对象 5.产品模块")
    private int configType;

    @ApiModelProperty(value = "节点描述")
    private String configDescribe;

    @ApiModelProperty(value = "父节点ID")
    private int fatherId;

    @ApiModelProperty(value = "创建时间")
    private int createTime;

    @ApiModelProperty(value = "创建人ID")
    private int createId;

    @ApiModelProperty(value = "修改人ID")
    private int modifyId;

    @ApiModelProperty(value = "修改时间")
    private int modifyTime;

    @ApiModelProperty(value = "昵称")
    private String nick;
}
