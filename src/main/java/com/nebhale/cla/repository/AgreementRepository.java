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

import com.nebhale.cla.Agreement;
import com.nebhale.cla.Type;

/**
 * Data access method for {@link Agreement}s
 */
public interface AgreementRepository {

    /**
     * Returns all of the {@link Agreement}s in the repository
     * 
     * @return all of the agreements in the repository
     */
    SortedSet<Agreement> find();

    /**
     * Create a new {@link Agreement}
     * 
     * @param type The type of agreement
     * @param name The name of the agreement
     * @return The newly created agreement
     */
    Agreement create(Type type, String name);

    /**
     * Read an {@link Agreement} identified by its {@code id}
     * 
     * @param id The id of the agreement
     * @return The agreement
     * @throws EmptyResultDataAccessException if no agreement with {@code id} exists
     */
    Agreement read(Long id);
}
