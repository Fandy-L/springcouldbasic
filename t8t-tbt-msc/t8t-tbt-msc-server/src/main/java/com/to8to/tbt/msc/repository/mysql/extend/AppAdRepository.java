package com.to8to.tbt.msc.repository.mysql.extend;

import com.to8to.tbt.msc.entity.mysql.extend.AppAd;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author juntao.guo
 */
public interface AppAdRepository extends CrudRepository<AppAd, Integer> {
    /**
     * 批量查询活动消息
     *
     * @param startTime
     * @param currentTime
     * @param appId
     * @param appOsType
     * @param usedCity
     * @param enableVersion
     * @return
     */
    @Query("select a from AppAd a where a.adType=4 AND a.status=1 AND a.beginTime<=:currentTime AND a.beginTime>=:startTime AND a.appId=:appId AND a.platform=:appOsType and (a.usedCity='' or a.usedCity=0 or find_in_set(-1,a.usedCity) > 0 or find_in_set(-2,a.usedCity) > 0 or find_in_set(:usedCity,a.usedCity) > 0) and (a.enableVersion in (:enableVersion,'全部','0') or find_in_set(:enableVersion,a.enableVersion) > 0) ORDER BY a.beginTime DESC")
    List<AppAd> queryAppAds(@Param("startTime") int startTime, @Param("currentTime") int currentTime, @Param("appId") int appId, @Param("appOsType") int appOsType, @Param("usedCity") int usedCity, @Param("enableVersion") String enableVersion);
}
