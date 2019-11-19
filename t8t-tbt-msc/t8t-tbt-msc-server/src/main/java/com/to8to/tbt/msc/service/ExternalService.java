package com.to8to.tbt.msc.service;

import com.to8to.tbt.msc.vo.CompanyResultWrapper;
import com.to8to.tbt.msc.vo.OperateConfigGetVO;
import com.to8to.tbt.msc.vo.PermissionWrapper;

import java.util.List;
import java.util.Map;

/**
 * @author juntao.guo
 */
public interface ExternalService {

    /**
     * 解密手机号信息
     *
     * @param phone
     * @return
     */
    int getIdByPhone(String phone);

    /**
     * 根据ID查询用户信息
     *
     * @param uids
     * @return
     */
    List<PermissionWrapper.CrmUserVO> userGetList(List<Integer> uids);

    /**
     * 批量查询CRM用户信息
     *
     * @param uidList
     * @return
     */
    Map<Integer, PermissionWrapper.CrmUserVO> crmUserQueryMap(List<Integer> uidList);

    /**
     * 装企-查询用户信息
     *
     * @param accountIds
     * @return
     */
    Map<Integer, CompanyResultWrapper.Business> decInfoQueryList(List<Integer> accountIds);

    /**
     * 读取运营配置信息
     *
     * @param arguments
     * @return
     */
    OperateConfigGetVO getOpeConfigs(List<String> arguments);
}
