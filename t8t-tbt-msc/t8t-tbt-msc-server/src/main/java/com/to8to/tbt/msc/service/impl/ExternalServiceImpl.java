package com.to8to.tbt.msc.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.constant.AppMsgConstant;
import com.to8to.tbt.msc.constant.HttpRequestConstant;
import com.to8to.tbt.msc.dto.FeignRequestDTO;
import com.to8to.tbt.msc.dto.OperateConfigGetDTO;
import com.to8to.tbt.msc.service.external.CompanyService;
import com.to8to.tbt.msc.service.external.OperateConfigService;
import com.to8to.tbt.msc.utils.IntegerUtils;
import com.to8to.tbt.msc.utils.LogUtils;
import com.to8to.tbt.msc.utils.RequestUtils;
import com.to8to.tbt.msc.vo.CompanyResultWrapper;
import com.to8to.tbt.msc.vo.OperateConfigGetVO;
import com.to8to.tbt.msc.vo.PermissionWrapper;
import com.to8to.tbt.msc.service.ExternalService;
import com.to8to.tbt.msc.service.external.CrmService;
import com.to8to.tbt.msc.service.external.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author juntao.guo
 */
@Slf4j
@Service
public class ExternalServiceImpl implements ExternalService {

    @Autowired
    private CrmService crmService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private OperateConfigService operateConfigService;

    @Override
    public int getIdByPhone(String phone) {
        int phoneId = 0;
        try {
            List<String> phoneIds = new ArrayList<>();
            phoneIds.add("\"" + phone + "\"");
            FeignRequestDTO args = FeignRequestDTO.builder()
                    .args(phoneIds)
                    .build();
            ResResult response = crmService.getIdByPhone(args);
            if (response.getStatus().equals(HttpRequestConstant.FEIGN_CLIENT_SUCCESS)) {
                if (response.getData() != null) {
                    phoneId = (int) response.getData();
                }
                log.debug(LogUtils.buildTemplate("args response"), args, response);
            } else {
                log.warn(LogUtils.buildTemplate("requestError:args response"), args, response);
            }
        } catch (Exception e) {
            log.warn(LogUtils.buildTemplate("phone"), phone, e);
        }
        return phoneId;
    }

    @Override
    public List<PermissionWrapper.CrmUserVO> userGetList(List<Integer> uids) {
        List<PermissionWrapper.CrmUserVO> userList = new ArrayList<>();
        if (uids.size() > 0) {
            com.to8to.tbt.msc.dto.PermissionWrapper.UserGetListDTO userGetListDTO = com.to8to.tbt.msc.dto.PermissionWrapper.UserGetListDTO.builder()
                    .uids(uids)
                    .fields("uid,nick")
                    .build();
            FeignRequestDTO args = FeignRequestDTO.builder()
                    .args(userGetListDTO)
                    .build();
            try {
                ResResult response = permissionService.userGetList(args);
                if (response.getStatus().equals(HttpRequestConstant.FEIGN_CLIENT_SUCCESS)) {
                    JSONArray data = JSONArray.parseArray(JSON.toJSONString(response.getData()));
                    userList = data.toJavaList(PermissionWrapper.CrmUserVO.class);
                } else {
                    log.warn(LogUtils.buildTemplate("requestError:args response"), args, response);
                }
                log.debug(LogUtils.buildTemplate("requestFail:userGetListDTO response userList"), args, response, userList);
            } catch (Exception e) {
                log.warn(LogUtils.buildTemplate("args"), args, e);
            }
        }
        return userList;
    }

    @Override
    public Map<Integer, PermissionWrapper.CrmUserVO> crmUserQueryMap(List<Integer> uidList) {
        int length = uidList.size();
        Map<Integer, PermissionWrapper.CrmUserVO> userGetListItemVOMap = new HashMap<>(length);
        List<PermissionWrapper.CrmUserVO> userGetListItemVOList = userGetList(uidList);
        for (PermissionWrapper.CrmUserVO userGetListItemVO : userGetListItemVOList) {
            userGetListItemVOMap.put(userGetListItemVO.getUid(), userGetListItemVO);
        }
        return userGetListItemVOMap;
    }

