package com.to8to.tbt.msc.service;

import com.to8to.tbt.msc.BaseApplication;
import com.to8to.tbt.msc.dto.NoteExHistoryDTO;
import com.to8to.tbt.msc.dto.NoteReplyGetDTO;
import com.to8to.tbt.msc.dto.SearchMessageRecordDTO;
import com.to8to.tbt.msc.entity.admin.CountTemplateSendItem;
import com.to8to.tbt.msc.entity.admin.CountTemplateSendTemplate;
import com.to8to.tbt.msc.entity.template.TemplateInfo;
import com.to8to.tbt.msc.vo.NoteReplyVO;
import com.to8to.tbt.msc.vo.admin.CountTemplateSendVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * @author juntao.guo
 */
@Slf4j
@RunWith(value = SpringRunner.class)
public class AdminCountServiceTest extends BaseApplication {

    @Autowired
    private AdminCountService adminCountService;

    @Test
    public void countTemplateSend(){
        int sendType = 1;
        int pmModule = 174;
        int targetType = 4;
        SearchMessageRecordDTO searchMessageRecordDTO = SearchMessageRecordDTO.builder()
                .sendType(sendType)
                .pmModule(pmModule)
                .targetType(targetType)
                .build();
        CountTemplateSendVO countTemplateSendVO = adminCountService.countTemplateSend(searchMessageRecordDTO);
        Map<String, CountTemplateSendItem> countTemplateSendItemMap = countTemplateSendVO.getTemplateCount();
        Assert.assertTrue(countTemplateSendItemMap.size() > 0);
        log.debug("size:{} countTemplateSendVOMap:{}", countTemplateSendItemMap.size(), countTemplateSendItemMap);
        for (Map.Entry<String, CountTemplateSendItem> entry : countTemplateSendItemMap.entrySet()){
            CountTemplateSendItem countTemplateSendItem = entry.getValue();
            TemplateInfo countTemplateSendTemplate = countTemplateSendItem.getResult();
            log.debug("countTemplateSendVO tid:{} total:{} title:{}", countTemplateSendTemplate.getTid(), countTemplateSendItem.getTotal(), countTemplateSendTemplate.getTitle());
            Assert.assertTrue(countTemplateSendTemplate.getTid() > 0 && StringUtils.isNotBlank(countTemplateSendTemplate.getTitle()));
        }
    }

    @Test
    public void countChannelType(){
        SearchMessageRecordDTO searchMessageRecordDTO = SearchMessageRecordDTO.builder()
                .build();
        List<Map<String, Long>> countChannelTypeMapList = adminCountService.countChannelType(searchMessageRecordDTO);
        Assert.assertFalse(countChannelTypeMapList.isEmpty());
        log.debug("countChannelTypeMapList:{}", countChannelTypeMapList);
    }

    @Test
    public void getNoteReply(){
        int phoneId = 1351192;
        NoteReplyGetDTO noteReplyGetDTO = NoteReplyGetDTO.builder()
                .phoneId(String.valueOf(phoneId))
                .build();
        List<NoteReplyVO> noteReplyVOList = adminCountService.getNoteReply(noteReplyGetDTO);
        log.debug("size:{} list:{}", noteReplyVOList.size(), noteReplyVOList);
        Assert.assertFalse(noteReplyVOList.isEmpty());
    }

    @Test
    public void getNoteExHistory(){
        int phoneId = 7141775;
        NoteExHistoryDTO noteExHistoryDTO = NoteExHistoryDTO.builder()
                .phoneId(phoneId)
                .build();
        List<NoteReplyVO> noteReplyVOList = adminCountService.getNoteExHistory(noteExHistoryDTO);
        Assert.assertFalse(noteReplyVOList.isEmpty());
        log.debug("size:{} list:{}", noteReplyVOList.size(), noteReplyVOList);
    }
}
