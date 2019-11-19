package com.to8to.tbt.msc.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.util.Map;

/**
 * @author juntao.guo
 */
@Slf4j
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.to8to.tbt.msc.repository.mysql.main", entityManagerFactoryRef = "mainEntityManagerFactory", transactionManagerRef = "mainTransactionManager")
public class MainDataSourceConfiguration {

    @Autowired
    @Qualifier(value = "mainOriginalDataSource")
    private DataSource mainOriginalDataSource;

    @Autowired
    private JpaProperties jpaProperties;

    @Bean(name = "mainEntityManager")
    @Primary
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return mainEntityManagerFactory(builder).getObject().createEntityManager();
    }

    @Bean(name = "mainEntityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean mainEntityManagerFactory (EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(mainOriginalDataSource)
                .properties(getVendorProperties())
                .packages("com.to8to.tbt.msc.entity.mysql.main")
                .persistenceUnit("mainPersistenceUnit")
                .build();
    }

    private Map<String, Object> getVendorProperties() {
        return jpaProperties.getHibernateProperties(new HibernateSettings());
    }

    @Bean(name = "mainTransactionManager")
    @Primary
    PlatformTransactionManager mainTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(mainEntityManagerFactory(builder).getObject());
    }
}
