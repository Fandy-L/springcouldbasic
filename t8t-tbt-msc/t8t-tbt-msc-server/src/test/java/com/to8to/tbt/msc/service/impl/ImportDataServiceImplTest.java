package com.to8to.tbt.msc.service.impl;

import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.service.ImportDataService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class ImportDataServiceImplTest extends BaseApplication {


    @InjectMocks
    private ImportDataService importDataServiceMock = new ImportDataServiceImpl();

    @Autowired
    private ImportDataService importDataService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void syncMsgRecord() {
        importDataService.syncMsgRecord();
    }

    @Test
    public void syncAppRecord() {
        importDataService.syncAppRecord();
    }
}