package com.to8to.tbt.msc.service;

import com.to8to.tbt.msc.dto.RedirectAppMsgDTO;

/**
 * @author pajero.quan
 */
public interface PushService {
//    /**
//     * @param jsonObject
//     * @return
//     */
//    String send(JSONObject jsonObject);

    /**
     * @param params
     * @return
     */
    String send(RedirectAppMsgDTO params);
}
