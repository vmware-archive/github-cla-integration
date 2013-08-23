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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.gopivotal.cla.github.GitHubClient;
import com.gopivotal.cla.model.Agreement;
import com.gopivotal.cla.model.IndividualSignatory;
import com.gopivotal.cla.model.SignedAddress;
import com.gopivotal.cla.model.Version;
import com.gopivotal.cla.repository.IndividualSignatoryRepository;
import com.gopivotal.cla.repository.LinkedRepositoryRepository;
import com.gopivotal.cla.repository.SignedAddressRepository;
import com.gopivotal.cla.repository.VersionRepository;

@Controller
final class IndividualSignatoryController extends AbstractSignatoryController {

    private final IndividualSignatoryRepository individualSignatoryRepository;

    private final LinkedRepositoryRepository linkedRepositoryRepository;

    private final SignedAddressRepository signedAddressRepository;

    @Autowired
    IndividualSignatoryController(GitHubClient gitHubClient, LinkedRepositoryRepository linkedRepositoryRepository,
        IndividualSignatoryRepository individualSignatoryRepository, SignedAddressRepository signedAddressRepository,
        VersionRepository versionRepository) {
        super(gitHubClient, linkedRepositoryRepository, versionRepository);
        this.individualSignatoryRepository = individualSignatoryRepository;
        this.linkedRepositoryRepository = linkedRepositoryRepository;
        this.signedAddressRepository = signedAddressRepository;
    }

    @Transactional(readOnly = true)
    @RequestMapping(method = RequestMethod.GET, value = "{organization}/{repository}/individual")
    String readIndividual(@PathVariable String organization, @PathVariable String repository, ModelMap model) {
        populateAgreementModel(organization, repository, model);

        return "individual";
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST, value = "{organization}/{repository}/individual")
    String createIndividual(@PathVariable String organization, @PathVariable String repository, @RequestParam String name,
        @RequestParam("email") String emailAddress, @RequestParam String mailingAddress, @RequestParam String country,
        @RequestParam String telephoneNumber, @RequestParam("contribution") String[] addresses, @RequestParam("versionId") Version version) {

        Agreement agreement = this.linkedRepositoryRepository.findByOrganizationAndRepository(organization, repository).getAgreement();
        IndividualSignatory individualSignatory = this.individualSignatoryRepository.save(new IndividualSignatory(version, name, emailAddress,
            mailingAddress, country, telephoneNumber));

        for (String address : addresses) {
            this.signedAddressRepository.save(new SignedAddress(address, agreement, individualSignatory));
        }

        return "redirect:/";
    }
}
