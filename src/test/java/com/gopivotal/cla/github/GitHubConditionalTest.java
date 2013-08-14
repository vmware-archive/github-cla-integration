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

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;

import com.gopivotal.cla.util.Sets;

@SuppressWarnings("rawtypes")
public final class GitHubConditionalTest {

    private static final HttpEntity<?> REQUEST_ENTITY;
    static {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType("application", "vnd.github.v3")));
        REQUEST_ENTITY = new HttpEntity<>(headers);
    }

    private static final String LINK_URL = "test-link-url";

    private static final String URL = "test-url";

    private final RestOperations restOperations = mock(RestOperations.class);

    private final StubGitHubType gitHubType = new StubGitHubType(URL);

    @Before
    public void injectRestOperations() {
        GitHubConditional.aspectOf().setRestOperations(this.restOperations);
    }

    @Test
    public void noLinks() {
        ResponseEntity<Set> response = new ResponseEntity<Set>(Sets.asSet(), HttpStatus.OK);
        when(this.restOperations.exchange(URL, HttpMethod.GET, REQUEST_ENTITY, Set.class)).thenReturn(response);

        this.gitHubType.getTrigger();

        assertTrue(this.gitHubType.initializedCalled);
    }

    @Test
    public void noNextLink() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Link", "<" + URL + ">; rel=\"first\"");
        ResponseEntity<Set> response = new ResponseEntity<Set>(Sets.asSet(), headers, HttpStatus.OK);
        when(this.restOperations.exchange(URL, HttpMethod.GET, REQUEST_ENTITY, Set.class)).thenReturn(response);

        this.gitHubType.getTrigger();

        assertTrue(this.gitHubType.initializedCalled);
    }

    @Test
    public void withNextLink() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Link", "<" + URL + ">; rel=\"first\"");
        headers.add("Link", "<" + LINK_URL + ">; rel=\"next\"");
        ResponseEntity<Set> response1 = new ResponseEntity<Set>(Sets.asSet(), headers, HttpStatus.OK);
        when(this.restOperations.exchange(URL, HttpMethod.GET, REQUEST_ENTITY, Set.class)).thenReturn(response1);

        ResponseEntity<Set> response2 = new ResponseEntity<Set>(Sets.asSet(), HttpStatus.OK);
        when(this.restOperations.exchange(LINK_URL, HttpMethod.GET, REQUEST_ENTITY, Set.class)).thenReturn(response2);

        this.gitHubType.getTrigger();

        assertTrue(this.gitHubType.initializedCalled);
    }

    @Test
    public void notCollection() {
        ResponseEntity<String> response = new ResponseEntity<String>("", HttpStatus.OK);
        when(this.restOperations.exchange(URL, HttpMethod.GET, REQUEST_ENTITY, String.class)).thenReturn(response);

        StubNonCollectionGitHubType nonCollectionGitHubType = new StubNonCollectionGitHubType(URL);

        nonCollectionGitHubType.getTrigger();

        assertTrue(nonCollectionGitHubType.initializedCalled);
    }

    private static final class StubGitHubType extends AbstractGitHubType<Set> {

        private volatile Object trigger;

        private volatile boolean initializedCalled = false;

        private StubGitHubType(String url) {
            super(url, Set.class);
        }

        @Override
        void initialize(Set raw) {
            this.initializedCalled = true;
        }

        public Object getTrigger() {
            return this.trigger;
        };

    }

    private static final class StubNonCollectionGitHubType extends AbstractGitHubType<String> {

        private volatile Object trigger;

        private volatile boolean initializedCalled = false;

        private StubNonCollectionGitHubType(String url) {
            super(url, String.class);
        }

        @Override
        void initialize(String raw) {
            this.initializedCalled = true;
        }

        public Object getTrigger() {
            return this.trigger;
        };

    }

}
