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

package com.gopivotal.cla.model;

import java.util.Date;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Type;

/**
 * A class representing a specific signatory to a Contributor License Agreement
 */
@Entity
@Table(name = "signatories")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "signatoryType")
@DiscriminatorValue("individual")
public class IndividualSignatory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private volatile Integer id;

    @ManyToOne
    @JoinColumn(name = "versionId")
    private volatile Version version;

    @Temporal(TemporalType.TIMESTAMP)
    private volatile Date signingDate;

    @Type(type = "encryptedString")
    private volatile String name;

    @Type(type = "encryptedString")
    private volatile String emailAddress;

    @Type(type = "encryptedString")
    private volatile String mailingAddress;

    @Type(type = "encryptedString")
    private volatile String country;

    @Type(type = "encryptedString")
    private volatile String telephoneNumber;

    /**
     * Create a new instance
     * 
     * @param version The version that was signed
     * @param name The name of the signatory
     * @param emailAddress The email address of the signatory
     * @param mailingAddress The mailing address of the signatory
     * @param country The country of the signatory
     * @param telephoneNumber The telephone number of the signatory
     */
    public IndividualSignatory(Version version, String name, String emailAddress, String mailingAddress, String country, String telephoneNumber) {
        this.version = version;
        this.signingDate = new Date();
        this.name = name;
        this.emailAddress = emailAddress;
        this.mailingAddress = mailingAddress;
        this.country = country;
        this.telephoneNumber = telephoneNumber;
    }

    /**
     * Returns the synthetic id of the signatory
     * 
     * @return the synthetic id of the signatory
     */
    public final Integer getId() {
        return this.id;
    }

    /**
     * Returns the version that was signed
     * 
     * @return the version that was signed
     */
    public final Version getVersion() {
        return this.version;
    }

    /**
     * Returns the date of the signing
     * 
     * @return the date of the signing
     */
    public final Date getSigningDate() {
        return this.signingDate;
    }

    /**
     * Returns the name of the signatory
     * 
     * @return the name of the signatory
     */
    public final String getName() {
        return this.name;
    }

    /**
     * Returns the email address of the signatory
     * 
     * @return the email address of the signatory
     */
    public final String getEmailAddress() {
        return this.emailAddress;
    }

    /**
     * Returns the mailing address of the signatory
     * 
     * @return the mailing address of the signatory
     */
    public final String getMailingAddress() {
        return this.mailingAddress;
    }

    /**
     * Returns the country of the signatory
     * 
     * @return the country of the signatory
     */
    public final String getCountry() {
        return this.country;
    }

    /**
     * Returns the telephone number of the signatory
     * 
     * @return the telephone number of the signatory
     */
    public final String getTelephoneNumber() {
        return this.telephoneNumber;
    }

    @Override
    public String toString() {
        return "IndividualSignatory [id=" + this.id + ", version=" + this.version + ", signingDate=" + this.signingDate + ", name=" + this.name
            + ", emailAddress=" + this.emailAddress + ", mailingAddress=" + this.mailingAddress + ", country=" + this.country + ", telephoneNumber="
            + this.telephoneNumber + "]";
    }

    /**
     * <b>DO NOT USE</b> Provided for use by ORM
     */
    IndividualSignatory() {
    }

}
