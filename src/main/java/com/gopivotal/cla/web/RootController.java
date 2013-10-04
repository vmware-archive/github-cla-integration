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

package com.gopivotal.cla.web;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collection;

@Controller
final class RootController {

    @RequestMapping(method = RequestMethod.GET, value = "")
    String index() {
        return "index";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/route")
    String route(@AuthenticationPrincipal User user) {
        String destination = hasRole("ADMIN", user.getAuthorities()) ? "admin" : "signatory";
        return String.format("redirect:/%s", destination);
    }

    private Boolean hasRole(String role, Collection<GrantedAuthority> authorities) {
        for (GrantedAuthority authority : authorities) {
            if (String.format("ROLE_%s", role).equals(authority.getAuthority())) {
                return true;
            }
        }

        return false;
    }

}
