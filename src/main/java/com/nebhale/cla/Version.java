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
 * A class representing the version of a Contributor License Agreement
 */
public final class Version implements Comparable<Version> {

    private final Long id;

    private final Long agreementId;

    private final String version;

    private final String content;

    /**
     * Create a new instance
     * 
     * @param id The synthetic id of the version
     * @param agreementId The synthetic id of the agreement
     * @param version The version of the agreement
     * @param content The content of the version
     */
    public Version(Long id, Long agreementId, String version, String content) {
        this.id = id;
        this.agreementId = agreementId;
        this.version = version;
        this.content = content;
    }

    /**
     * Returns the synthetic id of the version
     * 
     * @return the synthetic id of the version
     */
    public Long getId() {
        return this.id;
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
     * Returns the version of the agreement
     * 
     * @return the version of the agreement
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Returns the content of the version
     * 
     * @return the content of the version
     */
    public String getContent() {
        return this.content;
    }

    @Override
    public int compareTo(Version o) {
        return this.version.compareTo(o.version);
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
        Version other = (Version) obj;
        if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

}
