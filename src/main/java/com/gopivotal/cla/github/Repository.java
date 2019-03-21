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

package com.gopivotal.cla.github;

import java.util.Map;

/**
 * A GitHub Repository
 */
@SuppressWarnings("rawtypes")
public final class Repository extends AbstractGitHubType<Map> implements Comparable<Repository> {

    private volatile String fullName;

    private volatile String name;

    private volatile Permissions permissions;

    /**
     * Creates a new pre-initalized instance
     * 
     * @param fullName The full name of the repository
     * @param name The name of the repository
     * @param permissions The user's permissions in this repository
     */
    public Repository(String fullName, String name, Permissions permissions) {
        super(null, null);
        this.fullName = fullName;
        this.name = name;
        this.permissions = permissions;
    }

    Repository(Map raw) {
        super(getString("url", raw), Map.class);
        initialize(raw);
    }

    @Override
    void initialize(Map raw) {
        this.fullName = getString("full_name", raw);
        this.name = getString("name", raw);
        this.permissions = new Permissions(getMap("permissions", raw));
    }

    /**
     * Returns the full name of the repository
     * 
     * @return the fill name of the repository
     */
    public String getFullName() {
        return this.fullName;
    }

    /**
     * Returns the name of the repository
     * 
     * @return the name of the repository
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the user's permissions in this repository
     * 
     * @return the user's permissions in this repository
     */
    public Permissions getPermissions() {
        return this.permissions;
    }

    @Override
    public int compareTo(Repository o) {
        return this.fullName.compareToIgnoreCase(o.getFullName());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + this.fullName.toLowerCase().hashCode();
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
        if (!(obj instanceof Repository)) {
            return false;
        }
        Repository other = (Repository) obj;
        if (!this.fullName.equalsIgnoreCase(other.getFullName())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Repository [fullName=" + this.fullName + ", name=" + this.name + ", permissions=" + this.permissions + "]";
    }

}
