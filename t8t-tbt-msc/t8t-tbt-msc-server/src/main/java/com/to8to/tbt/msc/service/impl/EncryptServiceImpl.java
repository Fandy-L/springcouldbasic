package com.to8to.tbt.msc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.dto.PhoneQueryDTO;
import com.to8to.tbt.msc.entity.CodecWrapper;
import com.to8to.tbt.msc.service.EncryptService;
import com.to8to.tbt.msc.service.external.CodecService;
import com.to8to.tbt.msc.utils.IntegerUtils;
import com.to8to.tbt.msc.utils.LogUtils;
import com.to8to.tbt.msc.utils.RequestUtils;
import com.to8to.tbt.msc.vo.PhoneQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author juntao.guo
 */
@Slf4j
@Service
public class EncryptServiceImpl implements EncryptService {

    @Value(value = "${com.codec.msgToken}")
    private String msgToken;

    @Autowired(required = false)
    private CodecService codecService;
    /**
     * 批量获取手机号的步进值
     */
    private static final int BATCH_GET_PHONE_MAP_STEP = 100;

    @Override
    public Map<String, String> getPhoneMap(Set<String> phoneIds) {
        Map<String, String> phoneMap = Maps.newHashMapWithExpectedSize(phoneIds.size());
        if (StringUtils.isBlank(msgToken)) {
            return phoneMap;
        }
        List<CodecWrapper.PhoneItem> phoneItemList = new ArrayList<>();
        for (String phoneId : phoneIds) {
            CodecWrapper.PhoneItem phoneItem = CodecWrapper.PhoneItem.builder()
                    .eid(phoneId)
                    .type(1)
                    .build();
            phoneItemList.add(phoneItem);
        }
        int listSize = phoneItemList.size();
        for (int i = 0; i < listSize; i += BATCH_GET_PHONE_MAP_STEP) {
            int endIndex = i + BATCH_GET_PHONE_MAP_STEP;
            List<CodecWrapper.PhoneItem> childPhoneItemList = phoneItemList.subList(i, Math.min(listSize, endIndex));
            PhoneQueryDTO phoneQueryDTO = PhoneQueryDTO.builder()
                    .phoneIdList(childPhoneItemList)
                    .token(msgToken)
                    .build();
            try {
                JSONObject args = RequestUtils.buildFeignRequestBody(phoneQueryDTO);
                ResResult<List<PhoneQueryVO>> resResult = codecService.getPhonesByIds(args);
                if (IntegerUtils.isEqLimitValue(resResult.getStatus())) {
                    List<PhoneQueryVO> phoneQueryVOS = resResult.getData();
                    for (PhoneQueryVO phoneQueryVO : phoneQueryVOS) {
                        phoneMap.put(String.valueOf(phoneQueryVO.getEid()), phoneQueryVO.getStr());
                    }
                } else {
                    log.warn(LogUtils.buildTemplate("args resResult"), args, resResult);
                }
                log.debug(LogUtils.buildTemplate("args resResult"), args, resResult);
            } catch (Exception e) {
                log.warn(LogUtils.buildExceptionTemplate("phoneIds"), phoneIds, e);
            }
        }
        return phoneMap;
    }

    @Override
    public String getPlainText(String phoneId) {
        Set<String> idList = new HashSet<>();
        idList.add(phoneId);
        List<String> phoneList = getPhoneList(idList);
        String phone = null;
        if (phoneList.size() > 0) {
            phone = phoneList.get(0);
        }
        return phone;
    }

    @Override
    public List<String> getPhoneList(Set<String> phoneIds) {
        List<String> phoneList = Lists.newArrayList();
        Map<String, String> phoneMap = getPhoneMap(phoneIds);
        for (String phoneId : phoneIds) {
            String phone = phoneMap.getOrDefault(phoneId, null);
            if (StringUtils.isNotBlank(phone)) {
                phoneList.add(phone);
            }
        }
        return phoneList;
    }
}
