package com.to8to.tbt.msc.enumeration;

/**
 * @author yason.li
 */

public enum AppSendModeEnum {
    /* 保存消息并发送 */
    SAVE_SEND(0, "保存消息并发送"),
    /* 保存消息不发送 */
    SAVE_UNSEND(1, "保存消息不发送"),
    /* 不保存消息发送 */
    UNSAVE_SEND(2, "不保存消息发送"),

    UNSAVE_UNSEND(3, "不保存消息发送");
    private int sendMode;
    private String name;

    AppSendModeEnum(int sendMode, String name) {
        this.sendMode = sendMode;
        this.name = name;
    }

    public static int of(Integer sendMode) {
        if (SAVE_SEND.getSendMode() == sendMode) {
            return SAVE_SEND.getSendMode();
        } else if (SAVE_UNSEND.getSendMode() == sendMode) {
            return SAVE_UNSEND.getSendMode();
        } else if (UNSAVE_SEND.getSendMode() == sendMode) {
            return UNSAVE_SEND.getSendMode();
        } else if (UNSAVE_UNSEND.getSendMode() == sendMode) {
            return SAVE_SEND.getSendMode();
        } else {
            return SAVE_SEND.getSendMode();
        }
    }

    public int getSendMode() {
        return sendMode;
    }

    public String getName() {
        return name;
    }

    public boolean compare(Integer value){
        return value != null && sendMode == value.intValue();
    }
}
