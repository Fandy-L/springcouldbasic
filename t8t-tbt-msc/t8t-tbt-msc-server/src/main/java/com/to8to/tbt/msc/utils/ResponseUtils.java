package com.to8to.tbt.msc.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.to8to.common.search.PageResult;
import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;
import com.to8to.tbt.msc.common.MyExceptionStatus;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.entity.OldMsgResponse;
import com.to8to.tbt.msc.entity.ResponseWrapper;
import com.to8to.tbt.msc.entity.response.StatusResultResponse;

import java.util.ArrayList;
import java.util.List;

import static com.to8to.tbt.msc.common.MyExceptionStatus.SEND_ALL_MESSAGE_FAIL;
import static com.to8to.tbt.msc.common.MyExceptionStatus.SEND_ALL_MESSAGE_SUCCESS;

/**
 * @author juntao.guo
 */
public class ResponseUtils {

    /**
     * 生成默认的响应结果
     *
     * @return
     */
    public static ResponseWrapper.ResultVO buildResult() {
        return ResponseWrapper.ResultVO.builder()
                .total(0)
                .list(new ArrayList())
                .data(new JSONObject())
                .build();
    }

    /**
     * 生成列表集的响应结果
     *
     * @param total
     * @param list
     * @return
     */
    public static ResponseWrapper.ResultVO buildResult(int total, List list) {
        return ResponseWrapper.ResultVO.builder()
                .total(total)
                .list(list)
                .data(new JSONObject())
                .build();
    }

    /**
     * 生成列表集的响应结果
     *
     * @return
     */
    public static PageResult buildPageResult() {
        PageResult pageResult = new PageResult();
        pageResult.setTotal(0L);
        pageResult.setRows(Lists.newArrayList());
        return pageResult;
    }

    /**
     * 生成列表集的响应结果
     *
     * @param total
     * @param list
     * @return
     */
    public static PageResult buildPageResult(int total, List list) {
        PageResult pageResult = new PageResult();
        pageResult.setTotal(total);
        pageResult.setRows(list);
        return pageResult;
    }

    /**
     * 根据总计录数计算总页码
     *
     * @param total
     * @param pageSize
     * @return
     */
    public static int calculateTotalPages(long total, int pageSize) {
        if (total > 0L) {
            long totalPages = (total % pageSize) > 0 ? Float.floatToIntBits(total / pageSize) + 1 : total / pageSize;
            return (int) totalPages;
        }
        return 0;
    }

    /**
     * 生成成功的返回结果-兼容老服务返回的数据格式
     *
     * @param desc
     * @return
     */
    public static ResResult<OldMsgResponse> success(String desc) {
        int status = 0;
        OldMsgResponse oldMsgResponse = OldMsgResponse.builder()
                .status(MyExceptionStatus.OLD_MSG_RESPONSE_SUCCESS.getCode())
                .msg(desc)
                .build();
        return new ResResult(status, desc, oldMsgResponse);
    }

    /**
     * 生成成功的返回结果-兼容老服务返回的数据格式
     *
     * @return
     */
    public static ResResult<MsgCenterResponse> success() {
        MsgCenterResponse msgCenterResponse = new MsgCenterResponse(MyExceptionStatus.OLD_MSG_CENTER_RESPONSE_SUCCESS.getCode(), MyExceptionStatus.OLD_MSG_CENTER_RESPONSE_SUCCESS.getMessage());
        return ResUtils.data(msgCenterResponse);
    }

    /**
     * 生成成功的返回结果-兼容老服务返回的数据格式
     *
     * @return
     */
    public static ResResult<MsgCenterResponse> centerResponseSuccess(String desc) {
        MsgCenterResponse msgCenterResponse = new MsgCenterResponse(MyExceptionStatus.OLD_MSG_CENTER_RESPONSE_SUCCESS.getCode(), desc);
        ResResult<MsgCenterResponse> resResult = ResUtils.data(msgCenterResponse);
        resResult.setDesc(desc);
        return resResult;
    }

    /**
     * 生成成功的返回结果-兼容老服务返回的数据格式
     *
     * @param code
     * @param desc
     * @return
     */
    public static ResResult<MsgCenterResponse> centerResponseSuccess(int code, String desc) {
        MsgCenterResponse msgCenterResponse = new MsgCenterResponse(code, desc);
        ResResult<MsgCenterResponse> resResult = ResUtils.data(msgCenterResponse);
        resResult.setDesc(desc);
        return resResult;
    }

    /**
     * 生成成功的返回结果-兼容老服务返回的数据格式
     *
     * @return
     */
    public static ResResult<MsgCenterResponse> fail() {
        MsgCenterResponse msgCenterResponse = new MsgCenterResponse(MyExceptionStatus.OLD_MSG_CENTER_RESPONSE_FAIL.getCode(), MyExceptionStatus.OLD_MSG_CENTER_RESPONSE_FAIL.getMessage());
        return new ResResult(MyExceptionStatus.OLD_MSG_CENTER_RESPONSE_FAIL.getCode(), MyExceptionStatus.OLD_MSG_CENTER_RESPONSE_FAIL.getMessage(), msgCenterResponse);
    }

    /**
     * 生成成功的返回结果-兼容老服务返回的数据格式
     *
     * @return
     */
    public static ResResult<MsgCenterResponse> centerResponseFail(String desc) {
        MsgCenterResponse msgCenterResponse = new MsgCenterResponse(MyExceptionStatus.OLD_MSG_CENTER_RESPONSE_FAIL.getCode(), desc);
        return new ResResult(MyExceptionStatus.OLD_MSG_CENTER_RESPONSE_FAIL.getCode(), desc, msgCenterResponse);
    }

    /**
     * 生成失败的返回结果-兼容老服务返回的数据格式
     *
     * @param desc
     * @return
     */
    public static ResResult<OldMsgResponse> fail(String desc) {
        int status = 10000;
        OldMsgResponse oldMsgResponse = OldMsgResponse.builder()
                .status(MyExceptionStatus.OLD_MSG_RESPONSE_FAIL.getCode())
                .msg(desc)
                .build();
        return new ResResult(status, desc, oldMsgResponse);
    }

    /**
     * 根据响应结果判断成功还是失败
     *
     * @param resResult
     * @return
     */
    public static boolean isSuccess(ResResult resResult) {
        return resResult.getStatus() == 0 ? true : false;
    }

    /**
     * 生成消息发送成功的响应包
     *
     * @return
     */
    public static StatusResultResponse<String> sendMessageSuccessResponse(){
        return buildStatusResultResponse(SEND_ALL_MESSAGE_SUCCESS.getCode(), SEND_ALL_MESSAGE_SUCCESS.getMessage());
    }

    /**
     * 生成消息发送失败的响应包
     *
     * @return
     */
    public static StatusResultResponse<String> sendMessageFailResponse(){
        return buildStatusResultResponse(SEND_ALL_MESSAGE_FAIL.getCode(), SEND_ALL_MESSAGE_FAIL.getMessage());
    }

    /**
     * 发成响应包－结构status and result
     *
     * @param code
     * @param message
     * @return
     */
    public static StatusResultResponse<String> buildStatusResultResponse(int code, String message){
        return StatusResultResponse.<String>builder()
                .status(code)
                .result(message)
                .build();
    }
}
