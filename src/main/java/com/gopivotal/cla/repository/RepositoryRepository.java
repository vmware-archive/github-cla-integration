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

package com.gopivotal.cla.repository;

import java.util.SortedSet;

import com.gopivotal.cla.Repository;

/**
 * Data access method for {@link Repository}s
 */
public interface RepositoryRepository {

    /**
     * Returns all of the {@link Repository}s in the repository
     * 
     * @return all of the repositories in the repository
     */
    SortedSet<Repository> find();

    /**
     * Create a new {@link Repository}
     * 
     * @param name The name of the repository
     * @param agreementId The id of the related agreement
     * @param accessToken The access token to use when accessing the repository
     * @return The newly created repository
     */
    Repository create(String name, Long agreementId, String accessToken);

    /**
     * Read a {@link Repository} identified by its {@code id}
     * 
     * @param id The id of the repository
     * @return The repository
     * @throws EmptyResultDataAccessException if no repository with {@code id} exists
     */
    Repository read(Long id);
}
