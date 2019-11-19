package com.to8to.tbt.msc.repository.es;

import com.alibaba.fastjson.JSONObject;
import com.to8to.tbt.msc.dto.SearchMessageRecordDTO;
import com.to8to.tbt.msc.vo.NoteReplyVO;
import com.to8to.tbt.msc.vo.SearchMessageRecordVO;

import java.util.List;
import java.util.Map;

/**
 * @author juntao.guo
 */
public interface EsMsgRecordRepository {
    /**
     * 查询短信记录
     *
     * @param searchMessageRecordDTO
     * @return
     */
    SearchMessageRecordVO querySmsRecord(SearchMessageRecordDTO searchMessageRecordDTO);

    /**
     * 查询APP消息记录
     *
     * @param searchMessageRecordDTO
     * @return
     */
    SearchMessageRecordVO queryAppRecord(SearchMessageRecordDTO searchMessageRecordDTO);

    /**
     * 查询APP消息数
     * @param uidList
     * @param tidList
     * @param isRead
     * @return
     */
    Long getAppMsgCount(List<Integer> uidList, List<Integer> tidList, Integer isRead);

    /**
     * 根据节点id查询APP消息数
     * @param uidList
     * @param nodeList
     * @param isRead
     * @return
     */
    Long getAppMsgCountByNodeList(List<Integer> uidList, List<Integer> nodeList, Integer isRead);

    /**
     * 统计模板发送情况
     *
     * @param searchMessageRecordDTO
     * @return
     */
    Map<String, Long> countTemplateSend(SearchMessageRecordDTO searchMessageRecordDTO);

    /**
     * 统计通道发送情况
     *
     * @param searchMessageRecordDTO
     * @return
     */
    Map<String, Long> countChannelType(SearchMessageRecordDTO searchMessageRecordDTO);

    /**
     * 统计APP消息发送情况
     *
     * @param searchMessageRecordDTO
     * @return
     */
    Map<String, Long> countAppMessageRecord(SearchMessageRecordDTO searchMessageRecordDTO);

    /**
     * 根据手机号搜索短信发送记录
     *
     * @param phoneId
     * @return
     */
    List<NoteReplyVO> getNoteRecord(Integer phoneId);

    /**
     * 保存消息
     *
     * @param data
     * @return
     */
    void saveRecord(JSONObject data);
}
