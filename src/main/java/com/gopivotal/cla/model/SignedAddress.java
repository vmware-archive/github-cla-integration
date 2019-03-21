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
 * A class representing an email address that has been signed for
 */
@Entity
@Table(name = "signedAddresses")
public final class SignedAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private volatile Integer id;

    private volatile String address;

    @ManyToOne
    @JoinColumn(name = "agreementId")
    private volatile Agreement agreement;

    @ManyToOne
    @JoinColumn(name = "signatoryId")
    private volatile IndividualSignatory individualSignatory;

    /**
     * Creates an instance
     * 
     * @param address The signed for address
     * @param agreement The agreement that was signed
     * @param individualSignatory The signatory that signed for the email address
     */
    public SignedAddress(String address, Agreement agreement, IndividualSignatory individualSignatory) {
        this.address = address;
        this.agreement = agreement;
        this.individualSignatory = individualSignatory;
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
     * Returns the signed for address
     * 
     * @return the signed for address
     */
    public String getAddress() {
        return this.address;
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
     * Returns the signatory that signed for the email address
     * 
     * @return the signatory that signed for the email address
     */
    public IndividualSignatory getIndividualSignatory() {
        return this.individualSignatory;
    }

    @Override
    public String toString() {
        return "SignedAddress [id=" + this.id + ", address=" + this.address + ", agreement=" + this.agreement + ", individualSignatory="
            + this.individualSignatory + "]";
    }

    /**
     * <b>DO NOT USE</b> Provided for use by ORM
     */
    SignedAddress() {
    }
}
