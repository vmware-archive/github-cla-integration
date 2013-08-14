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

@SuppressWarnings("rawtypes")
final class StandardUser extends AbstractGitHubType<Map> implements User {

    private volatile String avatarUrl;

    private volatile String login;

    private volatile String name;

    private volatile Organizations organizations;

    private volatile Repositories repositories;

    StandardUser(String url) {
        super(url, Map.class);
    }

    @Override
    void initialize(Map raw) {
        this.avatarUrl = getString("avatar_url", raw);
        this.login = getString("login", raw);
        this.name = getString("name", raw);
        this.organizations = new StandardOrganizations(getString("organizations_url", raw));
        this.repositories = new StandardRepositories(getString("repos_url", raw));
    }

    @Override
    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    @Override
    public String getLogin() {
        return this.login;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Organizations getOrganizations() {
        return this.organizations;
    }

    @Override
    public Repositories getRepositories() {
        return this.repositories;
    }

    @Override
    public int compareTo(User o) {
        return this.name.compareToIgnoreCase(o.getName());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + this.name.toLowerCase().hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        User other = (User) obj;
        if (!this.name.equalsIgnoreCase(other.getName())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "StandardUser [avatarUrl=" + this.avatarUrl + ", login=" + this.login + ", name=" + this.name + ", organizations="
            + this.organizations + ", repositories=" + this.repositories + "]";
    }

}
