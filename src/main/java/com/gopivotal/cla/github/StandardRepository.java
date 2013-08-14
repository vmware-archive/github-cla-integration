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
final class StandardRepository extends AbstractGitHubType<Map> implements Repository {

    private volatile String fullName;

    private volatile String name;

    private volatile Permissions permissions;

    StandardRepository(Map raw) {
        super(getString("url", raw), Map.class);
        initialize(raw);
    }

    @Override
    void initialize(Map raw) {
        this.fullName = getString("full_name", raw);
        this.name = getString("name", raw);
        this.permissions = new StandardPermissions(getMap("permissions", raw));
    }

    @Override
    public String getFullName() {
        return this.fullName;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
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
        return "StandardRepository [fullName=" + this.fullName + ", name=" + this.name + ", permissions=" + this.permissions + "]";
    }

}
