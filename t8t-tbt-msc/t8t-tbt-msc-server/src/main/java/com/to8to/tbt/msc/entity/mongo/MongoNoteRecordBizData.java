package com.to8to.tbt.msc.entity.mongo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author yason.li
 */
@Data
public class MongoNoteRecordBizData {
    @Field(value = "note_id")
    private int noteId;
}
