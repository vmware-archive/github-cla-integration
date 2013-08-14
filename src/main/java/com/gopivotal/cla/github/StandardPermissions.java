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
final class StandardPermissions extends AbstractGitHubType<Map> implements Permissions {

    private volatile Boolean admin;

    private volatile Boolean push;

    private volatile Boolean pull;

    StandardPermissions(Map raw) {
        super(null, Map.class);
        initialize(raw);
    }

    @Override
    void initialize(Map raw) {
        this.admin = getBoolean("admin", raw);
        this.push = getBoolean("push", raw);
        this.pull = getBoolean("pull", raw);
    }

    /**
     * Returns whether the user has admin permissions
     * 
     * @return whether the user has admin permissions
     */
    @Override
    public Boolean isAdmin() {
        return this.admin;
    }

    /**
     * Returns whether the user has push permissions
     * 
     * @return whether the user has push permissions
     */
    @Override
    public Boolean isPush() {
        return this.push;
    }

    /**
     * Returns whether the user has pull permissions
     * 
     * @return whether the user has pull permissions
     */
    @Override
    public Boolean isPull() {
        return this.pull;
    }

    @Override
    public int compareTo(Permissions o) {
        int comparison = this.admin.compareTo(o.isAdmin());

        if (comparison == 0) {
            comparison = this.pull.compareTo(o.isPull());
        }

        if (comparison == 0) {
            comparison = this.push.compareTo(o.isPush());
        }

        return comparison;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + this.admin.hashCode();
        result = (prime * result) + this.pull.hashCode();
        result = (prime * result) + this.push.hashCode();
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
        if (!(obj instanceof Permissions)) {
            return false;
        }
        Permissions other = (Permissions) obj;
        if (!this.admin.equals(other.isAdmin())) {
            return false;
        }
        if (!this.pull.equals(other.isPull())) {
            return false;
        }
        if (!this.push.equals(other.isPush())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "StandardPermissions [admin=" + this.admin + ", push=" + this.push + ", pull=" + this.pull + "]";
    }

}
