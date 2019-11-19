package com.to8to.tbt.msc.service;

import com.google.common.collect.Lists;
import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.dto.GroupNoteRecordSearchDTO;
import com.to8to.tbt.msc.dto.GroupNoteRecordUpdateDTO;
import com.to8to.tbt.msc.entity.GroupNoteRecordWrapper;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.entity.SearchTime;
import com.to8to.tbt.msc.entity.mongo.MongoGroupNoteRecord;
import com.to8to.tbt.msc.repository.mongo.GroupNoteRecordRepository;
import com.to8to.tbt.msc.vo.GroupNoteRecordSearchVO;
import com.to8to.tbt.msc.vo.ImportNoteGroupRecordDTO;
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
public class GroupNoteRecordServiceTest extends BaseApplication {

    @Autowired
    private GroupNoteRecordService groupNoteRecordService;

    @Autowired
    private GroupNoteRecordRepository groupNoteRecordRepository;

    @Test
    public void searchGroupNoteRecord() {
        int searchType = 1;
        String content = "2390";
        long startTime = 1569307696;
        long endTime = 1569307696;
        SearchTime searchTime = SearchTime.builder()
                .startTime(startTime)
                .endTime(endTime)
                .build();
        GroupNoteRecordSearchDTO groupNoteRecordSearchDTO = GroupNoteRecordSearchDTO.builder()
                .searchType(searchType)
                .content(content)
                .searchTime(searchTime)
                .build();
        GroupNoteRecordSearchVO groupNoteRecordSearchVO = groupNoteRecordService.searchGroupNoteRecord(groupNoteRecordSearchDTO);
        log.debug("groupNoteRecordSearchVO:{}", groupNoteRecordSearchVO);
        for (GroupNoteRecordWrapper.GroupNoteRecord groupNoteRecord : groupNoteRecordSearchVO.getRecords()) {
            Assert.assertTrue(groupNoteRecord.getPhone().indexOf(content) >= 0 && groupNoteRecord.getSendTime() >= startTime);
        }
    }

    @Test
    public void updateGroupNoteRecord() {
        String groupNoteId = "5d89bc10d5de2ed125019714";
        String name = "356678";
        String phone = "15824909673";
        List<GroupNoteRecordWrapper.GroupNoteRecord> groupNoteRecordList = searchGroupNoteRecordByGroupNoteId(groupNoteId);
        for (GroupNoteRecordWrapper.GroupNoteRecord groupNoteRecord : groupNoteRecordList) {
            GroupNoteRecordUpdateDTO groupNoteRecordUpdateDTO = GroupNoteRecordUpdateDTO.builder()
                    .id(groupNoteRecord.getId())
                    .name(name)
                    .phone(phone)
                    .build();
            ResResult<MsgCenterResponse> resResult = groupNoteRecordService.updateGroupNoteRecord(groupNoteRecordUpdateDTO);
            log.debug("id:{} resResult:{}", groupNoteRecord.getId(), resResult);
            List<GroupNoteRecordWrapper.GroupNoteRecord> checkGroupNoteRecordList = searchGroupNoteRecordByGroupNoteIdAndPhone(groupNoteId, phone);
            Assert.assertFalse(checkGroupNoteRecordList.isEmpty());
            break;
        }
    }

    @Test
    public void delGroupNoteRecord() {
        int status = 1;
        List<GroupNoteRecordWrapper.GroupNoteRecord> groupNoteRecordList = searchGroupNoteRecordByStatus(status);
        for (GroupNoteRecordWrapper.GroupNoteRecord groupNoteRecord : groupNoteRecordList) {
            List<String> idList = Lists.newArrayList();
            idList.add(groupNoteRecord.getId());
            ResResult<MsgCenterResponse> resResult = groupNoteRecordService.delGroupNoteRecord(idList);
            log.debug("groupNoteRecord:{} resResult:{}", groupNoteRecord, resResult);
            MongoGroupNoteRecord mongoGroupNoteRecord = groupNoteRecordRepository.findById(groupNoteRecord.getId()).orElse(null);
            Assert.assertTrue(mongoGroupNoteRecord == null);
            break;
        }
    }

