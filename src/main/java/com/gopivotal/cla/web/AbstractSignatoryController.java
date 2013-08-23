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

import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.ui.Model;

import com.gopivotal.cla.github.Email;
import com.gopivotal.cla.github.GitHubClient;
import com.gopivotal.cla.model.LinkedRepository;
import com.gopivotal.cla.model.Version;
import com.gopivotal.cla.repository.LinkedRepositoryRepository;
import com.gopivotal.cla.repository.VersionRepository;

abstract class AbstractSignatoryController extends AbstractController {

    private final GitHubClient gitHubClient;

    private final LinkedRepositoryRepository linkedRepositoryRepository;

    private final VersionRepository versionRepository;

    protected AbstractSignatoryController(GitHubClient gitHubClient, LinkedRepositoryRepository linkedRepositoryRepository,
        VersionRepository versionRepository) {
        super(gitHubClient);
        this.gitHubClient = gitHubClient;
        this.linkedRepositoryRepository = linkedRepositoryRepository;
        this.versionRepository = versionRepository;
    }

    protected final void populateAgreementModel(String organization, String repository, Model model) {
        LinkedRepository linkedRepository = this.linkedRepositoryRepository.findByOrganizationAndRepository(organization, repository);
        Version version = this.versionRepository.findByAgreement(linkedRepository.getAgreement(), new Sort(Direction.DESC, "name")).get(0);

        model //
        .addAttribute("emails", verifiedEmails()) //
        .addAttribute("repository", linkedRepository) //
        .addAttribute("version", version);
    }

    protected final SortedSet<Email> verifiedEmails() {
        SortedSet<Email> verifiedEmails = new TreeSet<>();

        for (Email email : this.gitHubClient.getEmails()) {
            if (email.isVerified()) {
                verifiedEmails.add(email);
            }
        }

        return verifiedEmails;
    }

}