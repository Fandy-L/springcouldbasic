package com.to8to.tbt.msc.repository.mysql.main;

import com.to8to.tbt.msc.entity.mysql.main.TimingMessage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author juntao.guo
 */
public interface TimingMessageRepository extends CrudRepository<TimingMessage, Integer> {

    /**
     * 查询未发送消息
     *
     * @param isSend
     * @param startTime
     * @param endTime
     * @return
     */
    List<TimingMessage> findAllByIsSendAndDelayTimeIsBetween(int isSend, int startTime, int endTime);

    /**
     * 根据模板和手机号ID统计消息数量
     *
     * @param tid
     * @param phoneId
     * @return
     */
    int countByTidAndPhoneId(int tid, int phoneId);
}
