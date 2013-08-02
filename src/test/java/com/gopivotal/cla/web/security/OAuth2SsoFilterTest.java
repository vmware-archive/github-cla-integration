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

package com.gopivotal.cla.web.security;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashSet;

import javax.servlet.ServletException;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;

import com.gopivotal.cla.github.Email;
import com.gopivotal.cla.github.Emails;
import com.gopivotal.cla.github.GitHubClient;
import com.gopivotal.cla.github.User;

public final class OAuth2SsoFilterTest {

    private final GitHubClient gitHubClient = mock(GitHubClient.class);

    private final OAuth2SsoFilter oAuth2SsoFilter = new OAuth2SsoFilter(new String[] { "test.domain" }, "test-url", this.gitHubClient);

    @Test
    public void attemptAuthentication() throws IOException, ServletException {
        Emails emails = new StubEmails();
        when(this.gitHubClient.getEmails()).thenReturn(emails);
        emails.add(new StubEmail("email@test.domain", false, true));

        User user = mock(User.class);
        when(this.gitHubClient.getUser()).thenReturn(user);

        Authentication authentication = this.oAuth2SsoFilter.attemptAuthentication(new MockHttpServletRequest(), new MockHttpServletResponse());

        assertEquals(user, authentication.getPrincipal());
    }

    @Test(expected = BadCredentialsException.class)
    public void attemptAuthenticationNonVerifiedEmail() throws IOException, ServletException {
        Emails emails = new StubEmails();
        when(this.gitHubClient.getEmails()).thenReturn(emails);
        emails.add(new StubEmail("email@test.domain", false, false));

        User user = mock(User.class);
        when(this.gitHubClient.getUser()).thenReturn(user);

        this.oAuth2SsoFilter.attemptAuthentication(new MockHttpServletRequest(), new MockHttpServletResponse());
    }

    @Test(expected = BadCredentialsException.class)
    public void attemptAuthenticationNonValidAdminDomain() throws IOException, ServletException {

        Emails emails = new StubEmails();
        when(this.gitHubClient.getEmails()).thenReturn(emails);
        emails.add(new StubEmail("email@other.domain", false, true));

        User user = mock(User.class);
        when(this.gitHubClient.getUser()).thenReturn(user);

        this.oAuth2SsoFilter.attemptAuthentication(new MockHttpServletRequest(), new MockHttpServletResponse());
    }

    @Test
    public void unsuccessfulAuthentication() throws IOException, ServletException {
        this.oAuth2SsoFilter.unsuccessfulAuthentication(new MockHttpServletRequest(), new MockHttpServletResponse(),
            new BadCredentialsException(null));
    }

    @Test(expected = AccessTokenRequiredException.class)
    public void unsuccessfulAuthenticationWithAccessTokenRequiredException() throws IOException, ServletException {
        this.oAuth2SsoFilter.unsuccessfulAuthentication(new MockHttpServletRequest(), new MockHttpServletResponse(),
            new AccessTokenRequiredException(null));
    }

    private static final class StubEmails extends HashSet<Email> implements Emails {

        private static final long serialVersionUID = 5555232529640927085L;

    }

    private static final class StubEmail implements Email {

        private final String address;

        private final Boolean primary;

        private final Boolean verified;

        private StubEmail(String address, Boolean primary, Boolean verified) {
            this.address = address;
            this.primary = primary;
            this.verified = verified;
        }

        @Override
        public int compareTo(Email o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String getAddress() {
            return this.address;
        }

        @Override
        public Boolean isPrimary() {
            return this.primary;
        }

        @Override
        public Boolean isVerified() {
            return this.verified;
        }

    }

}
