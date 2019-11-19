package com.to8to.tbt.msc.controller;

import com.alibaba.fastjson.JSONArray;
import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;
import com.to8to.tbt.msc.dto.GroupNoteRecordSearchDTO;
import com.to8to.tbt.msc.dto.GroupNoteRecordUpdateDTO;
import com.to8to.tbt.msc.entity.GroupNoteRecordWrapper;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.export.GroupNoteRecordApi;
import com.to8to.tbt.msc.service.GroupNoteRecordService;
import com.to8to.tbt.msc.vo.GroupNoteRecordSearchVO;
import com.to8to.tbt.msc.vo.ImportNoteGroupRecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author juntao.guo
 */
@RestController
public class GroupNoteRecordController implements GroupNoteRecordApi {

    @Autowired
    private GroupNoteRecordService groupNoteRecordService;

    @Override
    public ResResult delGroupNoteRecord(@RequestBody List<List<String>> params) {
        return groupNoteRecordService.delGroupNoteRecord(params.get(0));
    }

    @Override
    public ResResult updateGroupNoteRecord(@RequestBody List<GroupNoteRecordUpdateDTO> params) {
        return groupNoteRecordService.updateGroupNoteRecord(params.get(0));
    }

    @Override
    public ResResult<GroupNoteRecordSearchVO> searchGroupNoteRecord(@RequestBody List<GroupNoteRecordSearchDTO> params) {
        return ResUtils.data(groupNoteRecordService.searchGroupNoteRecord(params.get(0)));
    }

    @Override
    public ResResult<List<GroupNoteRecordWrapper.GroupNoteRecord>> exportGroupNoteRecord(@RequestBody List<GroupNoteRecordSearchDTO> params) {
        return ResUtils.data(groupNoteRecordService.exportGroupNoteRecord(params.get(0)));
    }

    @Override
    public ResResult importGroupNoteObject(@RequestBody JSONArray params) {
        return groupNoteRecordService.importGroupNoteObject(params.getString(0), params.getJSONArray(1).toJavaList(ImportNoteGroupRecordDTO.class));
    }

    @Override
    public ResResult<MsgCenterResponse> groupSendNote(@RequestBody List<String> params) {
        return groupNoteRecordService.groupSendNote(params.get(0), params.get(1));
    }

    @Override
    public ResResult<MsgCenterResponse> groupReSendNote(@RequestBody JSONArray params) {
        return groupNoteRecordService.groupReSendNote(params.getString(0), params.getJSONArray(1).toJavaList(String.class));
    }
}
