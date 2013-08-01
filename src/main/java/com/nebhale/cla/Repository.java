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

package com.nebhale.cla;

/**
 * A class representing a repository that has been linked to an agreement
 */
public final class Repository implements Comparable<Repository> {

    private final Long id;

    private final String name;

    private final Long agreementId;

    private final String accessToken;

    /**
     * Create a new instance
     * 
     * @param id The synthetic id of the repository
     * @param name The name of the repository
     * @param agreementId The synthetic id of the agreement
     * @param accessToken The access token to use to update the repository
     */
    public Repository(Long id, String name, Long agreeementId, String accessToken) {
        this.id = id;
        this.name = name;
        this.agreementId = agreeementId;
        this.accessToken = accessToken;
    }

    /**
     * Returns the synthetic id of the repository
     * 
     * @return the synthetic id of the repository
     */
    public Long getId() {
        return this.id;
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
     * Returns the synthetic id of the agreement
     * 
     * @return the synthetic id of the agreement
     */
    public Long getAgreementId() {
        return this.agreementId;
    }

    /**
     * Returns the access token to use to update the repository
     * 
     * @return the access token to use to update the repository
     */
    public String getAccessToken() {
        return this.accessToken;
    }

    @Override
    public int compareTo(Repository o) {
        return this.name.compareTo(o.name);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + this.id.hashCode();
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
        if (getClass() != obj.getClass()) {
            return false;
        }
        Repository other = (Repository) obj;
        if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Repository [id=" + this.id + ", name=" + this.name + ", agreementId=" + this.agreementId + ", accessToken=" + this.accessToken + "]";
    }

}
