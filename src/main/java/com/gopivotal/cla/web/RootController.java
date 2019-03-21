/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gopivotal.cla.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gopivotal.cla.repository.LinkedRepositoryRepository;

@Controller
final class RootController {

    private final LinkedRepositoryRepository linkedRepositoryRepository;

    @Autowired
    RootController(LinkedRepositoryRepository linkedRepositoryRepository) {
        this.linkedRepositoryRepository = linkedRepositoryRepository;
    }

    @Transactional(readOnly = true)
    @RequestMapping(method = RequestMethod.GET, value = "")
    String index(Model model) {
        model.addAttribute("linkedRepositories", this.linkedRepositoryRepository.findAll(new Sort("name")));

        return "index";
    }
}
