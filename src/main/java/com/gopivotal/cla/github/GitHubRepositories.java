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

import java.util.SortedSet;

/**
 * Methods for interacting with the collection of GitHub repositories visible to a user
 */
public interface GitHubRepositories {

    /**
     * Returns the collection of {@code full_name}s for all repositories that this user can administer
     * 
     * @return the collection of {@code full_name}s for all repositories that this user can administer
     */
    SortedSet<String> getAdminRepositories();
}