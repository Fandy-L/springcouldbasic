package com.to8to.tbt.msc.repository.mysql.push;

import com.to8to.tbt.msc.entity.mysql.push.App;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

/**
 * @author pajero.quan
 */
public interface AppRepository extends JpaRepository<App, Integer> {
    boolean existsByPushAppIdAndDeleted(Integer pushAppId, Byte deleted);
    Optional<App> findTopByPushAppIdAndDeleted(Integer pushAppId, Byte deleted);

    List<App> findByDeleted(Byte deleted);
}
