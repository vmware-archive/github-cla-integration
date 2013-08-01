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

package com.gopivotal.cla;

/**
 * A class representing a specific version of a Contributor License Agreement
 */
public final class Version implements Comparable<Version> {

    private final Long id;

    private final Long agreementId;

    private final String name;

    private final String individualAgreementContent;

    private final String corporateAgreementContent;

    /**
     * Create a new instance
     * 
     * @param id The synthetic id of the version
     * @param agreementId The synthetic id of the agreement
     * @param name The name of the version
     * @param individualAgreementContent The content of the individual agreement
     * @param corporateAgreementContent The content of the corporate agreement
     */
    public Version(Long id, Long agreementId, String name, String individualAgreementContent, String corporateAgreementContent) {
        this.id = id;
        this.agreementId = agreementId;
        this.name = name;
        this.individualAgreementContent = individualAgreementContent;
        this.corporateAgreementContent = corporateAgreementContent;
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
     * Returns the name of the version
     * 
     * @return the name of the version
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the content of the individual agreement
     * 
     * @return the content of the individual agreement
     */
    public String getIndividualAgreementContent() {
        return this.individualAgreementContent;
    }

    /**
     * Returns the content of the corporate agreement
     * 
     * @return the content of the corporate agreement
     */
    public String getCorporateAgreementContent() {
        return this.corporateAgreementContent;
    }

    @Override
    public int compareTo(Version o) {
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
        Version other = (Version) obj;
        if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Version [id=" + this.id + ", agreementId=" + this.agreementId + ", name=" + this.name + "]";
    }

}
