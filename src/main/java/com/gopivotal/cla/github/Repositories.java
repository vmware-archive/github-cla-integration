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
 * A collection of GitHub repositories
 */
public final class Repositories extends AbstractCollection<Repository> {

    /**
     * Creates a new pre-initialized instance
     */
    public Repositories() {
        super();
    }

    Repositories(String url) {
        super(url);
    }

    @SuppressWarnings("rawtypes")
    @Override
    Repository initialize(Map raw) {
        return new Repository(raw);
    }
}
