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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gopivotal.cla.github.GitHubClient;
import com.gopivotal.cla.github.Organization;
import com.gopivotal.cla.github.Repository;
import com.gopivotal.cla.model.Agreement;
import com.gopivotal.cla.model.LinkedRepository;
import com.gopivotal.cla.repository.AgreementRepository;
import com.gopivotal.cla.repository.LinkedRepositoryRepository;

@Controller
@RequestMapping("/repositories")
final class RepositoriesController extends AbstractController {

    private static final String DEFAULT_SCHEME = "http";

    private static final String SECURE_SCHEME = "https";

    private static final String HEADER_HOST = "host";

    private final GitHubClient gitHubClient;

    private final AgreementRepository agreementRepository;

    private final LinkedRepositoryRepository linkedRepositoryRepository;

    @Autowired
    RepositoriesController(GitHubClient gitHubClient, AgreementRepository agreementRepository, LinkedRepositoryRepository linkedRepositoryRepository) {
        super(gitHubClient);
        this.gitHubClient = gitHubClient;
        this.agreementRepository = agreementRepository;
        this.linkedRepositoryRepository = linkedRepositoryRepository;
    }

    @Transactional(readOnly = true)
    @RequestMapping(method = RequestMethod.GET, value = "")
    String listRepositories(ModelMap model) {
        List<LinkedRepository> linkedRepositories = this.linkedRepositoryRepository.findAll(new Sort("name"));
        SortedSet<Repository> adminRepositories = getAdminRepositories();
        List<Agreement> candidateAgreements = this.agreementRepository.findAll(new Sort("name"));

        model.put("linkedRepositories", filterLinkedRepositories(linkedRepositories, adminRepositories));
        model.put("candidateAgreements", candidateAgreements);
        model.put("candidateRepositories", filterAdminRepositories(adminRepositories, linkedRepositories));

        return "repositories";
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, value = "")
    String createLinkedRepository(@RequestParam Agreement agreement, @RequestParam("repository") String name) {
        this.linkedRepositoryRepository.save(new LinkedRepository(agreement, name, this.gitHubClient.getAccessToken()));

        return "redirect:/repositories";
    }

    @ModelAttribute("hrefPrefix")
    final String hrefPrefix(HttpServletRequest httpServletRequest) {
        if (httpServletRequest != null) {
            String scheme = getScheme(httpServletRequest);
            String host = httpServletRequest.getHeader(HEADER_HOST);

            return String.format("%s://%s", scheme, host);
        }
        return "";
    }

    private static String getScheme(HttpServletRequest httpServletRequest) {
        return httpServletRequest.isSecure() ? SECURE_SCHEME : DEFAULT_SCHEME;
    }

    private SortedSet<Repository> getAdminRepositories() {
        SortedSet<Repository> adminRepositories = new TreeSet<>();

        for (Organization organization : this.gitHubClient.getUser().getOrganizations()) {
            for (Repository repository : organization.getRepositories()) {
                if (repository.getPermissions().isAdmin()) {
                    adminRepositories.add(repository);
                }
            }
        }

        adminRepositories.addAll(this.gitHubClient.getUser().getRepositories());

        return adminRepositories;
    }

    private SortedSet<Repository> filterAdminRepositories(SortedSet<Repository> adminRepositories, List<LinkedRepository> linkedRepositories) {
        Set<String> linkedRepositoryNames = new HashSet<>();
        for (LinkedRepository linkedRepository : linkedRepositories) {
            linkedRepositoryNames.add(linkedRepository.getName().toLowerCase());
        }

        SortedSet<Repository> filteredAdminRepositories = new TreeSet<>();

        for (Repository adminRepository : adminRepositories) {
            if (!linkedRepositoryNames.contains(adminRepository.getFullName().toLowerCase())) {
                filteredAdminRepositories.add(adminRepository);
            }
        }

        return filteredAdminRepositories;
    }

    private List<LinkedRepository> filterLinkedRepositories(List<LinkedRepository> linkedRepositories, SortedSet<Repository> adminRepositories) {
        Set<String> adminRepositoryNames = new HashSet<>();
        for (Repository adminRepository : adminRepositories) {
            adminRepositoryNames.add(adminRepository.getFullName().toLowerCase());
        }

        List<LinkedRepository> candidateRepositories = new ArrayList<>();

        for (LinkedRepository linkedRepository : linkedRepositories) {
            if (adminRepositoryNames.contains(linkedRepository.getName().toLowerCase())) {
                candidateRepositories.add(linkedRepository);
            }
        }

        return candidateRepositories;
    }
}
