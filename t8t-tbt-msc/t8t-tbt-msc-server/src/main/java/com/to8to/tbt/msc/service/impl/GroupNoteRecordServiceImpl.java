package com.to8to.tbt.msc.service.impl;

import com.google.common.collect.Lists;
import com.to8to.common.executors.SafeRunnable;
import com.to8to.common.search.PageResult;
import com.to8to.common.util.DozerUtils;
import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.common.MyExceptionStatus;
import com.to8to.tbt.msc.configuration.MsgCenterConfiguration;
import com.to8to.tbt.msc.configuration.OperateConfigConfiguration;
import com.to8to.tbt.msc.constant.MsgConstant;
import com.to8to.tbt.msc.dto.GroupNoteRecordSearchDTO;
import com.to8to.tbt.msc.dto.GroupNoteRecordUpdateDTO;
import com.to8to.tbt.msc.entity.GroupNoteRecordWrapper;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.entity.mongo.MongoGroupNote;
import com.to8to.tbt.msc.entity.mongo.MongoGroupNoteRecord;
import com.to8to.tbt.msc.entity.response.StatusResultResponse;
import com.to8to.tbt.msc.repository.mongo.GroupNoteRecordRepository;
import com.to8to.tbt.msc.repository.mongo.GroupNoteRepository;
import com.to8to.tbt.msc.repository.mongo.template.GroupNoteRecordComplexRepository;
import com.to8to.tbt.msc.service.*;
import com.to8to.tbt.msc.utils.RequestUtils;
import com.to8to.tbt.msc.utils.ResponseUtils;
import com.to8to.tbt.msc.utils.TimeUtils;
import com.to8to.tbt.msc.vo.GroupNoteRecordSearchVO;
import com.to8to.tbt.msc.vo.ImportNoteGroupRecordDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;

/**
 * @author juntao.guo
 */
@Slf4j
@Service
public class GroupNoteRecordServiceImpl implements GroupNoteRecordService {

    @Autowired
    private GroupNoteRecordRepository groupNoteRecordRepository;

    @Autowired
    private GroupNoteRecordComplexRepository groupNoteRecordComplexRepository;

    @Autowired
    private GroupNoteRepository groupNoteRepository;

    @Autowired
    private EncryptService encryptService;

    @Autowired
    private AsyncTaskService asyncTaskService;

    @Autowired
    private SmsMsgService smsMsgService;

    @Autowired
    private GroupNoteService groupNoteService;

    @Autowired
    private GroupUserSendRecordService groupUserSendRecordService;

    @Override
    public ResResult<MsgCenterResponse> delGroupNoteRecord(List<String> idList) {
        try {
            for (String id : idList) {
                groupNoteRecordRepository.deleteById(id);
            }
        } catch (Exception e) {
            log.warn("delGroupNoteRecord exception idList:{} e:{}", idList, e);
            return ResponseUtils.fail();
        }
        return ResponseUtils.success();
    }

    @Override
    public ResResult<MsgCenterResponse> updateGroupNoteRecord(GroupNoteRecordUpdateDTO groupNoteRecordUpdateDTO) {
        try {
            MongoGroupNoteRecord mongoGroupNoteRecord = groupNoteRecordRepository.findById(groupNoteRecordUpdateDTO.getId()).orElse(null);
            if (mongoGroupNoteRecord != null) {
                mongoGroupNoteRecord.setName(groupNoteRecordUpdateDTO.getName());
                mongoGroupNoteRecord.setPhone(groupNoteRecordUpdateDTO.getPhone());
                groupNoteRecordRepository.save(mongoGroupNoteRecord);
            } else {
                log.warn("updateGroupNoteRecord not found groupNoteRecordUpdateDTO:{}", groupNoteRecordUpdateDTO);
                return ResponseUtils.fail();
            }
        } catch (Exception e) {
            log.warn("updateGroupNoteRecord exception e:{}", e);
            return ResponseUtils.fail();
        }
        return ResponseUtils.success();
    }

