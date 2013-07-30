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

package com.nebhale.cla.repository;

import java.util.SortedSet;

import com.nebhale.cla.Version;

/**
 * Data access methods for {@link Version}s
 */
public interface VersionRepository {

    /**
     * Returns all of the {@link Version}s in the repository that are related to an {@link Agreement}
     * 
     * @param agreementId The id of the agreement
     * @return all of versions in the repository that are related to an agreement
     */
    SortedSet<Version> find(Long agreementId);

    /**
     * Create a new {@link Version}
     * 
     * @param agreementId The id of the agreement
     * @param version The version of the agreement
     * @param content The content of the version
     * @return The newly created version
     */
    Version create(Long agreementId, String version, String content);

    /**
     * Read a {@link Version} identified by its {@code id}
     * 
     * @param id The id of the version
     * @return The version
     * @throws EmptyResultDataAccessException if no version with {@code id} exists
     */
    Version read(Long id);
}
