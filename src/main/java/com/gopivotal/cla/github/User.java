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

/**
 * A GitHub User
 */
@SuppressWarnings("rawtypes")
public final class User extends AbstractGitHubType<Map> implements Comparable<User> {

    private volatile String avatarUrl;

    private volatile String company;

    private volatile String login;

    private volatile String name;

    private volatile Organizations organizations;

    private volatile Repositories repositories;

    /**
     * Creates a new pre-initalized instance
     * 
     * @param avatarUrl The user's avatar url
     * @param company The user's company
     * @param login The user's login
     * @param name The user's name
     * @param organizations The user's organizations
     * @param repositories The user's repositories
     */
    public User(String avatarUrl, String company, String login, String name, Organizations organizations, Repositories repositories) {
        super(null, null);
        this.avatarUrl = avatarUrl;
        this.company = company;
        this.login = login;
        this.name = name;
        this.organizations = organizations;
        this.repositories = repositories;
    }

    User(String url) {
        super(url, Map.class);
    }

    @Override
    void initialize(Map raw) {
        this.avatarUrl = getString("avatar_url", raw);
        this.company = getString("company", raw);
        this.login = getString("login", raw);
        this.name = getString("name", raw);
        this.organizations = new Organizations(getString("organizations_url", raw));
        this.repositories = new Repositories(getString("repos_url", raw));
    }

    /**
     * Returns the user's avatar url
     * 
     * @return the user's avatar url
     */
    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    /**
     * Returns the user's company
     * 
     * @return the user's company
     */

    public String getCompany() {
        return this.company;
    }

    /**
     * Returns the user's login
     * 
     * @return the user's login
     */

    public String getLogin() {
        return this.login;
    }

    /**
     * Returns the user's name
     * 
     * @return the user's name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the user's organizations
     * 
     * @return the user's organizations
     */
    public Organizations getOrganizations() {
        return this.organizations;
    }

    /**
     * Returns the user's repositories
     * 
     * @return the user's repositories
     */
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
        return "User [avatarUrl=" + this.avatarUrl + ", company=" + this.company + ", login=" + this.login + ", name=" + this.name
            + ", organizations=" + this.organizations + ", repositories=" + this.repositories + "]";
    }

}
