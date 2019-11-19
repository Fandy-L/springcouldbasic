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
public class SendTencentSmsVO {
    /**
     * 状态码
     */
    private String result;

    /**
     * 状态码描述
     */
    private String errmsg;

    /**
     * 扩展数据
     */
    private String ext;

    private String sid;

    private String count;

    private String fee;
}
