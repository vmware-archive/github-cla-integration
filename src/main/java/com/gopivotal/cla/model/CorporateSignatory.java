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

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.Type;

/**
 * A class representing a corporate signatory
 */
@Entity
@DiscriminatorValue("corporate")
public final class CorporateSignatory extends IndividualSignatory {

    @Type(type = "encryptedString")
    private volatile String company;

    @Type(type = "encryptedString")
    private volatile String title;

    /**
     * Create a new instance
     * 
     * @param version The version that was signed
     * @param name The name of the contact
     * @param emailAddress The email address of the contact
     * @param mailingAddress The mailing address of the contact
     * @param country The country of the contact
     * @param telephoneNumber The telephone number of the contact
     * @param company The name of the company
     * @param title The title of the contact
     */
    public CorporateSignatory(Version version, String name, String emailAddress, String mailingAddress, String country, String telephoneNumber,
        String company, String title) {
        super(version, name, emailAddress, mailingAddress, country, telephoneNumber);
        this.company = company;
        this.title = title;
    }

    /**
     * Returns the name of the company
     * 
     * @return the name of the company
     */
    public String getCompany() {
        return this.company;
    }

    /**
     * Returns the title of the contact
     * 
     * @return the title of the contact
     */
    public String getTitle() {
        return this.title;
    }

    @Override
    public String toString() {
        return "CorporateSignatory [company=" + this.company + ", title=" + this.title + ", getId()=" + this.getId() + ", getVersion()="
            + this.getVersion() + ", getSigningDate()=" + this.getSigningDate() + ", getName()=" + this.getName() + ", getEmailAddress()="
            + this.getEmailAddress() + ", getMailingAddress()=" + this.getMailingAddress() + ", getCountry()=" + this.getCountry()
            + ", getTelephoneNumber()=" + this.getTelephoneNumber() + "]";
    }

    /**
     * <b>DO NOT USE</b> Provided for use by ORM
     */
    CorporateSignatory() {
    }

}
