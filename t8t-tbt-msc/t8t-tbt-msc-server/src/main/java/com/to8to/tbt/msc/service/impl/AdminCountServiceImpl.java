package com.to8to.tbt.msc.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.to8to.common.util.DozerUtils;
import com.to8to.tbt.msc.dto.*;
import com.to8to.tbt.msc.entity.MsgRecord;
import com.to8to.tbt.msc.entity.admin.CountTemplateSendItem;
import com.to8to.tbt.msc.entity.admin.CountTemplateSendTemplate;
import com.to8to.tbt.msc.entity.mongo.MongoMsgReply;
import com.to8to.tbt.msc.enumeration.MsgSendTypeEnum;
import com.to8to.tbt.msc.repository.es.EsMsgRecordRepository;
import com.to8to.tbt.msc.repository.mongo.MongoMsgReplyRepository;
import com.to8to.tbt.msc.repository.mongo.template.MsgRecordComplexRepository;
import com.to8to.tbt.msc.service.AdminCountService;
import com.to8to.tbt.msc.service.EncryptService;
import com.to8to.tbt.msc.service.MessageCenterService;
import com.to8to.tbt.msc.service.TemplateV2Service;
import com.to8to.tbt.msc.utils.IntegerUtils;
import com.to8to.tbt.msc.utils.LogUtils;
import com.to8to.tbt.msc.utils.TimeUtils;
import com.to8to.tbt.msc.vo.*;
import com.to8to.tbt.msc.vo.admin.CountTemplateSendVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author juntao.guo
 */
@Slf4j
@Service
public class AdminCountServiceImpl implements AdminCountService {

    @Autowired
    private EsMsgRecordRepository esMsgRecordRepository;

    @Autowired
    private TemplateV2Service templateV2Service;

    @Autowired
    private MongoMsgReplyRepository mongoMsgReplyRepository;

    @Autowired
    private EncryptService encryptService;

    @Autowired
    private MsgRecordComplexRepository msgRecordComplexRepository;

    @Autowired
    private MessageCenterService messageCenterService;

    @Override
    public CountTemplateSendVO countTemplateSend(SearchMessageRecordDTO searchMessageRecordDTO) {
        Map<String, CountTemplateSendItem> countTemplateSendItemMap = Maps.newHashMap();
        searchMessageRecordDTO.setSendType(IntegerUtils.intValueAsDefault(searchMessageRecordDTO.getSendType(), MsgSendTypeEnum.SMS.getCode()));
        if (MsgSendTypeEnum.SMS.equals(searchMessageRecordDTO.getSendType())){
            Map<String, Long> countMap = esMsgRecordRepository.countTemplateSend(searchMessageRecordDTO);
            for (Map.Entry<String, Long> entry : countMap.entrySet()) {
                //查询模板信息
                TemplateGetDTO templateGetDTO = TemplateGetDTO.builder()
                        .tid(Integer.valueOf(entry.getKey()))
                        .build();
                try {
                    TemplateGetVO<MessageTemplateVO> templateGetVO = templateV2Service.getTemplateByTid(templateGetDTO);
                    if (templateGetVO != null) {
                        CountTemplateSendItem countTemplateSendItem = CountTemplateSendItem.builder()
                                .result(templateGetVO.getResult())
                                .total(entry.getValue())
                                .build();
                        countTemplateSendItemMap.put(entry.getKey(), countTemplateSendItem);
                    }
                } catch (Exception e) {
                    log.warn(LogUtils.buildTemplate("searchMessageRecordDTO"), searchMessageRecordDTO, e);
                }
            }
        }
        return CountTemplateSendVO.builder()
                .templateCount(countTemplateSendItemMap)
                .build();
    }

