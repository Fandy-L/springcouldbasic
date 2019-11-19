package com.to8to.tbt.msc.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 * @description
 * @author: pajero.quan
 * @date: 2019/4/24 13:57
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("通道")
public class ChannelVO {
    /**
     * 自增id
     */
    private Integer id;

    /**
     * 应用ID
     */
    private Integer pushAppId;

    /**
     * 通道。极光：0；苹果：1;华为：2；小米:3；OPPO：4；VIVO:5;魅族：6
     */
    private Byte channel;

    /**
     * AppId
     */
    private String appId;

    /**
     * AppKey
     */
    private String appKey;

    /**
     * AppSecret
     */
    private String appSecret;

    /**
     * AppServerSecret
     */
    private String masterSecret;

    /**
     * 禁用：0；启用：1
     */
    private Boolean enable;

}
