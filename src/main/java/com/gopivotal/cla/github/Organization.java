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
public final class Organization extends AbstractGitHubType<Map> implements Comparable<Organization> {

    private volatile String name;

    private volatile Repositories repositories;

    /**
     * Creates a new pre-initialized instance
     * 
     * @param name The name of the organization
     * @param repositories The repositories in this organization
     */
    public Organization(String name, Repositories repositories) {
        super(null, null);
        this.name = name;
        this.repositories = repositories;
    }

    Organization(Map raw) {
        super(getString("url", raw), Map.class);
        initialize(raw);
    }

    @Override
    void initialize(Map raw) {
        this.name = getString("login", raw);
        this.repositories = new Repositories(getString("repos_url", raw));
    }

    /**
     * Returns the name of the organization
     * 
     * @return the name of the organization
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the repositories in this organization
     * 
     * @return the repositories in this organization
     */
    public Repositories getRepositories() {
        return this.repositories;
    }

    @Override
    public int compareTo(Organization o) {
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
        if (!(obj instanceof Organization)) {
            return false;
        }
        Organization other = (Organization) obj;
        if (!this.name.equalsIgnoreCase(other.getName())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Organization [name=" + this.name + ", repositories=" + this.repositories + "]";
    }

}
