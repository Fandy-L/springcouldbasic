package com.to8to.tbt.msc.service;

import com.to8to.tbt.msc.dto.NoteExHistoryDTO;
import com.to8to.tbt.msc.dto.NoteReplyGetDTO;
import com.to8to.tbt.msc.dto.SearchMessageRecordDTO;
import com.to8to.tbt.msc.vo.NoteReplyVO;
import com.to8to.tbt.msc.vo.admin.CountTemplateSendVO;

import java.util.List;
import java.util.Map;

/**
 * @author juntao.guo
 */
public interface AdminCountService {
    /**
     * 统计模板发送情况
     *
     * @param searchMessageRecordDTO
     * @return
     */
    CountTemplateSendVO countTemplateSend(SearchMessageRecordDTO searchMessageRecordDTO);

    /**
     * 统计通道发送情况
     *
     * @param searchMessageRecordDTO
     * @return
     */
    List<Map<String, Long>> countChannelType(SearchMessageRecordDTO searchMessageRecordDTO);

    /**
     * 查询短信上行记录
     *
     * @param noteReplyGetDTO
     * @return
     */
    List<NoteReplyVO> getNoteReply(NoteReplyGetDTO noteReplyGetDTO);

    /**
     * 统计APP消息记录
     *
     * @param searchMessageRecordDTO
     * @return
     */
    Map<String, Long> countAppMessageRecord(SearchMessageRecordDTO searchMessageRecordDTO);

    /**
     * 查询历史发送记录和回复记录
     *
     * @param noteExHistoryDTO
     * @return
     */
    List<NoteReplyVO> getNoteExHistory(NoteExHistoryDTO noteExHistoryDTO);
}
