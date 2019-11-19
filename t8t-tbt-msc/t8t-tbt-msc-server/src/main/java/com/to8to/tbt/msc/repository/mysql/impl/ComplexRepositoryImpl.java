package com.to8to.tbt.msc.repository.mysql.impl;

import com.to8to.tbt.msc.constant.MsgConstant;
import com.to8to.tbt.msc.entity.mysql.main.AppRecord;
import com.to8to.tbt.msc.entity.mysql.main.Template;
import com.to8to.tbt.msc.repository.mysql.main.AppRecordRepository;
import com.to8to.tbt.msc.repository.mysql.ComplexRepository;
import com.to8to.tbt.msc.repository.mysql.main.TemplateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author juntao.guo
 */
@Slf4j
@Component
public class ComplexRepositoryImpl implements ComplexRepository {

    @Autowired
    private AppRecordRepository appRecordRepository;

    @Autowired
    private TemplateRepository templateRepository;

    @Override
    public boolean setAppMsgReaded(int id) {
        boolean status = false;
        AppRecord appRecord = appRecordRepository.findById(id).orElse(null);
        if (appRecord != null){
            appRecord.setIsRead(MsgConstant.APP_RECORD_HAS_READ_STATUS);
            appRecord = appRecordRepository.save(appRecord);
            if (appRecord.getIsRead() == MsgConstant.APP_RECORD_HAS_READ_STATUS){
                status = true;
            }
        }
        return status;
    }

    @Override
    public Set<Integer> queryTidListByNodeIds(List<Integer> nodeIds) {
        Set<Integer> tidList = null;
        try {
            List<Template> templates = templateRepository.findAllByNodeIdIn(nodeIds);
            if (templates != null && templates.size() > 0){
                tidList = templates.stream().map(template -> template.getTid()).collect(Collectors.toSet());
            }
            log.debug("queryTidListByNodeIds nodeIds:{} tidList:{} templates:{}", nodeIds, tidList, templates);
;        }catch (Exception e){
            log.warn("queryTidListByNodeIds exception nodeIds:{} e:{}", nodeIds, e);
        }
        return tidList;
    }
}
