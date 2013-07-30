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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestOperations;

import com.nebhale.cla.Agreement;
import com.nebhale.cla.Type;
import com.nebhale.cla.Version;
import com.nebhale.cla.repository.AgreementRepository;
import com.nebhale.cla.repository.VersionRepository;

@Controller
@RequestMapping("/admin")
final class AdminController extends AbstractController {

    private final AgreementRepository agreementRepository;

    private final VersionRepository versionRepository;

    @Autowired
    AdminController(RestOperations restOperations, AgreementRepository agreementRepository, VersionRepository versionRepository) {
        super(restOperations);
        this.agreementRepository = agreementRepository;
        this.versionRepository = versionRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    String index(ModelMap model) {
        model.put("agreements", this.agreementRepository.find());
        return "admin";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/agreements")
    String createAgreement(@RequestParam Type type, @RequestParam String name) {
        Agreement agreement = this.agreementRepository.create(type, name);
        return String.format("redirect:/admin/agreements/%d", agreement.getId());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/agreements/{agreementId}")
    String readAgreement(@PathVariable Long agreementId, ModelMap model) {
        model.put("agreement", this.agreementRepository.read(agreementId));
        model.put("versions", this.versionRepository.find(agreementId));
        return "agreement";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/agreements/{agreementId}/versions")
    String createVersion(@PathVariable Long agreementId, @RequestParam String version, @RequestParam String content) {
        Version richVersion = this.versionRepository.create(agreementId, version, content);
        return String.format("redirect:/admin/agreements/%d/versions/%d", agreementId, richVersion.getId());
    }

}
