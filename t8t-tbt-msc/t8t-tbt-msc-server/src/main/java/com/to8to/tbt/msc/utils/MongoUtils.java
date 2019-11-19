package com.to8to.tbt.msc.utils;

import org.bson.types.ObjectId;

public class MongoUtils {
    public static String getObjectId() {
        ObjectId objectId = new ObjectId();
        return objectId.toHexString();
    }
}
