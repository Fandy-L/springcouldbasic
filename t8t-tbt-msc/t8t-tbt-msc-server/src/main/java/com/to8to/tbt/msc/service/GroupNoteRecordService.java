package com.to8to.tbt.msc.service;

import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.dto.GroupNoteRecordSearchDTO;
import com.to8to.tbt.msc.dto.GroupNoteRecordUpdateDTO;
import com.to8to.tbt.msc.entity.GroupNoteRecordWrapper;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.vo.GroupNoteRecordSearchVO;
import com.to8to.tbt.msc.vo.ImportNoteGroupRecordDTO;

import java.util.List;

/**
 * @author juntao.guo
 */
public interface GroupNoteRecordService {

    /**
     * 批量删除群发短信群发记录
     *
     * @param idList
     * @return
     */
    ResResult<MsgCenterResponse> delGroupNoteRecord(List<String> idList);

    /**
     * 更新群发短信记录
     *
     * @param groupNoteRecordUpdateDTO
     * @return
     */
    ResResult<MsgCenterResponse> updateGroupNoteRecord(GroupNoteRecordUpdateDTO groupNoteRecordUpdateDTO);

    /**
     * 获取群发记录列表
     *
     * @param groupNoteRecordSearchDTO
     * @return
     */
    GroupNoteRecordSearchVO searchGroupNoteRecord(GroupNoteRecordSearchDTO groupNoteRecordSearchDTO);

    /**
     * 导出群发短信记录
     *
     * @param groupNoteRecordSearchDTO
     * @return
     */
    List<GroupNoteRecordWrapper.GroupNoteRecord> exportGroupNoteRecord(GroupNoteRecordSearchDTO groupNoteRecordSearchDTO);

    /**
     * 导入群发消息记录
     *
     * @param groupNoteId
     * @param importNoteGroupRecordDTOList
     * @return
     */
    ResResult<MsgCenterResponse> importGroupNoteObject(String groupNoteId, List<ImportNoteGroupRecordDTO> importNoteGroupRecordDTOList);

    /**
     * 群发短信
     *
     * @param groupNoteId
     * @param userName
     * @return
     */
    ResResult<MsgCenterResponse> groupSendNote(String groupNoteId, String userName);

    /**
     * 重新群发短信
     *
     * @param groupNoteId
     * @param ids
     * @return
     */
    ResResult<MsgCenterResponse> groupReSendNote(String groupNoteId, List<String> ids);
}
