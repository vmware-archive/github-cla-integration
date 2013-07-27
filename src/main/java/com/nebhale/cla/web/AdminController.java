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

package com.nebhale.cla.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestOperations;

@Controller
@RequestMapping("/admin")
final class AdminController {

    private final RestOperations restOperations;

    @Autowired
    AdminController(RestOperations restTemplate) {
        this.restOperations = restTemplate;
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    @ResponseBody
    String index() {
        System.out.println("Doing It");
        return this.restOperations.getForObject("https://github.com/user", String.class);
    }

}
