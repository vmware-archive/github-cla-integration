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

package com.gopivotal.cla.github;

/**
 * A GitHub Organization
 */
public interface Organization extends Comparable<Organization> {

    /**
     * Returns the name of the organization
     * 
     * @return the name of the organization
     */
    String getName();

    /**
     * Returns the repositories in this organization
     * 
     * @return the repositories in this organization
     */
    Repositories getRepositories();
}
