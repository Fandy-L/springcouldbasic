package com.to8to.tbt.msc.service.impl;

import com.to8to.common.util.DozerUtils;
import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.dto.GroupNoteCreateDTO;
import com.to8to.tbt.msc.dto.GroupNoteSearchDTO;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.entity.mongo.MongoGroupNote;
import com.to8to.tbt.msc.repository.mongo.GroupNoteRepository;
import com.to8to.tbt.msc.repository.mongo.template.GroupNoteComplexRepository;
import com.to8to.tbt.msc.service.GroupNoteService;
import com.to8to.tbt.msc.utils.RequestUtils;
import com.to8to.tbt.msc.utils.ResponseUtils;
import com.to8to.tbt.msc.utils.TimeUtils;
import com.to8to.tbt.msc.vo.GroupNoteGetVO;
import com.to8to.tbt.msc.vo.GroupNoteSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @author juntao.guo
 */
@Slf4j
@Service
public class GroupNoteServiceImpl implements GroupNoteService {
    @Autowired
    private GroupNoteRepository groupNoteRepository;

    @Autowired
    private GroupNoteComplexRepository groupNoteComplexRepository;

    @Override
    public ResResult<MsgCenterResponse> addGroupNote(GroupNoteCreateDTO groupNoteCreateDTO) {
        MongoGroupNote mongoGroupNote = DozerUtils.map(groupNoteCreateDTO, MongoGroupNote.class);
        try {
            mongoGroupNote.setSuccNum(0);
            mongoGroupNote.setFailNum(0);
            mongoGroupNote.setCreateTime(TimeUtils.getCurrentTimestampLong());
            mongoGroupNote.setSendTime(0L);
            mongoGroupNote.setInsertTime(TimeUtils.getCurrentTimestampLong());
            groupNoteRepository.save(mongoGroupNote);
        }catch (Exception e){
            log.warn("addGroupNote groupNoteCreateDTO:{} exception e:{}", groupNoteCreateDTO, e);
            return ResponseUtils.fail();
        }
        return ResponseUtils.success();
    }

    @Override
    public ResResult<MsgCenterResponse> delGroupNote(String id) {
        try {
            groupNoteRepository.deleteById(id);
        }catch (Exception e){
            log.warn("delGroupNote exception id:{} e:{}", id, e);
            return ResponseUtils.fail();
        }
        return ResponseUtils.success();
    }

    @Override
    public ResResult<MsgCenterResponse> updateGroupNote(String id, String content) {
        try {
            MongoGroupNote mongoGroupNote = groupNoteRepository.findById(id).orElse(null);
            if (mongoGroupNote == null){
                return ResponseUtils.fail();
            }
            mongoGroupNote.setContent(content);
            groupNoteRepository.save(mongoGroupNote);
        }catch (Exception e){
            log.warn("updateGroupNote exception id:{} content:{} e:{}", id, content, e);
            return ResponseUtils.fail();
        }
        return ResponseUtils.success();
    }

    @Override
    public GroupNoteSearchVO searchGroupNote(GroupNoteSearchDTO groupNoteSearchDTO) {
        try {
            groupNoteSearchDTO.setPageInfo(RequestUtils.filterPageInfo(groupNoteSearchDTO.getPageInfo()));
            return groupNoteComplexRepository.searchGroupNote(groupNoteSearchDTO);
        }catch (Exception e){
            log.warn("searchGroupNote groupNoteSearchDTO:{} exception e:{}", groupNoteSearchDTO, e);
        }
        return GroupNoteSearchVO.builder()
                .groupNotes(new ArrayList<>())
                .totalPages(0L)
                .totalRecords(0L)
                .build();
    }

    @Override
    public GroupNoteGetVO getGroupNote(String id) {
        try {
            MongoGroupNote mongoGroupNote = groupNoteRepository.findById(id).orElse(null);
            if (mongoGroupNote != null){
                GroupNoteGetVO groupNoteGetVO = DozerUtils.map(mongoGroupNote, GroupNoteGetVO.class);
                groupNoteGetVO.setAttachedId(mongoGroupNote.getId());
                return groupNoteGetVO;
            }
        }catch (Exception e){
            log.warn("getGroupNote exception id:{} e:{}", id, e);
        }
        return null;
    }

    @Override
    public void updateGroupNoteSendNumById(MongoGroupNote mongoGroupNote, int succNum, int failNum) {
        mongoGroupNote.setSuccNum(mongoGroupNote.getSuccNum() + succNum);
        mongoGroupNote.setFailNum(mongoGroupNote.getFailNum() + failNum);
        mongoGroupNote.setSendTime(TimeUtils.getCurrentTimestamp());
        try {
            groupNoteRepository.save(mongoGroupNote);
        }catch (Exception e){
            log.warn("updateGroupNoteSendNumById mysql error mongoGroupNote:{} succNum:{} failNum:{}", mongoGroupNote, succNum, failNum);
        }
    }
}
