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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import com.gopivotal.cla.github.Email;
import com.gopivotal.cla.github.GitHubClient;

final class OAuth2SsoFilter extends AbstractAuthenticationProcessingFilter {

    private final String[] adminEmailDomains;

    private final GitHubClient gitHubClient;

    OAuth2SsoFilter(String[] adminEmailDomains, String defaultFilterProcessesUrl, GitHubClient gitHubClient) {
        super(defaultFilterProcessesUrl);
        this.adminEmailDomains = adminEmailDomains;
        this.gitHubClient = gitHubClient;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException,
        IOException, ServletException {

        if (!isValidAdminUser()) {
            throw new BadCredentialsException("Not a valid administrative user");
        }

        return new PreAuthenticatedAuthenticationToken(this.gitHubClient.getUser(), null);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
        throws IOException, ServletException {
        if (failed instanceof AccessTokenRequiredException) {
            throw failed;
        } else {
            super.unsuccessfulAuthentication(request, response, failed);
        }
    }

    private boolean isValidAdminUser() {
        for (Email email : this.gitHubClient.getEmails()) {
            if (email.isVerified() && isValidAdminEmailDomain(email)) {
                return true;
            }
        }

        return false;
    }

    private boolean isValidAdminEmailDomain(Email email) {
        String address = email.getAddress();

        for (String adminEmailDomain : this.adminEmailDomains) {
            if (address.endsWith(adminEmailDomain)) {
                return true;
            }
        }

        return false;
    }

}
