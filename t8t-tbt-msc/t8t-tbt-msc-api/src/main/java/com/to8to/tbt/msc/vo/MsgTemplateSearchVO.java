package com.to8to.tbt.msc.vo;

import com.to8to.tbt.msc.entity.TemplateSearchItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author juntao.guo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MsgTemplateSearchVO {
    /**
     * 总数
     */
    private Long total;

    /**
     * 结果集
     */
    private List<TemplateSearchItem> result;
}
