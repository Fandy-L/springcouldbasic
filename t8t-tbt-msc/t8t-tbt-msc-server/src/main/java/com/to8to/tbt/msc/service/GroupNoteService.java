package com.to8to.tbt.msc.service;

import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.dto.GroupNoteCreateDTO;
import com.to8to.tbt.msc.dto.GroupNoteSearchDTO;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.entity.mongo.MongoGroupNote;
import com.to8to.tbt.msc.vo.GroupNoteGetVO;
import com.to8to.tbt.msc.vo.GroupNoteSearchVO;

/**
 * @author juntao.guo
 */
public interface GroupNoteService {

    /**
     * 新增群发短信模板
     *
     * @param groupNoteCreateDTO
     * @return
     */
    ResResult<MsgCenterResponse> addGroupNote(GroupNoteCreateDTO groupNoteCreateDTO);

    /**
     * 删除群发短信模板
     *
     * @param id
     * @return
     */
    ResResult<MsgCenterResponse> delGroupNote(String id);

    /**
     * 更新群发短信模板
     *
     * @param id
     * @param content
     * @return
     */
    ResResult<MsgCenterResponse> updateGroupNote(String id, String content);

    /**
     * 搜索群发短信模板
     *
     * @param groupNoteSearchDTO
     * @return
     */
    GroupNoteSearchVO searchGroupNote(GroupNoteSearchDTO groupNoteSearchDTO);

    /**
     * 获取单个群发短信模板
     *
     * @param id
     * @return
     */
    GroupNoteGetVO getGroupNote(String id);

    /**
     * 更新短信发送成功和失败的数量
     *
     * @param mongoGroupNote
     * @param succNum
     * @param failNum
     */
    void updateGroupNoteSendNumById(MongoGroupNote mongoGroupNote, int succNum, int failNum);
}
