package com.to8to.tbt.msc.repository.mongo.template;

import com.to8to.tbt.msc.dto.SearchMessageRecordDTO;
import com.to8to.tbt.msc.entity.MsgRecordWrapper;
import com.to8to.tbt.msc.entity.PageResult;
import com.to8to.tbt.msc.dto.ListMsgDTO;

import java.util.List;
import java.util.Map;

/**
 * @author juntao.guo
 */
public interface MsgRecordComplexRepository {

    /**
     * 综合消息查询
     *
     * @param params
     * @return
     */
    PageResult query(ListMsgDTO params);

    /**
     * 统计通道发送情况
     *
     * @param searchMessageRecordDTO
     * @return
     */
    Map<Integer, Integer> countChannel(SearchMessageRecordDTO searchMessageRecordDTO);
}