    @Test
    public void importGroupNoteObject(){
        String phone = "13942001112";
        String groupNoteId = "54b60a76e4b0e6c1e11cbb8d";
        List<GroupNoteRecordWrapper.GroupNoteRecord> groupNoteRecordList = searchGroupNoteRecordByGroupNoteIdAndPhone(groupNoteId, phone);
        for (GroupNoteRecordWrapper.GroupNoteRecord groupNoteRecord : groupNoteRecordList){
            groupNoteRecordRepository.deleteById(groupNoteRecord.getId());
        }
        groupNoteRecordList = searchGroupNoteRecordByGroupNoteIdAndPhone(groupNoteId, phone);
        Assert.assertTrue(groupNoteRecordList.isEmpty());
        List<ImportNoteGroupRecordDTO> importNoteGroupRecordDTOList = Lists.newArrayList();
        ImportNoteGroupRecordDTO importNoteGroupRecordDTO = ImportNoteGroupRecordDTO.builder()
                .name("郭飞宇")
                .phoneId("8653691")
                .build();
        importNoteGroupRecordDTOList.add(importNoteGroupRecordDTO);
        ResResult<MsgCenterResponse> resResult = groupNoteRecordService.importGroupNoteObject(groupNoteId, importNoteGroupRecordDTOList);
        log.debug("resResult:{}", resResult);
        List<GroupNoteRecordWrapper.GroupNoteRecord> checkGroupNoteRecordList = searchGroupNoteRecordByGroupNoteIdAndPhone(groupNoteId, phone);
        Assert.assertFalse(checkGroupNoteRecordList.isEmpty());
    }



    /**
     * 根据模板ID查询短信记录
     *
     * @param groupNoteId
     * @return
     */
    private List<GroupNoteRecordWrapper.GroupNoteRecord> searchGroupNoteRecordByGroupNoteId(String groupNoteId) {
        int sendStatus = 1;
        GroupNoteRecordSearchDTO groupNoteRecordSearchDTO = GroupNoteRecordSearchDTO.builder()
                .groupNoteId(groupNoteId)
                .sendStatus(sendStatus)
                .build();
        return searchGroupNoteRecordByDTO(groupNoteRecordSearchDTO);
    }

    /**
     * 根据模板ID和手机号查询查询短信记录
     *
     * @param groupNoteId
     * @return
     */
    private List<GroupNoteRecordWrapper.GroupNoteRecord> searchGroupNoteRecordByGroupNoteIdAndPhone(String groupNoteId, String phone) {
        GroupNoteRecordSearchDTO groupNoteRecordSearchDTO = GroupNoteRecordSearchDTO.builder()
                .searchType(1)
                .groupNoteId(groupNoteId)
                .content(phone)
                .build();
        return searchGroupNoteRecordByDTO(groupNoteRecordSearchDTO);
    }

    /**
     * 根据发送状态查询短信记录
     *
     * @param status
     * @return
     */
    private List<GroupNoteRecordWrapper.GroupNoteRecord> searchGroupNoteRecordByStatus(int status) {
        GroupNoteRecordSearchDTO groupNoteRecordSearchDTO = GroupNoteRecordSearchDTO.builder()
                .sendStatus(status)
                .build();
        return searchGroupNoteRecordByDTO(groupNoteRecordSearchDTO);
    }

    /**
     * 根据条件搜索
     *
     * @param groupNoteRecordSearchDTO
     * @return
     */
    private List<GroupNoteRecordWrapper.GroupNoteRecord> searchGroupNoteRecordByDTO(GroupNoteRecordSearchDTO groupNoteRecordSearchDTO) {
        GroupNoteRecordSearchVO groupNoteRecordSearchVO = groupNoteRecordService.searchGroupNoteRecord(groupNoteRecordSearchDTO);
        log.debug("searchGroupNoteRecordByDTO:{}", groupNoteRecordSearchVO);
        return groupNoteRecordSearchVO.getRecords();
    }
}
