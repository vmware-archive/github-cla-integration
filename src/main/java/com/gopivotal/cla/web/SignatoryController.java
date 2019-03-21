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
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gopivotal.cla.github.GitHubClient;
import com.gopivotal.cla.repository.LinkedRepositoryRepository;
import com.gopivotal.cla.repository.VersionRepository;

@Controller
final class SignatoryController extends AbstractSignatoryController {

    private final LinkedRepositoryRepository linkedRepositoryRepository;

    @Autowired
    SignatoryController(GitHubClient gitHubClient, LinkedRepositoryRepository linkedRepositoryRepository, VersionRepository versionRepository) {
        super(gitHubClient, linkedRepositoryRepository, versionRepository);
        this.linkedRepositoryRepository = linkedRepositoryRepository;
    }

    @Transactional(readOnly = true)
    @RequestMapping(method = RequestMethod.GET, value = "/{organization}/{repository}")
    String readRepository(@PathVariable String organization, @PathVariable String repository, Model model) {
        model.addAttribute("repository", this.linkedRepositoryRepository.findByOrganizationAndRepository(organization, repository));

        return "repository";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/confirmation")
    String confirmation() {
        return "confirmation";
    }
}
