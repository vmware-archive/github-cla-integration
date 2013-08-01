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

package com.nebhale.cla.github;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

@Component
final class StandardGitHubRestOperations implements GitHubRestOperations {

    private static final String BASE_URI = "https://api.github.com";

    private static final Pattern NEXT_LINK = Pattern.compile("<(.*)>; rel=\"next\"");

    private final RestOperations restOperations;

    @Autowired
    StandardGitHubRestOperations(RestOperations restOperations) {
        this.restOperations = restOperations;
    }

    @Override
    public <T> T getForObject(String path, Class<T> responseType, Object... uriVariables) {
        return paginatedExchange(path, HttpMethod.GET, getV2Entity(), responseType, uriVariables);
    }

    @Override
    public <T> T getForObjectV3(String path, Class<T> responseType, Object... uriVariables) {
        return paginatedExchange(path, HttpMethod.GET, getV3Entity(), responseType, uriVariables);
    }

    @Override
    public <T> T postForObject(String path, Object request, Class<T> responseType, Object... uriVariables) {
        return paginatedExchange(path, HttpMethod.POST, getV2Entity(request), responseType, uriVariables);
    }

    @SuppressWarnings("unchecked")
    private <T, U> T paginatedExchange(String path, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) {
        ResponseEntity<T> response = exchange(path, method, requestEntity, responseType, uriVariables);
        T body = response.getBody();
        String nextUri = getNextUri(response.getHeaders().get("Link"));

        if (body instanceof Collection) {
            while (nextUri != null) {
                response = this.restOperations.exchange(nextUri, method, requestEntity, responseType, uriVariables);
                ((Collection<U>) body).addAll((Collection<U>) response.getBody());
                nextUri = getNextUri(response.getHeaders().get("Link"));
            }
        }

        return body;
    }

    private <T> ResponseEntity<T> exchange(String path, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) {
        return this.restOperations.exchange(BASE_URI + path, method, requestEntity, responseType, uriVariables);
    }

    private String getNextUri(List<String> links) {
        if (links == null) {
            return null;
        }

        for (String link : links) {
            Matcher matcher = NEXT_LINK.matcher(link);

            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        return null;
    }

    private HttpEntity<?> getV2Entity() {
        HttpHeaders headers = new HttpHeaders();
        return new HttpEntity<>(headers);
    }

    private <T> HttpEntity<?> getV2Entity(T body) {
        HttpHeaders headers = new HttpHeaders();
        return new HttpEntity<>(body, headers);
    }

    private HttpEntity<?> getV3Entity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType("application", "vnd.github.v3")));
        return new HttpEntity<>(headers);
    }
}
