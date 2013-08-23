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

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gopivotal.cla.github.Email;
import com.gopivotal.cla.github.GitHubClient;
import com.gopivotal.cla.model.Agreement;
import com.gopivotal.cla.model.CorporateSignatory;
import com.gopivotal.cla.model.SignedDomain;
import com.gopivotal.cla.model.Version;
import com.gopivotal.cla.repository.CorporateSignatoryRepository;
import com.gopivotal.cla.repository.LinkedRepositoryRepository;
import com.gopivotal.cla.repository.SignedDomainRepository;
import com.gopivotal.cla.repository.VersionRepository;

@Controller
final class CorporateSignatoryController extends AbstractSignatoryController {

    private static final Pattern DOMAIN = Pattern.compile(".+(@.+)");

    private final CorporateSignatoryRepository corporateSignatoryRepository;

    private final LinkedRepositoryRepository linkedRepositoryRepository;

    private final SignedDomainRepository signedDomainRepository;

    @Autowired
    CorporateSignatoryController(GitHubClient gitHubClient, LinkedRepositoryRepository linkedRepositoryRepository,
        CorporateSignatoryRepository corporateSignatoryRepository, SignedDomainRepository signedDomainRepository, VersionRepository versionRepository) {
        super(gitHubClient, linkedRepositoryRepository, versionRepository);
        this.corporateSignatoryRepository = corporateSignatoryRepository;
        this.linkedRepositoryRepository = linkedRepositoryRepository;
        this.signedDomainRepository = signedDomainRepository;
    }

    @Transactional(readOnly = true)
    @RequestMapping(method = RequestMethod.GET, value = "/{organization}/{repository}/corporate")
    String readCorporate(@PathVariable String organization, @PathVariable String repository, Model model) {
        populateAgreementModel(organization, repository, model);
        model.addAttribute("domains", domains(verifiedEmails()));

        return "corporate";
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, value = "/{organization}/{repository}/corporate")
    String createCorporate(@PathVariable String organization, @PathVariable String repository, @RequestParam String company,
        @RequestParam String name, @RequestParam String title, @RequestParam("email") String emailAddress, @RequestParam String mailingAddress,
        @RequestParam String country, @RequestParam String telephoneNumber, @RequestParam("contribution") String[] domains,
        @RequestParam("versionId") Version version, RedirectAttributes model) {

        Agreement agreement = this.linkedRepositoryRepository.findByOrganizationAndRepository(organization, repository).getAgreement();
        CorporateSignatory corporateSignatory = this.corporateSignatoryRepository.save(new CorporateSignatory(version, name, emailAddress,
            mailingAddress, country, telephoneNumber, company, title));

        for (String domain : domains) {
            this.signedDomainRepository.save(new SignedDomain(domain, agreement, corporateSignatory));
        }

        model //
        .addFlashAttribute("organization", organization) //
        .addFlashAttribute("repository", repository);

        return "redirect:/confirmation";
    }

    private SortedSet<String> domains(Set<Email> emails) {
        SortedSet<String> domains = new TreeSet<>();

        for (Email email : emails) {
            Matcher matcher = DOMAIN.matcher(email.getAddress());
            if (matcher.find()) {
                domains.add(matcher.group(1));
            }
        }

        return domains;
    }
}
