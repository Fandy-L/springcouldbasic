package com.to8to.tbt.msc.service.impl;

import com.to8to.common.search.PageResult;
import com.to8to.common.util.DateUtils;
import com.to8to.common.util.DozerUtils;
import com.to8to.tbt.msc.dto.CreateMessageDTO;
import com.to8to.tbt.msc.entity.dto.GetMessageListDTO;
import com.to8to.tbt.msc.entity.mysql.push.DeviceToken;
import com.to8to.tbt.msc.entity.mysql.push.Message;
import com.to8to.tbt.msc.entity.vo.MessageVO;
import com.to8to.tbt.msc.enumeration.BooleanEnum;
import com.to8to.tbt.msc.repository.mysql.push.PushMessageRepository;
import com.to8to.tbt.msc.service.DeviceTokenService;
import com.to8to.tbt.msc.service.PushMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @description 消息记录服务
 * @author: pajero.quan
 * @date: 2019/4/24 21:22
 */
@Slf4j
@Service
public class PushPushMessageServiceImpl implements PushMessageService {
    private static final int SECONDS_OF_MONTH = 3600 * 24 * 30;

    @Autowired
    private PushMessageRepository pushMessageRepository;

    @Autowired
    private DeviceTokenService deviceTokenService;

    @Override
    public void createMessage(CreateMessageDTO createMessageDTO) {
        Message message = DozerUtils.map(createMessageDTO, Message.class);
        message.setPayload(createMessageDTO.getPayload());
        message.setDeleted(BooleanEnum.FALSE.getValue());
        message.setCreateTime(DateUtils.currentSeconds());
        message.setUpdateTime(DateUtils.currentSeconds());
        pushMessageRepository.save(message);
    }

    @Override
    public PageResult<MessageVO> getMessageList(GetMessageListDTO query) {
        Page<Message> messagePage = pushMessageRepository.findAll((Specification<Message>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (query.getPushAppId() != null) {
                Predicate predicate = criteriaBuilder.equal(root.get("pushAppId"), query.getPushAppId());
                predicates.add(predicate);
            }

            if (!StringUtils.isEmpty(query.getT8tMsgId())) {
                Predicate predicate = criteriaBuilder.equal(root.get("t8tMsgId"), query.getT8tMsgId());
                predicates.add(predicate);
            }

            if (query.getChannelMsgId() != null) {
                Predicate predicate = criteriaBuilder.equal(root.get("channelMsgId"), query.getChannelMsgId());
                predicates.add(predicate);
            }

            if (query.getUid() != null) {
                Predicate predicate = criteriaBuilder.equal(root.get("uid"), query.getUid());
                predicates.add(predicate);
            }

            if (!StringUtils.isEmpty(query.getDeviceToken())) {
                Predicate predicate = criteriaBuilder.equal(root.get("deviceToken"), query.getDeviceToken());
                predicates.add(predicate);
            }

            if (query.getChannel() != null) {
                Predicate predicate = criteriaBuilder.equal(root.get("channel"), query.getChannel());
                predicates.add(predicate);
            }

            if (query.getPlatform() != null) {
                Predicate predicate = criteriaBuilder.equal(root.get("platform"), query.getPlatform());
                predicates.add(predicate);
            }

            if (query.getStatus() != null) {
                Predicate predicate = criteriaBuilder.equal(root.get("status"), query.getStatus());
                predicates.add(predicate);
            }

            if (query.getFromTime() != null) {
                Predicate predicate = criteriaBuilder.greaterThanOrEqualTo(root.get("createTime"), query.getFromTime());
                predicates.add(predicate);
            }

            if (query.getEndTime() != null) {
                Predicate predicate = criteriaBuilder.lessThanOrEqualTo(root.get("createTime"), query.getEndTime());
                predicates.add(predicate);
            }

            if (!StringUtils.isEmpty(query.getFirstId())) {
                DeviceToken deviceToken = deviceTokenService.queryDeviceTokenByFirstId(query.getPushAppId(), null, null, query.getFirstId());
                Predicate predicate = criteriaBuilder.equal(root.get("deviceToken"), deviceToken.getDeviceToken());
                predicates.add(predicate);
            }
            if (predicates.isEmpty()) {
                return null;
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }, PageRequest.of(query.getPage() - 1, query.getSize(), Sort.by(Sort.Direction.DESC, "createTime")));
        PageResult<MessageVO> messageVOPage = new PageResult<>();
        messageVOPage.setTotal(messagePage.getTotalElements());
        List<MessageVO> rows = new ArrayList<>();
        for (Message message : messagePage.getContent()) {
            MessageVO messageVO = DozerUtils.map(message, MessageVO.class);
            messageVO.setPayload(new String(message.getPayload()));
            rows.add(messageVO);
        }
        messageVOPage.setRows(rows);
        return messageVOPage;
    }

    @Override
    public void cleanMessages(int duration) {
        if (duration <= 0) {
            duration = SECONDS_OF_MONTH;
        }
        log.info("清理消息记录!duration={}", duration);
        pushMessageRepository.deleteAllByCreateTimeLessThanEqual(DateUtils.currentSeconds() - duration);
    }
}
