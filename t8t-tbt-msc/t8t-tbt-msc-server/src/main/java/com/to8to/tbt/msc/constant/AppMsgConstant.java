package com.to8to.tbt.msc.constant;

/**
 * @author juntao.guo
 */
public interface AppMsgConstant {
    /**
     * 分页条数
     */
    int PAGE_SIZE = 20;
    int PAGE_NUMBER = 1;
    int PAGE_MAX_SIZE = 1000;
    /**
     * 图片主域
     */
    String HOST_PIC = "https://pic.to8to.com/";
    String HOST_PIC_HOTAREA = "http://pic.to8to.com/hotarea/";
    String OWNER_AVATAR_DEFAULT = "https://img.to8to.com/headphoto/906.png";
    /**
     * 消息聚合页-群发消息统计的起始时间
     */
    int PUSH_MESSAGE_QUERY_START_NUM = 7;
    /**
     * 审核消息通知图标
     */
    String ICON_MSG_NOTICE = "https://img.to8to.com/front_end/icon/msg_caution.png";
}
