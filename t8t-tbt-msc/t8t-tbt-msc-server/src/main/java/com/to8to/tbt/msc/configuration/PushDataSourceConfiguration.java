package com.to8to.tbt.msc.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

/**
 * @author pajero.quan
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.to8to.tbt.msc.repository.mysql.push",
        entityManagerFactoryRef = "pushEntityManagerFactory",
        transactionManagerRef = "pushTransactionManager")
public class PushDataSourceConfiguration {

    @Autowired
    @Qualifier(value = "pushOriginalDataSource")
    private DataSource pushOriginalDataSource;

    @Autowired
    private JpaProperties jpaProperties;

    @Bean(name = "pushEntityManager")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return pushEntityManagerFactory(builder).getObject().createEntityManager();
    }

    @Bean(name = "pushEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean pushEntityManagerFactory (EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(pushOriginalDataSource)
                .properties(getVendorProperties())
                .packages("com.to8to.tbt.msc.entity.mysql.push")
                .persistenceUnit("pushPersistenceUnit")
                .build();
    }

    private Map<String, Object> getVendorProperties() {
        return jpaProperties.getHibernateProperties(new HibernateSettings());
    }

    @Bean(name = "pushTransactionManager")
    PlatformTransactionManager pushTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(pushEntityManagerFactory(builder).getObject());
    }
}
