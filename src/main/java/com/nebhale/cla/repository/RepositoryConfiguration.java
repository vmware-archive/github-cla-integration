/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nebhale.cla.repository;

import java.net.URI;
import java.net.URISyntaxException;

import javax.sql.DataSource;

import org.postgresql.Driver;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.Assert;

import com.googlecode.flyway.core.Flyway;
import com.jolbox.bonecp.BoneCPDataSource;

/**
 * Configuration of repository components
 */
@Configuration
@ComponentScan
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
public class RepositoryConfiguration {

    private static final String DB_URL_PROPERTY_NAME = "DATABASE_URL";

    @Bean
    DataSource dataSource() throws URISyntaxException {
        String dbUrlProperty = System.getenv(DB_URL_PROPERTY_NAME);
        Assert.hasText(dbUrlProperty, String.format("The enviroment variable '%s' must be specified", DB_URL_PROPERTY_NAME));

        URI dbUri = new URI(dbUrlProperty);

        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        BoneCPDataSource dataSource = new BoneCPDataSource();
        dataSource.setDriverClass(Driver.class.getCanonicalName());
        dataSource.setJdbcUrl(dbUrl);

        String[] userInfoTokens = dbUri.getUserInfo().split(":");
        if (userInfoTokens.length > 0) {
            dataSource.setUsername(userInfoTokens[0]);
        }
        if (userInfoTokens.length > 1) {
            dataSource.setPassword(userInfoTokens[1]);
        }

        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.migrate();

        return dataSource;
    }

    @Bean
    PlatformTransactionManager transactionManager() throws URISyntaxException {
        return new DataSourceTransactionManager(dataSource());
    }
}