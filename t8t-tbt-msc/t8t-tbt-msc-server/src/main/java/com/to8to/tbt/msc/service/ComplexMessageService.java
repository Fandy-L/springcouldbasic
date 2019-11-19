package com.to8to.tbt.msc.service;

import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.dto.ListMsgDTO;
import com.to8to.tbt.msc.dto.SendAllMessageDTO;
import com.to8to.tbt.msc.dto.SendMsgDTO;
import com.to8to.tbt.msc.vo.ListMsgVO;
import com.to8to.tbt.msc.vo.MsgRecordVO;
import com.to8to.tbt.msc.vo.SendMsgVO;

/**
 * @author juntao.guo
 */
public interface ComplexMessageService {

    /**
     * 根据手机号码批量查询消息
     *
     * @param listMsgDTO
     * @return
     */
    ListMsgVO<MsgRecordVO> selectMsgByPhone(ListMsgDTO listMsgDTO);

    /**
     * 发送短信及APP消息
     *
     * @param sendAllMessageDTO
     * @return
     */
    ResResult sendAllMessage(SendAllMessageDTO sendAllMessageDTO);

    /**
     * 旧版综合消息发送
     *
     * @param sendMsgDTO
     * @return
     */
    SendMsgVO sendMsg(SendMsgDTO sendMsgDTO);
}
