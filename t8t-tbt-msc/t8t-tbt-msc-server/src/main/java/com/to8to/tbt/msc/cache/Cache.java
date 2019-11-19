package com.to8to.tbt.msc.cache;

import com.to8to.sc.component.redis.operation.AbstractPrefixOperation;

/**
 * @author pajero.quan
 */
public interface Cache<T, ID> {

    /**
     * 获取RedisOperation
     *
     * @return
     */
    AbstractPrefixOperation<T> operation();

    /**
     * 生成key
     *
     * @param id
     * @return
     */
    String key(ID id);

    /**
     * 单位：s
     *
     * @return
     */
    int ttl();

    /**
     * 加入缓存
     *
     * @param id
     * @param t
     */
    void put(ID id, T t);

    /**
     * 查询缓存
     *
     * @param id
     * @return
     */
    T get(ID id);

    /**
     * 移除缓存
     *
     * @param id
     */
    void evict(ID id);

    /**
     * 获取过期时间
     *
     * @param id
     * @return
     */
    Long getExpire(ID id);
}
