package com.to8to.tbt.msc.repository.mongo;

import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.dto.SearchMessageRecordDTO;
import com.to8to.tbt.msc.repository.mongo.template.MsgRecordComplexRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author juntao.guo
 */
@RunWith(value = SpringRunner.class)
public class MsgRecordComplexRepositoryTest extends BaseApplication {

    /**
     * 查询的时间区间
     */
    private int startTime = 1569398718;
    private int endTime = 1569465500;

    @Autowired
    private MsgRecordComplexRepository msgRecordComplexRepository;

    @Test
    public void countChannel() {
        msgRecordComplexRepository.countChannel(SearchMessageRecordDTO.builder().build());
    }

    @Test
    public void countChannelByTime() {
        msgRecordComplexRepository.countChannel(SearchMessageRecordDTO.builder()
                .stime(startTime)
                .etime(endTime)
                .build());
    }

    @Test
    public void countChannelByStartTime() {
        msgRecordComplexRepository.countChannel(SearchMessageRecordDTO.builder()
                .stime(startTime)
                .build());
    }

    @Test
    public void countChannelByEndTime() {
        msgRecordComplexRepository.countChannel(SearchMessageRecordDTO.builder()
                .etime(endTime)
                .build());
    }
}
