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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * A class representing a repository that has been linked to an agreement
 */
@Entity
@Table(name = "repositories")
public final class LinkedRepository {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private volatile Integer id;

    @ManyToOne
    @JoinColumn(name = "agreementId")
    private volatile Agreement agreement;

    private volatile String name;

    @Type(type = "encryptedString")
    private volatile String accessToken;

    /**
     * Create a new instance
     * 
     * @param id The synthetic id of the repository
     * @param agreement The agreement the repository is linked to
     * @param name The name of the repository
     * @param accessToken The access token to use to update the repository
     */
    public LinkedRepository(Agreement agreement, String name, String accessToken) {
        this.agreement = agreement;
        this.name = name;
        this.accessToken = accessToken;
    }

    /**
     * Returns the synthetic id of the repository
     * 
     * @return the synthetic id of the repository
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Returns the agreement the repository is linked to
     * 
     * @return the agreement the repository is linked to
     */
    public Agreement getAgreement() {
        return this.agreement;
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
     * Returns the access token to use to update the repository
     * 
     * @return the access token to use to update the repository
     */
    public String getAccessToken() {
        return this.accessToken;
    }

    @Override
    public String toString() {
        return "LinkedRepository [id=" + this.id + ", agreement=" + this.agreement + ", name=" + this.name + ", accessToken=" + this.accessToken
            + "]";
    }

    /**
     * <b>DO NOT USE</b> Provided for use by ORM
     */
    LinkedRepository() {
    }

}
