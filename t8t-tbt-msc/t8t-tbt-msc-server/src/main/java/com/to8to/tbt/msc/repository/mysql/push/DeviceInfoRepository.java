package com.to8to.tbt.msc.repository.mysql.push;

import com.to8to.tbt.msc.entity.mysql.push.QuickAppDeviceInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author edmund.yu
 */
public interface DeviceInfoRepository extends JpaRepository<QuickAppDeviceInfo,Integer> {

    /**
     * 根据deviceid获取整个设备信息。
     * @param deviceId
     * @return
     */
    Optional<QuickAppDeviceInfo> findByDeviceId(String deviceId);
}
