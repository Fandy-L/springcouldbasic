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
 * @author juntao.guo
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.to8to.tbt.msc.repository.mysql.extend", entityManagerFactoryRef = "extendEntityManagerFactory", transactionManagerRef = "extendTransactionManager")
public class ExtendDataSourceConfiguration {

    @Autowired
    @Qualifier(value = "extendOriginalDataSource")
    private DataSource extendOriginalDataSource;

    @Autowired
    private JpaProperties jpaProperties;

    @Bean(name = "extendEntityManager")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return extendEntityManagerFactory(builder).getObject().createEntityManager();
    }

    @Bean(name = "extendEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean extendEntityManagerFactory (EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(extendOriginalDataSource)
                .properties(getVendorProperties())
                .packages("com.to8to.tbt.msc.entity.mysql.extend")
                .persistenceUnit("extendPersistenceUnit")
                .build();
    }

    private Map<String, Object> getVendorProperties() {
        return jpaProperties.getHibernateProperties(new HibernateSettings());
    }

    @Bean(name = "extendTransactionManager")
    PlatformTransactionManager extendTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(extendEntityManagerFactory(builder).getObject());
    }
}
