package com.to8to.tbt.msc.controller;

import com.to8to.sc.response.ResResult;
import com.to8to.sc.response.ResUtils;
import com.to8to.tbt.msc.dto.GroupNoteCreateDTO;
import com.to8to.tbt.msc.dto.GroupNoteSearchDTO;
import com.to8to.tbt.msc.export.GroupNoteApi;
import com.to8to.tbt.msc.service.GroupNoteService;
import com.to8to.tbt.msc.vo.GroupNoteGetVO;
import com.to8to.tbt.msc.vo.GroupNoteSearchVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author juntao.guo
 */
@RestController
public class GroupNoteController implements GroupNoteApi {

    @Autowired
    private GroupNoteService groupNoteService;

    @Override
    public ResResult addGroupNote(@RequestBody List<GroupNoteCreateDTO> params) {
        return groupNoteService.addGroupNote(params.get(0));
    }

    @Override
    public ResResult delGroupNote(@RequestBody List<String> params) {
        return groupNoteService.delGroupNote(params.get(0));
    }

    @Override
    public ResResult updateGroupNote(@RequestBody List<String> params) {
        return groupNoteService.updateGroupNote(params.get(0), params.get(1));
    }

    @Override
    public ResResult<GroupNoteSearchVO> searchGroupNote(@RequestBody List<GroupNoteSearchDTO> params) {
        return ResUtils.data(groupNoteService.searchGroupNote(params.get(0)));
    }

    @Override
    public ResResult<GroupNoteGetVO> getGroupNote(@RequestBody List<String> params) {
        return ResUtils.data(groupNoteService.getGroupNote(params.get(0)));
    }
}
