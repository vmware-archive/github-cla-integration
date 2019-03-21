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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * A class representing an email address domain that has been signed for
 */
@Entity
@Table(name = "signedDomains")
public final class SignedDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private volatile Integer id;

    private volatile String domain;

    @ManyToOne
    @JoinColumn(name = "agreementId")
    private volatile Agreement agreement;

    @ManyToOne
    @JoinColumn(name = "signatoryId")
    private volatile CorporateSignatory corporateSignatory;

    /**
     * Creates an instance
     * 
     * @param address The signed for domain
     * @param agreement The agreement that was signed
     * @param corporateSignatory The signatory that signed for the email address domain
     */
    public SignedDomain(String domain, Agreement agreement, CorporateSignatory corporateSignatory) {
        this.domain = domain;
        this.agreement = agreement;
        this.corporateSignatory = corporateSignatory;
    }

    /**
     * Returns the synthetic id of the signed address
     * 
     * @return the synthetic id of the signed address
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Returns the signed for domain
     * 
     * @return the signed for domain
     */
    public String getDomain() {
        return this.domain;
    }

    /**
     * Returns the agreement that was signed
     * 
     * @return the agreement that was signed
     */
    public Agreement getAgreement() {
        return this.agreement;
    }

    /**
     * Returns the signatory that signed for the email address domain
     * 
     * @return the signatory that signed for the email address domain
     */
    public CorporateSignatory getCorporateSignatory() {
        return this.corporateSignatory;
    }

    @Override
    public String toString() {
        return "SignedDomain [id=" + this.id + ", domain=" + this.domain + ", agreement=" + this.agreement + ", corporateSignatory="
            + this.corporateSignatory + "]";
    }

    /**
     * <b>DO NOT USE</b> Provided for use by ORM
     */
    SignedDomain() {
    }
}
