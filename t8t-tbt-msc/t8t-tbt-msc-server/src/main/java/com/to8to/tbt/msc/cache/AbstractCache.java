package com.to8to.tbt.msc.cache;

import java.util.concurrent.TimeUnit;

/**
 * 实体缓存抽象类
 *
 * @author pajero.quan
 */
public abstract class AbstractCache<T, ID> implements Cache<T, ID> {
    @Override
    public void put(ID id, T t) {
        this.operation().set(this.key(id), t, this.ttl(), TimeUnit.SECONDS);
    }

    @Override
    public T get(ID id) {
        return this.operation().get(this.key(id));
    }

    @Override
    public void evict(ID id) {
        this.operation().delete(this.key(id));
    }

    @Override
    public Long getExpire(ID id) {
        return this.operation().getExpire(this.key(id));
    }
}
