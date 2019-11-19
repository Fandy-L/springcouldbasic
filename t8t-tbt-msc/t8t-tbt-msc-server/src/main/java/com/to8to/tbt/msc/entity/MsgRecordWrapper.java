package com.to8to.tbt.msc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo
 */
public class MsgRecordWrapper {

    /**
     * 短信记录根据渠道统计的结果集
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NoteRecordCountChannel {
        @JsonProperty(value = "id")
        private Integer id;

        private Integer count;
    }
}
