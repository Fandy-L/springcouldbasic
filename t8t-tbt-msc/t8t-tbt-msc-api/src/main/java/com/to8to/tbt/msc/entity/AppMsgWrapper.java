package com.to8to.tbt.msc.entity;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo
 */
public class AppMsgWrapper {
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "消息跳转参数")
    public static class MsgJumpParams {
        @ApiModelProperty(value = "模块类型", example = "diary")
        private String moduleCode;

        @ApiModelProperty(value = "内容ID", example = "1098")
        private Integer objectId;

        @ApiModelProperty(value = "H5跳转地址", example = "http://www.baidu.com")
        private String url;

        @ApiModelProperty(value = "扩展参数")
        private JSONObject extraDataParams;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "消息附加内容")
    public static class MsgContent{
        @ApiModelProperty(value = "评论内容")
        private Comment comment;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "评论内容详情")
    public static class Comment{
        @ApiModelProperty(value = "原始评论内容")
        private String origin;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "业务参数")
    public static class BizParams{
        @ApiModelProperty(value = "模块标识", example = "diary")
        private String moduleCode;

        @ApiModelProperty(value = "内容ID", example = "1098")
        private Integer objectId;

        @ApiModelProperty(value = "标题", example = "消息标题")
        private String title;

        @ApiModelProperty(value = "内容", example = "消息内容")
        private String content;

        @ApiModelProperty(value = "原始内容描述")
        private String originContentDesc;

        @ApiModelProperty(value = "原始内容图片")
        private String originContentPic;

        @ApiModelProperty(value = "跳转地址")
        private String url;

        @ApiModelProperty(value = "扩展参数")
        private JSONObject extraDataParams;

        @ApiModelProperty(value = "用户类型1-商家")
        private Integer triggerUserType;

        @ApiModelProperty(value = "账号ID")
        private Integer triggerAccountId;

        @ApiModelProperty(value = "业主ID")
        private Integer triggerUid;

        @ApiModelProperty(value = "备注")
        private String remark;

        @ApiModelProperty(value = "节点ID")
        private Integer noteId;

        @ApiModelProperty(value = "对象ID")
        private Integer triggerId;

        @ApiModelProperty(value = "消息类型13-互动消息14-审核通知15-系统消息")
        private Integer type;

        @ApiModelProperty(value = "原生页面跳转链接")
        private String schemeUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "聚合消息结果项")
    public static class AggregationMsgItem{
        @ApiModelProperty(value = "总数")
        private Integer count;

        @ApiModelProperty(value = "消息发送日期")
        private String time;

        @ApiModelProperty(value = "消息内容")
        private String message;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "综合消息业务参数")
    public static class BizData{
        private Integer yid;

        private Integer type;
    }
}
