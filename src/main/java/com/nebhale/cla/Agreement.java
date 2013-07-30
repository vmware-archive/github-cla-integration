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
 * A class representing a specific Contributor License Agreement
 */
public final class Agreement implements Comparable<Agreement> {

    private final Long id;

    private final String name;

    private final Type type;

    /**
     * Create a new instance
     * 
     * @param id The synthetic id of the agreement
     * @param type The type of the agreement
     * @param name The name of the agreement
     */
    public Agreement(Long id, Type type, String name) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    /**
     * Returns the synthetic id of the agreement
     * 
     * @return the synthetic id of the agreement
     */
    public Long getId() {
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

    /**
     * Returns the type of the agreement
     * 
     * @return the type of the agreement
     */
    public Type getType() {
        return this.type;
    }

    @Override
    public int compareTo(Agreement o) {
        int compare = this.name.compareTo(o.name);
        if (compare == 0) {
            compare = this.type.compareTo(o.type);
        }

        return compare;
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
        Agreement other = (Agreement) obj;
        if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Agreement [id=" + this.id + ", type=" + this.type + ", name=" + this.name + "]";
    }

}
