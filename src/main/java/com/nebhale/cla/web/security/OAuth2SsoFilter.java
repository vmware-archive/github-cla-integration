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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.web.client.RestOperations;

final class OAuth2SsoFilter extends AbstractAuthenticationProcessingFilter {

    private final RestOperations restOperations;

    OAuth2SsoFilter(String defaultFilterProcessesUrl, RestOperations restOperations) {
        super(defaultFilterProcessesUrl);
        this.restOperations = restOperations;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException,
        IOException, ServletException {

        Map<String, Object> userInfo = this.restOperations.getForObject("https://api.github.com/user", Map.class);
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

}
