package com.to8to.tbt.msc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.to8to.tbt.msc.entity.MsgRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author juntao.guo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListMsgVO<T> {
    public List<T> msgRecords;

    @JsonProperty(value = "total_records")
    public Long totalRecords;

    @JsonProperty(value = "total_pages")
    public Long totalPages;
}