    @Override
    public GroupNoteRecordSearchVO searchGroupNoteRecord(GroupNoteRecordSearchDTO groupNoteRecordSearchDTO) {
        PageResult<GroupNoteRecordWrapper.GroupNoteRecord> pageResult = queryGroupNoteRecordPage(groupNoteRecordSearchDTO);
        return GroupNoteRecordSearchVO.builder()
                .records(pageResult.getRows())
                .totalPages(ResponseUtils.calculateTotalPages(pageResult.getTotal(), groupNoteRecordSearchDTO.getPageInfo().getPageSize()))
                .totalRecords(pageResult.getTotal())
                .build();
    }

    @Override
    public List<GroupNoteRecordWrapper.GroupNoteRecord> exportGroupNoteRecord(GroupNoteRecordSearchDTO groupNoteRecordSearchDTO) {
        PageResult<GroupNoteRecordWrapper.GroupNoteRecord> pageResult = queryGroupNoteRecordPage(groupNoteRecordSearchDTO);
        return pageResult.getRows();
    }

    @Override
    public ResResult<MsgCenterResponse> importGroupNoteObject(String groupNoteId, List<ImportNoteGroupRecordDTO> importNoteGroupRecordDTOList) {
        if (StringUtils.isEmpty(groupNoteId) || importNoteGroupRecordDTOList == null || importNoteGroupRecordDTOList.isEmpty()) {
            return ResponseUtils.fail();
        }
        log.info("[importGroupNoteObject] 开始导入短信群组:{},总数：{}", groupNoteId, importNoteGroupRecordDTOList.size());
        importGroupNotePhoneIdHandler(importNoteGroupRecordDTOList);
        List<MongoGroupNoteRecord> mongoGroupNoteRecordList = importGroupNoteRepeatRecordFilter(groupNoteId, importNoteGroupRecordDTOList);
        ResResult<MsgCenterResponse> resResult = importGroupNoteRepeatRecordSave(mongoGroupNoteRecordList);
        log.info("[importGroupNoteObject] 完成短信群组:{},总数：{}，成功：{},重复或不合法：{}", groupNoteId, importNoteGroupRecordDTOList.size(), mongoGroupNoteRecordList.size(), importNoteGroupRecordDTOList.size() - mongoGroupNoteRecordList.size());
        return ResponseUtils.isSuccess(resResult) ? ResponseUtils.centerResponseSuccess(String.format("总数：%d 条 | 成功导入：%d 条 | 重复或不合法：%d 条！", importNoteGroupRecordDTOList.size(), mongoGroupNoteRecordList.size(), importNoteGroupRecordDTOList.size() - mongoGroupNoteRecordList.size())) : resResult;
    }

    @Override
    public ResResult<MsgCenterResponse> groupSendNote(String groupNoteId, String userName) {
        log.info("[groupSendNote] groupNoteId:{} userName:{}", groupNoteId, userName);
        //用户日与周短信发送量限制
        List<MongoGroupNoteRecord> mongoGroupNoteRecordList = groupNoteRecordRepository.findAllByGroupNoteIdAndStatus(groupNoteId, 0);
        if (!OperateConfigConfiguration.GROUP_SEND_NOTE_NO_LIMIT_USER.contains(userName)) {
            int dayCount = groupUserSendRecordService.countGroupUserSendRecordByUser(userName, TimeUtils.getTimesDayMorning(), TimeUtils.getTimesDayNight());
            int weekCount = groupUserSendRecordService.countGroupUserSendRecordByUser(userName, TimeUtils.getTimesWeekMorning(), TimeUtils.getTimesWeekNight());

            if (dayCount + mongoGroupNoteRecordList.size() > OperateConfigConfiguration.DAY_COUNT_MAX) {
                int overSize = dayCount + mongoGroupNoteRecordList.size() - OperateConfigConfiguration.DAY_COUNT_MAX;
                return ResponseUtils.centerResponseFail(String.format("本次将群发短信%s条,本日您已成功群发短信:%s条,本次发送将超出数量限制%s条", mongoGroupNoteRecordList.size(), dayCount, overSize));
            }
            if (weekCount + mongoGroupNoteRecordList.size() > OperateConfigConfiguration.WEEK_COUNT_MAX) {
                int overSize = weekCount + mongoGroupNoteRecordList.size() - OperateConfigConfiguration.WEEK_COUNT_MAX;
                return ResponseUtils.centerResponseFail(String.format("本次将群发短信%s条,本周您已成功群发短信:%s条,本次发送将超出数量限制%s条", mongoGroupNoteRecordList.size(), weekCount, overSize));
            }
        }
        asyncTaskService.submit(new SafeRunnable(() -> sendGroupNote(groupNoteId, mongoGroupNoteRecordList, userName)));
        return ResponseUtils.centerResponseSuccess(MyExceptionStatus.OLD_MSG_CENTER_RESPONSE_GROUP_NOTE_SEND_SUCCESS.getCode(), MyExceptionStatus.OLD_MSG_CENTER_RESPONSE_GROUP_NOTE_SEND_SUCCESS.getMessage());
    }

