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

import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

final class AdminUser extends User {

    private static final long serialVersionUID = -5949420142556127344L;

    private static final List<GrantedAuthority> ADMIN_AUTHORIES = Arrays.<GrantedAuthority> asList(new SimpleGrantedAuthority("ROLE_ADMIN"));

    AdminUser(String login) {
        super(login, "unused", ADMIN_AUTHORIES);
    }
}
