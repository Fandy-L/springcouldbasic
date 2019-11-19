package com.to8to.tbt.msc.constant;

/**
 * @author juntao.guo
 */
public interface MsgSendConstant {

    /**
     * 异步消息发送的队列名
     */
    String MQ_ASYNC_MESSAGE_QUEUE = "msc.async.message";
    String MQ_ASYNC_APP_NOTE_QUEUE = "msc.async.app.note";

    String MQ_NOTE_SYNC_QUEUE_TEST = "msgsync_test";
    String MQ_NOTE_SYNC_QUEUE_UAT = "msgsync_uat";
    String MQ_NOTE_SYNC_QUEUE_IDC = "msgsync_idc";
}
