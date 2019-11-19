package com.to8to.tbt.msc.dto;

import lombok.Data;

import java.util.List;

/**
 * @author pajero.quan
 */
@Data
public class QywxTextMsgBodyDTO {
    private String content;
    private List<String> mentioned_list;
    private List<String> mentioned_mobile_list;
}
