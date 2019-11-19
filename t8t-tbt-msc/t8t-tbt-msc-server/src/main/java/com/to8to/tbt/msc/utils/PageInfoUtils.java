package com.to8to.tbt.msc.utils;

import com.to8to.tbt.msc.entity.PageInfo;

/**
 * @author edmund.yu
 */
public class PageInfoUtils {

    public static void CreatDefaultPageInfo(PageInfo pageInfo) {

        if (pageInfo != null) {
            if (pageInfo.getCurrPage() == null || pageInfo.getCurrPage() < 1) {
                pageInfo.setCurrPage(1);
            }
            if (pageInfo.getPageSize() == null || pageInfo.getPageSize() < 1) {
                pageInfo.setPageSize(10);
            }
        }
    }
}
