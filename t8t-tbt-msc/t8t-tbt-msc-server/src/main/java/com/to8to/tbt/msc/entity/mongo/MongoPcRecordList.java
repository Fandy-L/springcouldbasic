package com.to8to.tbt.msc.entity.mongo;

import lombok.Data;

import java.util.List;

/**
 * @author edmund.yu
 */
@Data
public class MongoPcRecordList {
    private List<MongoPcRecord> mongoPcRecords;
    private long totalRecords;
}
