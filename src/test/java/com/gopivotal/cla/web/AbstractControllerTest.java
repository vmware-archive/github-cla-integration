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

import static org.mockito.Mockito.when;

import org.jasypt.encryption.pbe.PBEStringEncryptor;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.gopivotal.cla.github.GitHubClient;
import com.gopivotal.cla.github.Organization;
import com.gopivotal.cla.github.Organizations;
import com.gopivotal.cla.github.Repositories;
import com.gopivotal.cla.github.User;
import com.gopivotal.cla.repository.RepositoryConfiguration;

@WebAppConfiguration
@ContextHierarchy({ @ContextConfiguration(classes = { TestConfiguration.class, RepositoryConfiguration.class }),
    @ContextConfiguration(classes = WebConfiguration.class) //
})
public abstract class AbstractControllerTest extends AbstractTransactionalJUnit4SpringContextTests {

    protected volatile MockMvc mockMvc;

    protected volatile Organization organization;

    protected volatile User user;

    @Autowired
    private volatile GitHubClient gitHubClient;

    @Autowired
    private volatile PBEStringEncryptor stringEncryptor;

    @Autowired
    private volatile WebApplicationContext wac;

    @Before
    public final void mockMvc() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Before
    public final void gitHubClient() {
        this.organization = new Organization("test-name", new Repositories());

        Organizations organizations = new Organizations();
        organizations.add(this.organization);

        this.user = new User("test-avatar-url", "test-company", "test-login", "test-name", organizations, new Repositories());
        when(this.gitHubClient.getUser()).thenReturn(this.user);
    }

    protected final String decrypt(Object value) {
        return this.stringEncryptor.decrypt((String) value);
    }

}