    @Override
    public Map<Integer, CompanyResultWrapper.Business> decInfoQueryList(List<Integer> accountIds) {
        Map<Integer, CompanyResultWrapper.Business> userMap = new HashMap<>(accountIds.size());
        if (accountIds.isEmpty()) {
            return userMap;
        }
        Map<Integer, Integer> decMemberIds = queryDecMemberIds(accountIds);
        if (decMemberIds.isEmpty()) {
            return userMap;
        }
        try {
            JSONObject search = new JSONObject();
            JSONArray decMemberIdList = new JSONArray();
            Iterator iterator = decMemberIds.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                decMemberIdList.add(entry.getKey());
            }
            search.put("id_in", decMemberIdList);
            ResResult<List<CompanyResultWrapper.Business>> resResult = companyService.decInfoQueryList(generateRequestBody(search));
            if (resResult.getStatus().equals(HttpRequestConstant.FEIGN_CLIENT_SUCCESS)) {
                for (CompanyResultWrapper.Business business : resResult.getData()) {
                    Integer accountId = decMemberIds.get(business.getId());
                    if (accountId != null) {
                        business.setAvatar(generateBusinessAvatar(business.getId()));
                        userMap.put(accountId, business);
                    }
                }
            } else {
                log.warn("decInfoQueryList error params:{} resResult:{}", search, resResult);
            }
            log.debug("decInfoQueryList params:{} resResult:{}", search, resResult);
        } catch (Exception e) {
            log.warn("decInfoQueryList exception accountIds:{} e:{}", accountIds, e);
        }
        return userMap;
    }

    @Override
    public OperateConfigGetVO getOpeConfigs(List<String> arguments) {
        OperateConfigGetDTO operateConfigGetDTO = new OperateConfigGetDTO();
        operateConfigGetDTO.setNames(arguments);
        ResResult<OperateConfigGetVO> resResult = operateConfigService.getConfigs(RequestUtils.buildFeignRequestBody(operateConfigGetDTO));
        if (IntegerUtils.isEqLimitValue(resResult.getStatus())) {
            log.debug("getOpeConfigs operateConfigGetDTO:{} resResult:{}", operateConfigGetDTO, resResult);
            return resResult.getData();
        } else {
            log.error("getOpeConfigs error operateConfigGetDTO:{} resResult:{}", operateConfigGetDTO, resResult);
            return null;
        }
    }

    /**
     * 查询装企子账号信息
     *
     * @param accountIds
     * @return
     */
    private Map<Integer, Integer> queryDecMemberIds(List<Integer> accountIds) {
        Map<Integer, Integer> decMemberIds = new HashMap<>(accountIds.size());
        if (accountIds.isEmpty()) {
            return decMemberIds;
        }
        try {
            JSONObject search = new JSONObject();
            JSONArray accountIdList = new JSONArray();
            for (Integer accountId : accountIds) {
                accountIdList.add(accountId);
            }
            search.put("accountId_in", accountIdList);
            ResResult<List<CompanyResultWrapper.DecMember>> resResult = companyService.decMemberQueryList(generateRequestBody(search));
            if (resResult.getStatus().equals(HttpRequestConstant.FEIGN_CLIENT_SUCCESS)) {
                for (CompanyResultWrapper.DecMember decMember : resResult.getData()) {
                    decMemberIds.put(decMember.getDecId(), decMember.getAccountId());
                }
            } else {
                log.warn("queryDecMemberId error params:{} resResult:{}", search, resResult);
            }
            log.debug("queryDecMemberId params:{} resResult:{}", search, resResult);
        } catch (Exception e) {
            log.warn("queryDecMemberId exception accountIds:{} e:{}", accountIds, e);
        }
        return decMemberIds;
    }

    /**
     * 生成装企头像
     *
     * @param accountId
     * @return
     */
    private String generateBusinessAvatar(int accountId) {
        return AppMsgConstant.HOST_PIC + "user/" + accountId % 100 + "/headphoto_" + accountId + ".jpg";
    }

    /**
     * 生成JSON请求体
     *
     * @param params
     * @return
     */
    private JSONObject generateRequestBody(JSONObject params) {
        JSONObject args = new JSONObject();
        args.put("search", params);
        return args;
    }
}
