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

package com.gopivotal.cla.github;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public final class StandardGitHubClientTest {

    private final OAuth2RestOperations restOperations = mock(OAuth2RestOperations.class);

    private final StandardGitHubClient gitHubClient = new StandardGitHubClient(this.restOperations);

    @Before
    public void initialize() {
        Map<String, Object> raw = new HashMap<>();
        raw.put("emails_url", "test-emails-url");
        raw.put("current_user_url", "test-current-user-url");

        this.gitHubClient.initialize(raw);
    }

    @Test
    public void getAccessToken() {
        OAuth2AccessToken accessToken = mock(OAuth2AccessToken.class);
        when(accessToken.getValue()).thenReturn("test-access-token");
        when(this.restOperations.getAccessToken()).thenReturn(accessToken);

        assertEquals("test-access-token", this.gitHubClient.getAccessToken());
    }

    @Test
    public void getEmails() {
        assertNotNull(this.gitHubClient.getEmails());
    }

    @Test
    public void getUser() {
        assertNotNull(this.gitHubClient.getUser());
    }

}