    @Override
    public ResResult<MsgCenterResponse> groupReSendNote(String groupNoteId, List<String> ids) {
        List<MongoGroupNoteRecord> mongoGroupNoteRecordList = groupNoteRecordRepository.findAllByIdIn(ids);
        asyncTaskService.submit(new SafeRunnable(() -> sendGroupNote(groupNoteId, mongoGroupNoteRecordList, null)));
        return ResponseUtils.centerResponseSuccess(MyExceptionStatus.OLD_MSG_CENTER_RESPONSE_GROUP_NOTE_SEND_SUCCESS.getCode(), MyExceptionStatus.OLD_MSG_CENTER_RESPONSE_GROUP_NOTE_SEND_SUCCESS.getMessage());
    }

    /**
     * 发送群发短信
     *
     * @param groupNoteId
     * @param groupNoteRecordList
     * @param userName
     */
    private void sendGroupNote(String groupNoteId, List<MongoGroupNoteRecord> groupNoteRecordList, String userName) {
        log.info("sendGroupNote userName:{} groupNoteId:{} size:{}", userName, groupNoteId, groupNoteRecordList.size());
        MongoGroupNote mongoGroupNote = groupNoteRepository.findById(groupNoteId).orElse(null);
        if (mongoGroupNote == null || StringUtils.isBlank(mongoGroupNote.getContent())) {
            return;
        }
        log.info("sendGroupNote content:{}", mongoGroupNote.getContent());
        int succNum = 0;
        int failNum = 0;
        for (MongoGroupNoteRecord mongoGroupNoteRecord : groupNoteRecordList) {
            StatusResultResponse<String> statusResultResponse = smsMsgService.sendSmsAndSave(0, mongoGroupNoteRecord.getPhone(), null, mongoGroupNote.getContent(), MsgCenterConfiguration.MWYX_TO8TO, true);
            boolean flag = statusResultResponse.getStatus() == MyExceptionStatus.SEND_ALL_MESSAGE_SUCCESS.getCode();
            if (flag) {
                succNum++;
            } else {
                failNum++;
            }
            updateGroupNoteRecordStatusById(mongoGroupNoteRecord.getId(), flag ? 1 : 2);
        }
        if (StringUtils.isNotBlank(userName)) {
            groupUserSendRecordService.insertGroupUserSendRecord(groupNoteId, userName, succNum);
        }
        groupNoteService.updateGroupNoteSendNumById(mongoGroupNote, succNum, failNum);
    }

    /**
     * 更新群发短信记录的发送状态
     *
     * @param id
     * @param status
     */
    public void updateGroupNoteRecordStatusById(String id, int status) {
        try {
            MongoGroupNoteRecord mongoGroupNoteRecord = groupNoteRecordRepository.findById(id).orElse(null);
            if (mongoGroupNoteRecord != null) {
                mongoGroupNoteRecord.setStatus(status);
                mongoGroupNoteRecord.setSendTime(TimeUtils.getCurrentTimestamp());
                groupNoteRecordRepository.save(mongoGroupNoteRecord);
            }
        } catch (Exception e) {
            log.warn("updateGroupNoteRecordStatusById mysql error id:{} status:{} e:{}", id, status, e);
        }
    }

