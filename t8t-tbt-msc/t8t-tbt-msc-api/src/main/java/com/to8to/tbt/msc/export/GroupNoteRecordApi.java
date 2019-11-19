package com.to8to.tbt.msc.export;

import com.alibaba.fastjson.JSONArray;
import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.ServiceNameConst;
import com.to8to.tbt.msc.dto.GroupNoteRecordSearchDTO;
import com.to8to.tbt.msc.dto.GroupNoteRecordUpdateDTO;
import com.to8to.tbt.msc.entity.GroupNoteRecordWrapper;
import com.to8to.tbt.msc.entity.response.MsgCenterResponse;
import com.to8to.tbt.msc.vo.GroupNoteRecordSearchVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author juntao.guo
 */
@Validated
@FeignClient(value = ServiceNameConst.SERVICE_NAME)
public interface GroupNoteRecordApi {

    /**
     * 批量删除群发短信群发记录
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "批量删除群发短信群发记录", response = ResResult.class)
    @PostMapping(value = "/delGroupNoteRecord")
    ResResult delGroupNoteRecord(@RequestBody @NotEmpty @Valid List<@NotEmpty @Valid List<@NotBlank String>> params);

    /**
     * 更新群发短信记录
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "更新群发短信记录", response = ResResult.class)
    @PostMapping(value = "/updateGroupNoteRecord")
    ResResult updateGroupNoteRecord(@RequestBody @NotEmpty @Valid List<@NotNull GroupNoteRecordUpdateDTO> params);

    /**
     * 获取群发记录列表
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "获取群发记录列表", response = GroupNoteRecordSearchVO.class)
    @PostMapping(value = "/searchGroupNoteRecord")
    ResResult<GroupNoteRecordSearchVO> searchGroupNoteRecord(@RequestBody @NotEmpty @Valid List<@NotNull GroupNoteRecordSearchDTO> params);

    /**
     * 导出群发短信记录
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "导出群发短信记录", response = GroupNoteRecordWrapper.GroupNoteRecord.class)
    @PostMapping(value = "/exportGroupNoteRecord")
    ResResult<List<GroupNoteRecordWrapper.GroupNoteRecord>> exportGroupNoteRecord(@RequestBody @NotEmpty @Valid List<@NotNull GroupNoteRecordSearchDTO> params);

    /**
     * 导入群发消息对象
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "导入群发消息对象", response = ResResult.class)
    @PostMapping(value = "/importGroupNoteRecord")
    ResResult importGroupNoteObject(@RequestBody @Size(min = 2) JSONArray params);

    /**
     * 群发短信
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "群发短信", response = ResResult.class)
    @PostMapping(value = "/groupSendNote")
    ResResult<MsgCenterResponse> groupSendNote(@RequestBody @Size(min = 2) List<String> params);

    /**
     * 重新群发短信
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "重新群发短信", response = ResResult.class)
    @PostMapping(value = "/groupReSendNote")
    ResResult<MsgCenterResponse> groupReSendNote(@RequestBody @Size(min = 2) JSONArray params);
}
