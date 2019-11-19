package com.to8to.tbt.msc.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author juntao.guo
 */
@Configuration
public class DruidDataSourceConfiguration {

    @Bean(name = "mainOriginalDataSource")
    @Qualifier("mainOriginalDataSource")
    @Primary
    @ConfigurationProperties(prefix="datasource.main")
    public DataSource mainOriginalDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "extendOriginalDataSource")
    @Qualifier("extendOriginalDataSource")
    @ConfigurationProperties(prefix="datasource.extend")
    public DataSource extendOriginalDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "pushOriginalDataSource")
    @Qualifier("pushOriginalDataSource")
    @ConfigurationProperties(prefix="datasource.push")
    public DataSource pushOriginalDataSource(){
        return DataSourceBuilder.create().build();
    }
}
