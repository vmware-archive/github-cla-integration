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

import java.util.Arrays;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public final class GitHubUtils {

    private static final MediaType V3 = new MediaType("application", "vnd.github.v3");

    private GitHubUtils() {
    }

    /**
     * Returns an {@link HttpHeaders} with the GitHub V3 {@code Accept} header set
     * 
     * @return an {@link HttpHeaders} with the GitHub V3 {@code Accept} header set
     */
    public static HttpHeaders v3Headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(V3));

        return headers;
    }

    /**
     * Returns an {@link HttpEntity} with the GitHub V3 headers set
     * 
     * @return an {@link HttpEntity} with the GitHub V3 headers set
     * @see #v3Headers()
     */
    public static HttpEntity<?> v3HttpEntity() {
        return new HttpEntity<>(v3Headers());
    }
}
