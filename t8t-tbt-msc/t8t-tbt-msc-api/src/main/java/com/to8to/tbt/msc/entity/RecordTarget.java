package com.to8to.tbt.msc.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author juntao.guo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordTarget {
    @JsonProperty(value = "user_id")
    public String userId;

    public String contact;

    @JsonProperty(value = "user_type")
    public Integer userType;

    @JsonProperty(value = "send_type")
    public Integer sendType;
}
