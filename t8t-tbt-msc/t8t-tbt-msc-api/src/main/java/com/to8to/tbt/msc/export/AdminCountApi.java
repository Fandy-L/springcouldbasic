package com.to8to.tbt.msc.export;

import com.to8to.sc.response.ResResult;
import com.to8to.tbt.msc.ServiceNameConst;
import com.to8to.tbt.msc.dto.NoteExHistoryDTO;
import com.to8to.tbt.msc.dto.NoteReplyGetDTO;
import com.to8to.tbt.msc.dto.SearchMessageRecordDTO;
import com.to8to.tbt.msc.vo.NoteReplyVO;
import com.to8to.tbt.msc.vo.admin.CountTemplateSendVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author juntao.guo
 */
@Validated
@FeignClient(value = ServiceNameConst.SERVICE_NAME)
public interface AdminCountApi {
    /**
     * 统计模板发送情况
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "统计模板发送情况", response = ResResult.class)
    @PostMapping(value = "/countTemplateSend")
    ResResult<CountTemplateSendVO> countTemplateSend(@RequestBody @NotEmpty @Valid List<@NotNull SearchMessageRecordDTO> params);

    /**
     * 统计通道发送情况
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "统计通道发送情况", response = ResResult.class)
    @PostMapping(value = "/countChannelType")
    ResResult<List<Map<String, Long>>> countChannelType(@RequestBody @NotEmpty @Valid List<@NotNull SearchMessageRecordDTO> params);

    /**
     * 查询短信上行记录
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "查询短信上行记录", response = NoteReplyVO.class)
    @PostMapping(value = "/getNoteReply")
    ResResult<List<NoteReplyVO>> getNoteReply(@RequestBody @NotEmpty @Valid List<@NotNull NoteReplyGetDTO> params);

    /**
     * 统计APP消息记录
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "统计APP消息记录", response = ResResult.class)
    @PostMapping(value = "/getAppMsgRecordCount")
    ResResult<Map<String, Long>> getAppMsgRecordCount(@RequestBody @NotEmpty @Valid List<@NotNull SearchMessageRecordDTO> params);

    /**
     * 查询历史发送记录和回复记录
     *
     * @param params
     * @return
     */
    @ApiOperation(value = "查询历史发送记录和回复记录", response = NoteReplyVO.class)
    @PostMapping(value = "/getNoteExHistory")
    ResResult<List<NoteReplyVO>> getNoteExHistory(@RequestBody @NotEmpty @Valid List<@NotNull NoteExHistoryDTO> params);
}
