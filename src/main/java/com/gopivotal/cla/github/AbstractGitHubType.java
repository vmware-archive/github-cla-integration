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

import java.util.Map;

abstract class AbstractGitHubType<T> {

    private final String url;

    private final Class<T> responseType;

    protected AbstractGitHubType(String url, Class<T> responseType) {
        this.url = url;
        this.responseType = responseType;
    }

    protected final String getUrl() {
        return this.url;
    }

    protected final Class<T> getResponseType() {
        return this.responseType;
    }

    @SuppressWarnings("rawtypes")
    protected static final Boolean getBoolean(String key, Map raw) {
        return (Boolean) raw.get(key);
    }

    @SuppressWarnings("rawtypes")
    protected static final String getString(String key, Map raw) {
        return (String) raw.get(key);
    }

    @SuppressWarnings("rawtypes")
    protected static final Map getMap(String key, Map raw) {
        return (Map) raw.get(key);
    }

    abstract void initialize(T raw);

}
