package com.to8to.tbt.msc.service;

import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.repository.mysql.ComplexRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ComplexRepositoryImplTest extends BaseApplication {

    @Autowired
    private ComplexRepository complexRepository;
    @Test
    public void setAppMsgReaded() {
        complexRepository.setAppMsgReaded(82);
    }

    @Test
    public void queryTidListByNodeIds() {
    }
}