package com.to8to.tbt.msc.cache;

import com.to8to.sc.component.redis.operation.AbstractPrefixOperation;
import com.to8to.tbt.msc.entity.mysql.push.App;
import com.to8to.tbt.msc.entity.mysql.push.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author pajero.quan
 */
@Component
public class ChannelCache extends AbstractCache<Channel, String> {
    @Autowired
    private AbstractPrefixOperation<Channel> operation;

    @Override
    public AbstractPrefixOperation<Channel> operation() {
        return operation;
    }

    @Override
    public String key(String id) {
        return String.format("push:channel:%s", id);
    }

    @Override
    public int ttl() {
        //1小时
        return 3600;
    }
}

