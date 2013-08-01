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

package com.nebhale.cla;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Configuration of beans representing environment variable based configuration
 */
@Configuration
public class EnvironmentVariableConfiguration {

    private static final int MINIMUM_ENCRYPTION_KEY_LENGTH = 50;

    @Bean
    String[] adminEmailDomains() {
        return StringUtils.commaDelimitedListToStringArray(getRequiredProperty("ADMIN_EMAIL_DOMAINS"));
    }

    @Bean
    String databaseUrl() {
        return getRequiredProperty("DATABASE_URL");
    }

    @Bean
    String encryptionKey() {
        String encryptionKey = getRequiredProperty("ENCRYPTION_KEY");
        Assert.isTrue(encryptionKey.length() >= MINIMUM_ENCRYPTION_KEY_LENGTH,
            String.format("The minimum length for the ENCRYPTION_KEY is %d characters", MINIMUM_ENCRYPTION_KEY_LENGTH));

        return encryptionKey;
    }

    @Bean
    String gitHubClientId() {
        return getRequiredProperty("GITHUB_CLIENT_ID");
    }

    @Bean
    String gitHubClientSecret() {
        return getRequiredProperty("GITHUB_CLIENT_SECRET");
    }

    private String getRequiredProperty(String key) {
        String property = System.getenv(key);
        Assert.hasText(property, String.format("The enviroment variable '%s' must be specified", key));
        return property;
    }
}
