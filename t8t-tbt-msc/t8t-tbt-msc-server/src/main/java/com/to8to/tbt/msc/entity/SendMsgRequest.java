package com.to8to.tbt.msc.entity;

import lombok.Data;

import java.util.List;

/**
 * @author edmund.yu
 */
@Data
public class SendMsgRequest {
    private String ns;

    private List<RecordTarget> targets;

    private String content;

    private String fromUserId;

    private PcWrapper.BizData bizData;

    private String title;

    private long expireTime;

    private int bizId = 0;

    private int yjindu = 0;

    private int deal = 0;

    private int ywId = 0;

    private int ywType = 0;

    private int channel = 0;

    private int zid = 0;

    private int cityid = 0;

    private int ispass = 0;

    private String personal;
}
