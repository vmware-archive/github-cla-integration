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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gopivotal.cla.github.GitHubClient;
import com.gopivotal.cla.github.MarkdownService;
import com.gopivotal.cla.model.Agreement;
import com.gopivotal.cla.model.Version;
import com.gopivotal.cla.repository.AgreementRepository;
import com.gopivotal.cla.repository.VersionRepository;

@Controller
@RequestMapping("/agreements")
final class AgreementsController extends AbstractController {

    private final AgreementRepository agreementRepository;

    private final VersionRepository versionRepository;

    private final MarkdownService markdownService;

    @Autowired
    AgreementsController(GitHubClient gitHubClient, AgreementRepository agreementRepository, VersionRepository versionRepository,
        MarkdownService markdownService) {
        super(gitHubClient);
        this.agreementRepository = agreementRepository;
        this.versionRepository = versionRepository;
        this.markdownService = markdownService;
    }

    @Transactional(readOnly = true)
    @RequestMapping(method = RequestMethod.GET, value = "")
    String listAgreements(Model model) {
        model.addAttribute("agreements", this.agreementRepository.findAll(new Sort("name")));

        return "agreements";
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, value = "")
    String createAgreement(@RequestParam String name) {
        Agreement agreement = this.agreementRepository.save(new Agreement(name));

        return String.format("redirect:/agreements/%d/versions", agreement.getId());
    }

    @Transactional(readOnly = true)
    @RequestMapping(method = RequestMethod.GET, value = "/{agreementId}/versions")
    String listVersions(@PathVariable("agreementId") Agreement agreement, Model model) {
        model //
        .addAttribute("agreement", agreement) //
        .addAttribute("versions", this.versionRepository.findByAgreement(agreement, new Sort("name")));

        return "versions";
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, value = "/{agreementId}/versions")
    String createVersion(@PathVariable("agreementId") Agreement agreement, @RequestParam String name, @RequestParam String individualContent,
        @RequestParam String corporateContent) {
        String renderedIndividualContent = this.markdownService.render(individualContent);
        String renderedCorporateContent = this.markdownService.render(corporateContent);
        Version version = this.versionRepository.save(new Version(agreement, name, renderedIndividualContent, renderedCorporateContent));

        return String.format("redirect:/agreements/%d/versions/%d", agreement.getId(), version.getId());
    }

    @Transactional(readOnly = true)
    @RequestMapping(method = RequestMethod.GET, value = "/{agreementId}/versions/{versionId}")
    String readVersion(@PathVariable("versionId") Version version, Model model) {
        model //
        .addAttribute("version", version) //
        .addAttribute("individualContent", version.getIndividualAgreementContent()) //
        .addAttribute("corporateContent", version.getCorporateAgreementContent());

        return "version";
    }

}