    @Override
    public List<Map<String, Long>> countChannelType(SearchMessageRecordDTO searchMessageRecordDTO) {
        List<Map<String, Long>> countChannelTypeList = Lists.newArrayList();
        if (searchMessageRecordDTO.getStime() == null) {
            searchMessageRecordDTO.setStime(TimeUtils.generateNatureWeekStartTime());
        }
        Map<Integer, Integer> mongoCountChannelMap = msgRecordComplexRepository.countChannel(searchMessageRecordDTO);
        Map<String, Long> esCountChannelMap = esMsgRecordRepository.countChannelType(searchMessageRecordDTO);
        int maxChannel = 6;
        for (int channel = 1; channel <= maxChannel; channel++) {
            long count = 0;
            Integer mongoCount = mongoCountChannelMap.getOrDefault(channel, null);
            count += mongoCount != null ? mongoCount.longValue() : 0L;
            count += esCountChannelMap.getOrDefault(String.valueOf(channel), 0L);
            Map<String, Long> map = Maps.newHashMap();
            map.put(String.valueOf(channel), count);
            countChannelTypeList.add(map);
        }
        return countChannelTypeList;
    }

    @Override
    public List<NoteReplyVO> getNoteReply(NoteReplyGetDTO noteReplyGetDTO) {
        List<NoteReplyVO> noteReplyVOS = Lists.newArrayList();
        if (StringUtils.isBlank(noteReplyGetDTO.getPhoneId())) {
            return noteReplyVOS;
        }
        String phone;
        try {
            phone = encryptService.getPlainText(noteReplyGetDTO.getPhoneId());
            if (StringUtils.isEmpty(phone)) {
                return noteReplyVOS;
            }
        } catch (Exception e) {
            log.warn("[getNoteReply] getPlainText error phoneId:{} e:{}", noteReplyGetDTO.getPhoneId(), e);
            return noteReplyVOS;
        }
        List<MongoMsgReply> mongoMsgReplyList;
        try {
            mongoMsgReplyList = mongoMsgReplyRepository.findAllByPhone(phone);
        } catch (Exception e) {
            log.warn("getNoteReply findAllByPhone exception e:{}", e);
            return noteReplyVOS;
        }
        for (MongoMsgReply mongoMsgReply : mongoMsgReplyList) {
            NoteReplyVO noteReplyVO = NoteReplyVO.builder()
                    .sendTime(IntegerUtils.intValueAsDefault(mongoMsgReply.getReplyTime()))
                    .msgContent(StringUtils.defaultString(mongoMsgReply.getReply()))
                    .type(2)
                    .build();
            noteReplyVOS.add(noteReplyVO);
        }
        return noteReplyVOS;
    }

    @Override
    public Map<String, Long> countAppMessageRecord(SearchMessageRecordDTO searchMessageRecordDTO) {
        return esMsgRecordRepository.countAppMessageRecord(searchMessageRecordDTO);
    }

    @Override
    public List<NoteReplyVO> getNoteExHistory(NoteExHistoryDTO noteExHistoryDTO) {
        List<NoteReplyVO> noteReplyVOS = Lists.newArrayList();
        if (IntegerUtils.isEqLimitValue(noteExHistoryDTO.getPhoneId())) {
            return noteReplyVOS;
        }
        noteReplyVOS = esMsgRecordRepository.getNoteRecord(noteExHistoryDTO.getPhoneId());
        List<NoteReplyVO> smsReplyList = getNoteReply(NoteReplyGetDTO.builder()
                .phoneId(String.valueOf(noteExHistoryDTO.getPhoneId()))
                .build());
        noteReplyVOS.addAll(smsReplyList);
        String phone = encryptService.getPlainText(String.valueOf(noteExHistoryDTO.getPhoneId()));
        if (phone != null) {
            ListMsgDTO listMsgDTO = new ListMsgDTO();
            listMsgDTO.setTargetContact(phone);
            listMsgDTO.setSendType(MsgSendTypeEnum.SMS.getCode());
            ListMsgVO listMsgVO = messageCenterService.listMsg(listMsgDTO);
            List<MsgRecord> msgRecordList = listMsgVO.getMsgRecords();
            if (msgRecordList != null && !msgRecordList.isEmpty()) {
                for (MsgRecord msgRecord : msgRecordList) {
                    NoteReplyVO noteReplyVO = NoteReplyVO.builder()
                            .msgContent(StringUtils.defaultString(msgRecord.getContent()))
                            .sendTime((int) msgRecord.getSendTime())
                            .type(1)
                            .build();
                    noteReplyVOS.add(noteReplyVO);
                }
            }
        }
        return noteReplyVOS;
    }
}
