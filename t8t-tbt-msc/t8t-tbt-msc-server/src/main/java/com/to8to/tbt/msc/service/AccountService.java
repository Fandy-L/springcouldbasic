package com.to8to.tbt.msc.service;

import com.to8to.tbt.msc.entity.UserWrapper;

import java.util.HashMap;
import java.util.List;

/**
 * @author juntao.guo
 */
public interface AccountService {
    /**
     * 批量查询业主信息
     * @param uidList
     * @return
     */
    HashMap<Integer, UserWrapper.Owner> batchQueryOwner(List<Integer> uidList);
}
