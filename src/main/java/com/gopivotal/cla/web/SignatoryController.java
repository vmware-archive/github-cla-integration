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

import java.util.Arrays;
import java.util.Set;
import java.util.SortedSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gopivotal.cla.LinkedRepository;
import com.gopivotal.cla.Version;
import com.gopivotal.cla.github.Email;
import com.gopivotal.cla.github.GitHubClient;
import com.gopivotal.cla.repository.LinkedRepositoryRepository;
import com.gopivotal.cla.repository.VersionRepository;
import com.gopivotal.cla.util.Sets;

@Controller
final class SignatoryController extends AbstractController {

    private static final Pattern DOMAIN = Pattern.compile(".+(@.+)");

    private final GitHubClient gitHubClient;

    private final LinkedRepositoryRepository linkedRepositoryRepository;

    private final VersionRepository versionRepository;

    @Autowired
    SignatoryController(GitHubClient gitHubClient, LinkedRepositoryRepository linkedRepositoryRepository, VersionRepository versionRepository) {
        super(gitHubClient);
        this.gitHubClient = gitHubClient;
        this.linkedRepositoryRepository = linkedRepositoryRepository;
        this.versionRepository = versionRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "{organization}/{repository}")
    String readRepository(@PathVariable String organization, @PathVariable String repository, ModelMap model) {
        model.put("repository", this.linkedRepositoryRepository.read(organization, repository));

        return "repository";
    }

    @RequestMapping(method = RequestMethod.GET, value = "{organization}/{repository}/individual")
    String readIndividual(@PathVariable String organization, @PathVariable String repository, ModelMap model) {
        populateAgreementModel(organization, repository, model);
        model.put("emails", verifiedEmails(this.gitHubClient.getEmails()));

        return "individual";
    }

    @RequestMapping(method = RequestMethod.POST, value = "{organization}/{repository}/individual")
    String createIndividual(@RequestParam String name, @RequestParam String email, @RequestParam String mailingAddress, @RequestParam String country,
        @RequestParam String telephoneNumber, @RequestParam("contribution") String[] contributions, @RequestParam String versionId) {

        System.out.println(name);
        System.out.println(email);
        System.out.println(mailingAddress);
        System.out.println(country);
        System.out.println(telephoneNumber);
        System.out.println(Arrays.toString(contributions));
        System.out.println(versionId);

        return "redirect:/";
    }

    @RequestMapping(method = RequestMethod.GET, value = "{organization}/{repository}/corporate")
    String readCorporate(@PathVariable String organization, @PathVariable String repository, ModelMap model) {
        populateAgreementModel(organization, repository, model);
        SortedSet<Email> verifiedEmails = verifiedEmails(this.gitHubClient.getEmails());

        model.put("emails", verifiedEmails);
        model.put("domains", domains(verifiedEmails));

        return "corporate";
    }

    @RequestMapping(method = RequestMethod.POST, value = "{organization}/{repository}/corporate")
    String createCorporate(@RequestParam String company, @RequestParam String name, @RequestParam String title, @RequestParam String email,
        @RequestParam String mailingAddress, @RequestParam String country, @RequestParam String telephoneNumber,
        @RequestParam("contribution") String[] contributions, @RequestParam String versionId) {

        System.out.println(company);
        System.out.println(name);
        System.out.println(title);
        System.out.println(email);
        System.out.println(mailingAddress);
        System.out.println(country);
        System.out.println(telephoneNumber);
        System.out.println(Arrays.toString(contributions));
        System.out.println(versionId);

        return "redirect:/";
    }

    private void populateAgreementModel(String organization, String repository, ModelMap model) {
        LinkedRepository linkedRepository = this.linkedRepositoryRepository.read(organization, repository);
        Version version = this.versionRepository.find(linkedRepository.getAgreement().getId()).last();

        model.put("repository", linkedRepository);
        model.put("version", version);
    }

    private SortedSet<Email> verifiedEmails(Set<Email> emails) {
        SortedSet<Email> verifiedEmails = Sets.asSortedSet();

        for (Email email : emails) {
            if (email.isVerified()) {
                verifiedEmails.add(email);
            }
        }

        return verifiedEmails;
    }

    private SortedSet<String> domains(Set<Email> emails) {
        SortedSet<String> domains = Sets.asSortedSet();

        for (Email email : emails) {
            Matcher matcher = DOMAIN.matcher(email.getAddress());
            if (matcher.find()) {
                domains.add(matcher.group(1));
            }
        }

        return domains;
    }
}
