package com.to8to.tbt.msc.repository.mongo.impl;

import com.google.common.collect.Lists;
import com.to8to.common.util.DozerUtils;
import com.to8to.tbt.msc.constant.MsgConstant;
import com.to8to.tbt.msc.dto.GroupNoteSearchDTO;
import com.to8to.tbt.msc.entity.GroupNoteWrapper;
import com.to8to.tbt.msc.entity.PageInfo;
import com.to8to.tbt.msc.entity.SearchTime;
import com.to8to.tbt.msc.entity.mongo.MongoGroupNote;
import com.to8to.tbt.msc.repository.mongo.template.GroupNoteComplexRepository;
import com.to8to.tbt.msc.utils.IntegerUtils;
import com.to8to.tbt.msc.vo.GroupNoteSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author juntao.guo
 */
@Slf4j
@Component
public class GroupNoteComplexRepositoryImpl implements GroupNoteComplexRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public GroupNoteSearchVO searchGroupNote(GroupNoteSearchDTO groupNoteSearchDTO) {
        long totalRecords = 0;
        long totalPages = 0;
        List<GroupNoteWrapper.GroupNote> groupNoteList = Lists.newArrayList();
        List<Criteria> criteriaList = Lists.newArrayList();
        Query query = new Query();
        if (StringUtils.isNotBlank(groupNoteSearchDTO.getContent())) {
            Pattern pattern = Pattern.compile("^.*" + groupNoteSearchDTO.getContent() + ".*$",
                    Pattern.CASE_INSENSITIVE);
            query.addCriteria(Criteria.where("content").regex(pattern));
        }
        if (groupNoteSearchDTO.getSearchTime() != null) {
            String timeField = IntegerUtils.isEqLimitValue(groupNoteSearchDTO.getTimeType()) ? "create_time" : "send_time";
            SearchTime searchTime = groupNoteSearchDTO.getSearchTime();
            if (IntegerUtils.isGtLimitValue(new Long(searchTime.getStartTime()).intValue()) && IntegerUtils.isGtLimitValue(new Long(searchTime.getEndTime()).intValue())) {
                query.addCriteria(Criteria.where(timeField).gte(searchTime.getStartTime()).lte(searchTime.getEndTime()));
            } else if (IntegerUtils.isGtLimitValue(new Long(searchTime.getStartTime()).intValue())) {
                query.addCriteria(Criteria.where(timeField).gte(searchTime.getStartTime()));
            } else if (IntegerUtils.isGtLimitValue(new Long(searchTime.getEndTime()).intValue())) {
                query.addCriteria(Criteria.where(timeField).lte(searchTime.getEndTime()));
            }
        }
        PageInfo pageInfo = groupNoteSearchDTO.getPageInfo();
        try {
            totalRecords = mongoTemplate.count(query, MsgConstant.COLLECTION_GROUP_NOTE);
            totalPages = totalRecords % pageInfo.getPageSize() > 0 ? totalRecords / pageInfo.getPageSize() + 1 : totalRecords / pageInfo.getPageSize();
            int skip = (pageInfo.getCurrPage() - 1) * pageInfo.getPageSize();
            query.with(Sort.by(Sort.Direction.DESC, "create_time"))
                    .skip(skip)
                    .limit(pageInfo.getPageSize());
            List<MongoGroupNote> mongoGroupNoteEntityList = mongoTemplate.find(query, MongoGroupNote.class);
            for (MongoGroupNote mongoGroupNoteEntity : mongoGroupNoteEntityList) {
                GroupNoteWrapper.GroupNote groupNote = DozerUtils.map(mongoGroupNoteEntity, GroupNoteWrapper.GroupNote.class);
                groupNote.setAttachedId(mongoGroupNoteEntity.getId());
                groupNoteList.add(groupNote);
            }
        } catch (Exception e) {
            log.warn("searchGroupNote database error e:{}", e);
        }
        return GroupNoteSearchVO.builder()
                .groupNotes(groupNoteList)
                .totalRecords(totalRecords)
                .totalPages(totalPages)
                .build();
    }
}
