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

package com.gopivotal.cla.github;

import java.util.Map;

/**
 * A GitHub email
 */
@SuppressWarnings("rawtypes")
public final class Email extends AbstractGitHubType<Map> implements Comparable<Email> {

    private volatile String address;

    private volatile Boolean primary;

    private volatile Boolean verified;

    /**
     * Creates a new pre-initalized instance
     * 
     * @param address The address
     * @param primary Whether the email is primary
     * @param verified Whether the email is verified
     */
    public Email(String address, Boolean primary, Boolean verified) {
        super(null, null);
        this.address = address;
        this.primary = primary;
        this.verified = verified;
    }

    Email(Map raw) {
        super(null, Map.class);
        initialize(raw);
    }

    @Override
    void initialize(Map raw) {
        this.address = getString("email", raw);
        this.primary = getBoolean("primary", raw);
        this.verified = getBoolean("verified", raw);
    }

    /**
     * Returns the address
     * 
     * @return the address
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Returns whether the email is primary
     * 
     * @return whether the email is primary
     */
    public Boolean isPrimary() {
        return this.primary;
    }

    /**
     * Returns whether the email is verified
     * 
     * @return whether the email is verified
     */
    public Boolean isVerified() {
        return this.verified;
    }

    @Override
    public int compareTo(Email o) {
        return this.address.compareToIgnoreCase(o.getAddress());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + this.address.toLowerCase().hashCode();
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
        if (!(obj instanceof Email)) {
            return false;
        }
        Email other = (Email) obj;
        if (!this.address.equalsIgnoreCase(other.getAddress())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Email [address=" + this.address + ", primary=" + this.primary + ", verified=" + this.verified + "]";
    }

}
