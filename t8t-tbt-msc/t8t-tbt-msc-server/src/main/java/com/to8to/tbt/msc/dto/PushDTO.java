package com.to8to.tbt.msc.dto;

import lombok.Data;

import java.util.List;

@Data
public class PushDTO {
    private String to8toMsgId;

    private Integer appId;

    private List<Integer> uidList;

    private List<String> firstIdList;

    private String startTime;

    private String expireTime;

    private Byte scope;

    private NotificationDTO notification;

    private MessageDTO message;

    private Boolean apnsProduction;
}
