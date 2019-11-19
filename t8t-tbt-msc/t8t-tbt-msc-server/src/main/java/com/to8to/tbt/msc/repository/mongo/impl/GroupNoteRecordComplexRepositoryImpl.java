package com.to8to.tbt.msc.repository.mongo.impl;

import com.to8to.common.search.PageResult;
import com.to8to.tbt.msc.dto.GroupNoteRecordSearchDTO;
import com.to8to.tbt.msc.entity.PageInfo;
import com.to8to.tbt.msc.entity.SearchTime;
import com.to8to.tbt.msc.entity.mongo.MongoGroupNoteRecord;
import com.to8to.tbt.msc.repository.mongo.template.GroupNoteRecordComplexRepository;
import com.to8to.tbt.msc.utils.IntegerUtils;
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
public class GroupNoteRecordComplexRepositoryImpl implements GroupNoteRecordComplexRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public PageResult<MongoGroupNoteRecord> searchGroupNoteRecord(GroupNoteRecordSearchDTO groupNoteRecordSearchDTO) {
        return queryPage(buildCriteria(groupNoteRecordSearchDTO), groupNoteRecordSearchDTO.getPageInfo());
    }

    /**
     * 分页查询
     *
     * @param query
     * @param pageInfo
     * @return
     */
    protected PageResult<MongoGroupNoteRecord> queryPage(Query query, PageInfo pageInfo) {
        Integer skip = (pageInfo.getCurrPage() - 1) * pageInfo.getPageSize();

        long total = mongoTemplate.count(query, MongoGroupNoteRecord.class);
        query.with(Sort.by(Sort.Direction.DESC, "create_time"))
                .skip(skip)
                .limit(pageInfo.getPageSize());
        List<MongoGroupNoteRecord> mongoGroupNoteRecordList = mongoTemplate.find(query, MongoGroupNoteRecord.class);
        PageResult<MongoGroupNoteRecord> pageResult = new PageResult();
        pageResult.setRows(mongoGroupNoteRecordList);
        pageResult.setTotal(total);
        return pageResult;
    }

    /**
     * 根据DTO生成查询条件
     *
     * @param groupNoteRecordSearchDTO
     * @return
     */
    protected Query buildCriteria(GroupNoteRecordSearchDTO groupNoteRecordSearchDTO) {
        Query query = new Query();
        if (groupNoteRecordSearchDTO.getSearchTime() != null) {
            SearchTime searchTime = groupNoteRecordSearchDTO.getSearchTime();
            if (IntegerUtils.isGtLimitValue(searchTime.getStartTime()) && IntegerUtils.isGtLimitValue(searchTime.getEndTime())) {
                query.addCriteria(Criteria.where("send_time").gte(searchTime.getStartTime()).lte(searchTime.getEndTime()));
            } else if (IntegerUtils.isGtLimitValue(searchTime.getStartTime())) {
                query.addCriteria(Criteria.where("send_time").gte(searchTime.getStartTime()));
            } else if (IntegerUtils.isGtLimitValue(searchTime.getEndTime())) {
                query.addCriteria(Criteria.where("send_time").lte(searchTime.getEndTime()));
            }
        }
        if (StringUtils.isNotBlank(groupNoteRecordSearchDTO.getContent())) {
            Pattern pattern = Pattern.compile("^.*" + groupNoteRecordSearchDTO.getContent() + ".*$",
                    Pattern.CASE_INSENSITIVE);
            String field = IntegerUtils.isEqLimitValue(groupNoteRecordSearchDTO.getSearchType()) ? "name" : "phone";
            query.addCriteria(Criteria.where(field).regex(pattern));
        }
        if (StringUtils.isNotBlank(groupNoteRecordSearchDTO.getGroupNoteId())) {
            query.addCriteria(Criteria.where("group_note_id").is(groupNoteRecordSearchDTO.getGroupNoteId()));
        }
        if (IntegerUtils.isGtLimitValue(groupNoteRecordSearchDTO.getSendStatus())){
            query.addCriteria(Criteria.where("status").is(groupNoteRecordSearchDTO.getSendStatus()));
        }
        return query;
    }
}