    /**
     * 导入数据补充手机号
     *
     * @param importNoteGroupRecordDTOList
     */
    protected void importGroupNotePhoneIdHandler(List<ImportNoteGroupRecordDTO> importNoteGroupRecordDTOList) {
        Set<String> phoneIdSet = new HashSet<>();
        for (ImportNoteGroupRecordDTO importNoteGroupRecordDTO : importNoteGroupRecordDTOList) {
            if (StringUtils.isNotBlank(importNoteGroupRecordDTO.getPhoneId())) {
                if (StringUtils.isBlank(importNoteGroupRecordDTO.getPhone())) {
                    phoneIdSet.add(importNoteGroupRecordDTO.getPhoneId());
                } else {
                    importNoteGroupRecordDTO.setPhoneId(null);
                }
            }
        }
        long startTime = System.currentTimeMillis();
        Map<String, String> phoneMap = encryptService.getPhoneMap(phoneIdSet);
        long endTime = System.currentTimeMillis();
        for (ImportNoteGroupRecordDTO importNoteGroupRecordDTO : importNoteGroupRecordDTOList) {
            if (StringUtils.isBlank(importNoteGroupRecordDTO.getPhone()) && StringUtils.isNotBlank(importNoteGroupRecordDTO.getPhoneId())) {
                String phone = phoneMap.getOrDefault(importNoteGroupRecordDTO.getPhoneId(), null);
                if (phone != null) {
                    importNoteGroupRecordDTO.setPhone(phone);
                } else {
                    log.warn("importGroupNotePhoneIdHandler getPhone fail groupNoteRecord:{}", importNoteGroupRecordDTO);
                }
            }
        }
        log.info("importGroupNotePhoneIdHandler 总数：{} 待解密：{} 已解密：{} 耗时：{}", importNoteGroupRecordDTOList.size(), phoneIdSet.size(), phoneMap.size(), endTime - startTime);
    }

    /**
     * 根据模板ID过滤掉已经存在的记录
     *
     * @param groupNoteId
     * @param importNoteGroupRecordDTOList
     * @return
     */
    protected List<MongoGroupNoteRecord> importGroupNoteRepeatRecordFilter(String groupNoteId, List<ImportNoteGroupRecordDTO> importNoteGroupRecordDTOList) {
        List<MongoGroupNoteRecord> mongoGroupNoteRecordList = Lists.newArrayList();
        Set<String> phoneList = queryPhoneListByGroupNoteId(groupNoteId);
        List<ImportNoteGroupRecordDTO> repeatImportGroupNoteRecordList = Lists.newArrayList();
        for (ImportNoteGroupRecordDTO importNoteGroupRecordDTO : importNoteGroupRecordDTOList) {
            if (StringUtils.isBlank(importNoteGroupRecordDTO.getPhone())) {
                continue;
            }
            Matcher matcher = MsgConstant.PHONE_VALIDATION_REGEX.matcher(importNoteGroupRecordDTO.getPhone());
            boolean legalCheck = matcher.matches();
            boolean saveFlag = phoneList.add(importNoteGroupRecordDTO.getPhone()) && legalCheck;
            if (saveFlag) {
                MongoGroupNoteRecord mongoGroupNoteRecord = DozerUtils.map(importNoteGroupRecordDTO, MongoGroupNoteRecord.class);
                mongoGroupNoteRecord.setGroupNoteId(groupNoteId);
                mongoGroupNoteRecord.setStatus(0);
                mongoGroupNoteRecord.setSendTime(0);
                mongoGroupNoteRecord.setInsertTime(TimeUtils.getCurrentTimestampLong());
                mongoGroupNoteRecordList.add(mongoGroupNoteRecord);
            } else {
                repeatImportGroupNoteRecordList.add(importNoteGroupRecordDTO);
            }
        }
        log.info("[importGroupNoteRepeatRecordFilter] 群发送对象：{}，数据库：{}，总共需导入：{}，判重后：{} ，重复：{} repeatGroupNoteRecordList:{}", groupNoteId, phoneList.size(), importNoteGroupRecordDTOList.size(), mongoGroupNoteRecordList.size(), repeatImportGroupNoteRecordList.size(), repeatImportGroupNoteRecordList);
        return mongoGroupNoteRecordList;
    }

