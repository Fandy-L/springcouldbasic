package com.to8to.tbt.msc.service;

import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.dto.GroupNoteCreateDTO;
import com.to8to.tbt.msc.dto.GroupNoteSearchDTO;
import com.to8to.tbt.msc.entity.GroupNoteWrapper;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.entity.SearchTime;
import com.to8to.tbt.msc.utils.TimeUtils;
import com.to8to.tbt.msc.vo.GroupNoteGetVO;
import com.to8to.tbt.msc.vo.GroupNoteSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author juntao.guo
 */
@Slf4j
@RunWith(value = SpringRunner.class)
public class GroupNoteServiceTest extends BaseApplication {

    @Autowired
    private GroupNoteService groupNoteService;

    @Test
    public void searchGroupNote(){
        long createStartTime = 1531220477L;
        long createEndTime = 1570785974L;
        String content = "三等奖";
        int timeType = 0;
        SearchTime searchTime = SearchTime.builder()
                .startTime(createStartTime)
                .endTime(createEndTime)
                .build();
        GroupNoteSearchDTO groupNoteSearchDTO = GroupNoteSearchDTO.builder()
                .timeType(timeType)
                .content(content)
                .searchTime(searchTime)
                .build();
        GroupNoteSearchVO groupNoteSearchVO = groupNoteService.searchGroupNote(groupNoteSearchDTO);
        log.debug("total:{} pageNum:{} groupNoteSearchVO:{}", groupNoteSearchVO.getTotalRecords(), groupNoteSearchVO.getTotalPages(), groupNoteSearchVO);
        for (GroupNoteWrapper.GroupNote groupNote : groupNoteSearchVO.getGroupNotes()){
            Assert.assertTrue(groupNote.getContent().indexOf(content) >= 0 && groupNote.getCreateTime() >= createStartTime && groupNote.getSendTime() <= createEndTime);
            Assert.assertTrue(groupNote.getCreateTime() >= createStartTime);
            Assert.assertTrue(groupNote.getSendTime() <= createEndTime);
        }
    }

    @Test
    public void addGroupNote(){
        String content = "测试信息" + TimeUtils.getCurrentTimestamp();
        String rtxName = "juntao.guo";
        String department = "技术研发部门1222";
        GroupNoteCreateDTO groupNoteCreateDTO = GroupNoteCreateDTO.builder()
                .content(content)
                .rtxName(rtxName)
                .department(department)
                .build();
        ResResult<MsgCenterResponse> resResult = groupNoteService.addGroupNote(groupNoteCreateDTO);
        log.debug("resResult:{}", resResult);
        List<GroupNoteWrapper.GroupNote> groupNoteList = searchGroupNoteByContent(content);
        for (GroupNoteWrapper.GroupNote groupNote : groupNoteList){
            Assert.assertTrue(groupNote.getContent().equals(content) && groupNote.getCreateTime() > 0 && groupNote.getRtxName().equals(rtxName) && groupNote.getDepartment().equals(department));
        }
    }

    @Test
    public void updateGroupNote(){
        String id = "5da172a73ee59e18c4437eac";
        String content = "测试信息更新" + TimeUtils.getCurrentTimestamp();
        ResResult<MsgCenterResponse> resResult = groupNoteService.updateGroupNote(id, content);
        log.debug("resResult:{}", resResult);
        List<GroupNoteWrapper.GroupNote> groupNoteList = searchGroupNoteByContent(content);
        for (GroupNoteWrapper.GroupNote groupNote : groupNoteList){
            Assert.assertTrue(groupNote.getContent().equals(content));
        }
    }

    @Test
    public void delGroupNote(){
        String content = "测试信息";
        List<GroupNoteWrapper.GroupNote> groupNoteList = searchGroupNoteByContent(content);
        for (GroupNoteWrapper.GroupNote groupNote : groupNoteList){
            ResResult<MsgCenterResponse> resResult = groupNoteService.delGroupNote(groupNote.getId());
            log.debug("id:{} resResult:{}", groupNote.getId(), resResult);
            List<GroupNoteWrapper.GroupNote> checkGroupNoteList = searchGroupNoteByContent(groupNote.getContent());
            Assert.assertTrue(checkGroupNoteList.isEmpty());
            break;
        }
    }

    @Test
    public void getGroupNote(){
        String id = "5da170683ee59e16e02c9f09";
        GroupNoteGetVO groupNoteGetVO = groupNoteService.getGroupNote(id);
        log.debug("groupNoteGetVO:{}", groupNoteGetVO);
        Assert.assertTrue(groupNoteGetVO.getId().equals(id));
    }

    /**
     * 根据内容查询数据
     *
     * @param content
     * @return
     */
    private List<GroupNoteWrapper.GroupNote> searchGroupNoteByContent(String content){
        GroupNoteSearchDTO groupNoteSearchDTO = GroupNoteSearchDTO.builder()
                .content(content)
                .build();
        GroupNoteSearchVO groupNoteSearchVO = groupNoteService.searchGroupNote(groupNoteSearchDTO);
        log.debug("groupNoteSearchVO:{}", groupNoteSearchVO);
        return groupNoteSearchVO.getGroupNotes();
    }
}
