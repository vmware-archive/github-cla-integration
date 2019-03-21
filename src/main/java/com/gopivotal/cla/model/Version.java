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

package com.gopivotal.cla.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * A class representing a specific version of a Contributor License Agreement
 */
@Entity
@Table(name = "versions")
public final class Version {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private volatile Integer id;

    @ManyToOne
    @JoinColumn(name = "agreementId")
    private volatile Agreement agreement;

    private volatile String name;

    @Column(name = "individualContent")
    private volatile String individualAgreementContent;

    @Column(name = "corporateContent")
    private volatile String corporateAgreementContent;

    /**
     * Create a new instance
     * 
     * @param agreement The agreement this version is related to
     * @param name The name of the version
     * @param individualAgreementContent The content of the individual agreement
     * @param corporateAgreementContent The content of the corporate agreement
     */
    public Version(Agreement agreement, String name, String individualAgreementContent, String corporateAgreementContent) {
        this.agreement = agreement;
        this.name = name;
        this.individualAgreementContent = individualAgreementContent;
        this.corporateAgreementContent = corporateAgreementContent;
    }

    /**
     * Returns the synthetic id of the version
     * 
     * @return the synthetic id of the version
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Returns the agreement this version is related to
     * 
     * @return agreement this version is related to
     */
    public Agreement getAgreement() {
        return this.agreement;
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
    public String toString() {
        return "Version [id=" + this.id + ", agreement=" + this.agreement + ", name=" + this.name + ", individualAgreementContent="
            + this.individualAgreementContent + ", corporateAgreementContent=" + this.corporateAgreementContent + "]";
    }

    /**
     * <b>DO NOT USE</b> Provided for use by ORM
     */
    Version() {
    }

}