    /**
     * 批量保存短信记录
     *
     * @param mongoGroupNoteRecordList
     * @return
     */
    private ResResult<MsgCenterResponse> importGroupNoteRepeatRecordSave(List<MongoGroupNoteRecord> mongoGroupNoteRecordList) {
        if (mongoGroupNoteRecordList == null || mongoGroupNoteRecordList.isEmpty()) {
            return ResponseUtils.fail();
        }
        int step = 1000;
        int total = mongoGroupNoteRecordList.size();
        try {
            for (int i = 0; i < total; i += step) {
                int endIndex = (i + step) < total ? (i + step) : total;
                groupNoteRecordRepository.saveAll(mongoGroupNoteRecordList.subList(i, endIndex));
            }
        } catch (Exception e) {
            log.warn("[importGroupNoteRepeatRecordSave] mysql save error mongoGroupNoteRecordList:{} e:{}", mongoGroupNoteRecordList, e);
            return ResponseUtils.fail();
        }
        return ResponseUtils.success();
    }

    /**
     * 根据模板ID查询已发送的手机号
     *
     * @param groupNoteId
     * @return
     */
    private Set<String> queryPhoneListByGroupNoteId(String groupNoteId) {
        Set<String> list = new HashSet<>();
        try {
            List<MongoGroupNoteRecord> mongoGroupNoteRecordList = groupNoteRecordRepository.findAllByGroupNoteId(groupNoteId);
            for (MongoGroupNoteRecord mongoGroupNoteRecord : mongoGroupNoteRecordList) {
                list.add(mongoGroupNoteRecord.getPhone());
            }
        } catch (Exception e) {
            log.info("[queryPhoneListByGroupNoteId] mongoDB 群发对象查询失败：{},{}", groupNoteId, e);
        }
        return list;
    }

    /**
     * 查询分页信息
     *
     * @param groupNoteRecordSearchDTO
     * @return
     */
    protected PageResult queryGroupNoteRecordPage(GroupNoteRecordSearchDTO groupNoteRecordSearchDTO) {
        groupNoteRecordSearchDTO.setPageInfo(RequestUtils.filterPageInfo(groupNoteRecordSearchDTO.getPageInfo()));
        PageResult<MongoGroupNoteRecord> pageResult;
        try {
            pageResult = groupNoteRecordComplexRepository.searchGroupNoteRecord(groupNoteRecordSearchDTO);
        } catch (Exception e) {
            log.warn("queryGroupNoteRecordPage exception groupNoteRecordSearchDTO:{} e:{}", groupNoteRecordSearchDTO, e);
            return ResponseUtils.buildPageResult();
        }
        List<GroupNoteRecordWrapper.GroupNoteRecord> groupNoteRecordList = new ArrayList<>();
        for (MongoGroupNoteRecord mongoGroupNoteRecord : pageResult.getRows()) {
            GroupNoteRecordWrapper.GroupNoteRecord groupNoteRecordVO = DozerUtils.map(mongoGroupNoteRecord, GroupNoteRecordWrapper.GroupNoteRecord.class);
            groupNoteRecordVO.setAttachedId(mongoGroupNoteRecord.getId());
            groupNoteRecordList.add(groupNoteRecordVO);
        }
        PageResult<GroupNoteRecordWrapper.GroupNoteRecord> groupNoteRecordPageResult = new PageResult<>();
        groupNoteRecordPageResult.setTotal(pageResult.getTotal());
        groupNoteRecordPageResult.setRows(groupNoteRecordList);
        return groupNoteRecordPageResult;
    }
}
