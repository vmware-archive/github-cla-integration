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

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gopivotal.cla.Repository;
import com.gopivotal.cla.github.GitHubRepositories;
import com.gopivotal.cla.github.GitHubRestOperations;
import com.gopivotal.cla.repository.AgreementRepository;
import com.gopivotal.cla.repository.RepositoryRepository;

@Controller
@RequestMapping("/repositories")
final class RepositoriesController extends AbstractController {

    private final AgreementRepository agreementRepository;

    private final GitHubRepositories gitHubRepositories;

    private final RepositoryRepository repositoryRepository;

    private final OAuth2RestOperations oAuth2RestOperations;

    @Autowired
    RepositoriesController(GitHubRestOperations gitHubRestOperations, AgreementRepository agreementRepository, GitHubRepositories gitHubRepositories,
        RepositoryRepository repositoryRepository, OAuth2RestOperations oAuth2RestOperations) {
        super(gitHubRestOperations);
        this.agreementRepository = agreementRepository;
        this.gitHubRepositories = gitHubRepositories;
        this.repositoryRepository = repositoryRepository;
        this.oAuth2RestOperations = oAuth2RestOperations;
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    String listRepositories(ModelMap model) {
        Map<String, String> repositoryMapping = getRepositoryMapping();
        SortedSet<String> adminRepositories = this.gitHubRepositories.getAdminRepositories();
        adminRepositories.removeAll(repositoryMapping.keySet());

        model.put("repositoryMapping", repositoryMapping);
        model.put("candidateAgreements", this.agreementRepository.find());
        model.put("candidateRepositories", adminRepositories);

        return "repositories";
    }

    @RequestMapping(method = RequestMethod.POST, value = "")
    String createRepository(@RequestParam("repository") String name, @RequestParam("agreement") Long agreementId) {
        this.repositoryRepository.create(name, agreementId, this.oAuth2RestOperations.getAccessToken().getValue());

        return "redirect:/repositories";
    }

    private Map<String, String> getRepositoryMapping() {
        Map<String, String> repositoryMapping = new HashMap<>();

        for (Repository repository : this.repositoryRepository.find()) {
            repositoryMapping.put(repository.getName(), this.agreementRepository.read(repository.getAgreementId()).getName());
        }

        return repositoryMapping;
    }
}
