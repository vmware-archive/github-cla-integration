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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Set;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;

import com.gopivotal.cla.github.StandardGitHubRestOperations;
import com.gopivotal.cla.util.Sets;

public final class StandardGitHubRestOperationsTest {

    private final RestOperations restOperations = mock(RestOperations.class);

    private final StandardGitHubRestOperations gitHubRestOperations = new StandardGitHubRestOperations(this.restOperations);

    @Test
    public void getForObject() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> responseEntity = new ResponseEntity<>("test-result", responseHeaders, HttpStatus.OK);

        when(this.restOperations.exchange("https://api.github.com/test-path", HttpMethod.GET, requestEntity, String.class, "uri-variable")).thenReturn(
            responseEntity);

        String result = this.gitHubRestOperations.getForObject("/test-path", String.class, "uri-variable");

        assertEquals("test-result", result);
    }

    @Test
    public void getForObjectV3() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType("application", "vnd.github.v3")));
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> responseEntity = new ResponseEntity<>("test-result", responseHeaders, HttpStatus.OK);

        when(this.restOperations.exchange("https://api.github.com/test-path", HttpMethod.GET, requestEntity, String.class, "uri-variable")).thenReturn(
            responseEntity);

        String result = this.gitHubRestOperations.getForObjectV3("/test-path", String.class, "uri-variable");

        assertEquals("test-result", result);
    }

    @Test
    public void postForObject() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>("test-body", headers);

        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<String> responseEntity = new ResponseEntity<>("test-result", responseHeaders, HttpStatus.OK);

        when(this.restOperations.exchange("https://api.github.com/test-path", HttpMethod.POST, requestEntity, String.class, "uri-variable")).thenReturn(
            responseEntity);

        String result = this.gitHubRestOperations.postForObject("/test-path", "test-body", String.class, "uri-variable");

        assertEquals("test-result", result);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void collectionNoLinks() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        HttpHeaders responseHeaders = new HttpHeaders();
        ResponseEntity<Set> responseEntity = new ResponseEntity<Set>(Sets.asSet(), responseHeaders, HttpStatus.OK);

        when(this.restOperations.exchange("https://api.github.com/test-path", HttpMethod.GET, requestEntity, Set.class, "uri-variable")).thenReturn(
            responseEntity);

        Set result = this.gitHubRestOperations.getForObject("/test-path", Set.class, "uri-variable");

        assertEquals(Sets.asSet(), result);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void collectionNoNextLinks() {
        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(requestHeaders);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", "<https://api.github.com/test-link-path>; rel=\"last\"");
        ResponseEntity<Set> responseEntity = new ResponseEntity<Set>(Sets.asSet(), responseHeaders, HttpStatus.OK);

        when(this.restOperations.exchange("https://api.github.com/test-path", HttpMethod.GET, requestEntity, Set.class, "uri-variable")).thenReturn(
            responseEntity);

        Set result = this.gitHubRestOperations.getForObject("/test-path", Set.class, "uri-variable");

        assertEquals(Sets.asSet(), result);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void collectionNextLinks() {
        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity<?> requestEntity = new HttpEntity<>(requestHeaders);

        Set<String> response1 = Sets.asSet("value1");
        HttpHeaders responseHeaders1 = new HttpHeaders();
        responseHeaders1.add("Link", "<https://api.github.com/test-link-path>; rel=\"next\"");
        ResponseEntity<Set> responseEntity1 = new ResponseEntity<Set>(response1, responseHeaders1, HttpStatus.OK);

        Set<String> response2 = Sets.asSet("value2");
        HttpHeaders responseHeaders2 = new HttpHeaders();
        ResponseEntity<Set> responseEntity2 = new ResponseEntity<Set>(response2, responseHeaders2, HttpStatus.OK);

        when(this.restOperations.exchange("https://api.github.com/test-path", HttpMethod.GET, requestEntity, Set.class, "uri-variable")).thenReturn(
            responseEntity1);
        when(this.restOperations.exchange("https://api.github.com/test-link-path", HttpMethod.GET, requestEntity, Set.class, "uri-variable")).thenReturn(
            responseEntity2);

        Set result = this.gitHubRestOperations.getForObject("/test-path", Set.class, "uri-variable");

        assertEquals(2, result.size());
    }
}
