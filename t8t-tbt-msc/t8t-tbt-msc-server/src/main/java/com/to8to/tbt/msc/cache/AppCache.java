package com.to8to.tbt.msc.cache;

import com.to8to.sc.component.redis.operation.AbstractPrefixOperation;
import com.to8to.tbt.msc.entity.mysql.push.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author pajero.quan
 */
@Component
public class AppCache extends AbstractCache<App, Integer> {
    @Autowired
    private AbstractPrefixOperation<App> appOperation;

    @Override
    public AbstractPrefixOperation<App> operation() {
        return appOperation;
    }

    @Override
    public String key(Integer id) {
        return String.format("push:app:%s", id);
    }

    @Override
    public int ttl() {
        //1小时
        return 3600;
    }
}
