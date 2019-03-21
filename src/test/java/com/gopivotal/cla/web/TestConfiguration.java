/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gopivotal.cla.web;

import static org.mockito.Mockito.mock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gopivotal.cla.github.GitHubClient;
import com.gopivotal.cla.github.MarkdownService;

/**
 * Configuration of beans representing environment variable based configuration
 */
@Configuration
class TestConfiguration {

    @Bean
    String databaseUrl() {
        return String.format("postgresql://%s@127.0.0.1:5432/test", System.getenv("USER"));
    }

    @Bean
    String encryptionKey() {
        return "aYvcyUMwzGpKh7HptueVQJPkCVSYjxpOOSZeVby4FWP";
    }

    @Bean
    MarkdownService markdownService() {
        return mock(MarkdownService.class);
    }

    @Bean
    GitHubClient gitHubClient() {
        return mock(GitHubClient.class);
    }
}
