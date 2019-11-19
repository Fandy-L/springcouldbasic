package com.to8to.tbt.msc.controller;

import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;
import com.to8to.tbt.msc.dto.NoteExHistoryDTO;
import com.to8to.tbt.msc.dto.NoteReplyGetDTO;
import com.to8to.tbt.msc.dto.SearchMessageRecordDTO;
import com.to8to.tbt.msc.export.AdminCountApi;
import com.to8to.tbt.msc.service.AdminCountService;
import com.to8to.tbt.msc.vo.NoteReplyVO;
import com.to8to.tbt.msc.vo.admin.CountTemplateSendVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author juntao.guo
 */
@RestController
public class AdminCountController implements AdminCountApi {

    @Autowired
    private AdminCountService adminCountService;

    @Override
    public ResResult<CountTemplateSendVO> countTemplateSend(@RequestBody List<SearchMessageRecordDTO> params) {
        return ResUtils.data(adminCountService.countTemplateSend(params.get(0)));
    }

    @Override
    public ResResult<List<Map<String, Long>>> countChannelType(@RequestBody List<SearchMessageRecordDTO> params) {
        return ResUtils.data(adminCountService.countChannelType(params.get(0)));
    }

    @Override
    public ResResult<List<NoteReplyVO>> getNoteReply(@RequestBody List<NoteReplyGetDTO> params) {
        return ResUtils.data(adminCountService.getNoteReply(params.get(0)));
    }

    @Override
    public ResResult<Map<String, Long>> getAppMsgRecordCount(@RequestBody List<SearchMessageRecordDTO> params) {
        return ResUtils.data(adminCountService.countAppMessageRecord(params.get(0)));
    }

    @Override
    public ResResult<List<NoteReplyVO>> getNoteExHistory(@RequestBody List<NoteExHistoryDTO> params) {
        return ResUtils.data(adminCountService.getNoteExHistory(params.get(0)));
    }
}
