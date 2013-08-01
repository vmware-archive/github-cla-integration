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

package com.nebhale.cla.web.security;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.nebhale.cla.github.GitHubRestOperations;

final class OAuth2SsoFilter extends AbstractAuthenticationProcessingFilter {

    private final String[] adminEmailDomains;

    private final GitHubRestOperations gitHubRestOperations;

    OAuth2SsoFilter(String[] adminEmailDomains, String defaultFilterProcessesUrl, GitHubRestOperations gitHubRestOperations) {
        super(defaultFilterProcessesUrl);
        this.adminEmailDomains = adminEmailDomains;
        this.gitHubRestOperations = gitHubRestOperations;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException,
        IOException, ServletException {

        if (!isValidAdminUser()) {
            throw new BadCredentialsException("Not a valid administrative user");
        }

        Map<String, Object> userInfo = this.gitHubRestOperations.getForObject("/user", Map.class);
        AdminUser adminUser = new AdminUser((String) userInfo.get("login"));

        return new UsernamePasswordAuthenticationToken(adminUser, null, adminUser.getAuthorities());
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

    @SuppressWarnings("unchecked")
    private boolean isValidAdminUser() {
        Set<Map<String, Object>> emails = this.gitHubRestOperations.getForObjectV3("/user/emails", Set.class);

        for (Map<String, Object> email : emails) {
            if (isVerified(email) && isValidAdminEmailDomain(email)) {
                return true;
            }
        }

        return false;
    }

    private boolean isValidAdminEmailDomain(Map<String, Object> email) {
        String address = (String) email.get("email");

        for (String adminEmailDomain : this.adminEmailDomains) {
            if (address.endsWith(adminEmailDomain)) {
                return true;
            }
        }

        return false;
    }

    private boolean isVerified(Map<String, Object> email) {
        return (boolean) email.get("verified");
    }

}
