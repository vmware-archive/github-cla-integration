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

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@SuppressWarnings("rawtypes")
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.INTERFACES)
public final class StandardGitHubClient extends AbstractGitHubType<Map> implements GitHubClient {

    private static final String ROOT_URL = "https://api.github.com/";

    private final OAuth2RestOperations restOperations;

    private volatile Emails emails;

    private volatile User user;

    @Autowired
    StandardGitHubClient(OAuth2RestOperations restOperations) {
        super(ROOT_URL, Map.class);
        this.restOperations = restOperations;
    }

    @Override
    void initialize(Map raw) {
        this.emails = new StandardEmails(getString("emails_url", raw));
        this.user = new StandardUser(getString("current_user_url", raw));
    }

    @Override
    public String getAccessToken() {
        return this.restOperations.getAccessToken().getValue();
    }

    @Override
    public Emails getEmails() {
        return this.emails;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public String toString() {
        return "StandardGitHubClient [emails=" + this.emails + ", user=" + this.user + "]";
    }

}
