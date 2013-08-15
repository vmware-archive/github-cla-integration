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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.gopivotal.cla.github.Email;
import com.gopivotal.cla.github.GitHubClient;

@Component
final class AdminEmailDomainFilter extends GenericFilterBean {

    private final String[] adminEmailDomains;

    private final GitHubClient gitHubClient;

    @Autowired
    AdminEmailDomainFilter(@Value("#{@adminEmailDomains}") String[] adminEmailDomains, GitHubClient gitHubClient) {
        this.adminEmailDomains = adminEmailDomains;
        this.gitHubClient = gitHubClient;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!isValidAdminUser()) {
            throw new AccessDeniedException("Not a valid administrative user");
        }

        chain.doFilter(request, response);
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
