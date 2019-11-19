package com.to8to.tbt.msc.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author juntao.guo
 */
public interface EncryptService {

    /**
     * 根据ID批量获取手机号
     *
     * @param phoneIds
     * @return
     */
    Map<String, String> getPhoneMap(Set<String> phoneIds);

    /**
     * 根据ID获取手机号
     *
     * @param phoneId
     * @return
     */
    String getPlainText(String phoneId);

    /**
     * 从HashMap中取出所有手机号
     *
     * @param phoneIds
     * @return
     */
    List<String> getPhoneList(Set<String> phoneIds);
}
