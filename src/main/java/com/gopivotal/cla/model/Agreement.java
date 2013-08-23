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
import javax.persistence.Table;

/**
 * A class representing a specific Contributor License Agreement
 */
@Entity
@Table(name = "agreements")
public final class Agreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private volatile Integer id;

    private volatile String name;

    /**
     * Create a new instance
     * 
     * @param name The name of the agreement
     */
    public Agreement(String name) {
        this.id = this.id;
        this.name = name;
    }

    /**
     * Returns the synthetic id of the agreement
     * 
     * @return the synthetic id of the agreement
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Returns the name of the agreement
     * 
     * @return the name of the agreement
     */
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "Agreement [id=" + this.id + ", name=" + this.name + "]";
    }

    /**
     * <b>DO NOT USE</b> Provided for use by ORM
     */
    Agreement() {
    }

}
