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

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.googlecode.flyway.core.Flyway;
import com.jolbox.bonecp.BoneCPDataSource;
import com.mysql.jdbc.Driver;

/**
 * Configuration of repository components
 */
@Configuration
@ComponentScan
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
public class RepositoryConfiguration {

    @Bean
    DataSource dataSource(String databaseUrl) throws URISyntaxException {
        URI dbUri = new URI(databaseUrl);

        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        BoneCPDataSource dataSource = new BoneCPDataSource();
        dataSource.setDriverClass(Driver.class.getCanonicalName());
        dataSource.setJdbcUrl(dbUrl);

        String[] userInfoTokens = dbUri.getUserInfo().split(":");
        dataSource.setUsername(userInfoTokens.length > 0 ? userInfoTokens[0] : "");
        dataSource.setPassword(userInfoTokens.length > 1 ? userInfoTokens[1] : "");

        dataSource.setPartitionCount(1);
        dataSource.setMaxConnectionsPerPartition(20);

        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource);
        flyway.setLocations("META-INF/db/migration");
        flyway.migrate();

        return dataSource;
    }

    @Bean
    PlatformTransactionManager transactionManager(String databaseUrl) throws URISyntaxException {
        return new DataSourceTransactionManager(dataSource(databaseUrl));
    }

}