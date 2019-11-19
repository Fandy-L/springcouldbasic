package com.to8to.tbt.msc.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author juntao.guo
 */
@Data
@Builder
public class PageResult {
    /**
     * 总数
     */
    private long total;

    private List data;
}
