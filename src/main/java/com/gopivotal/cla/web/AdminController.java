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

import com.gopivotal.cla.domain.Agreement;
import com.gopivotal.cla.domain.AgreementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/admin")
final class AdminController {

    private final AgreementRepository agreementRepository;

    @Autowired
    AdminController(AgreementRepository agreementRepository) {
        this.agreementRepository = agreementRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "")
    String admin() {
        return "admin";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/agreements")
    String listAgreements(Agreement agreement) {
        return "admin-agreements";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/agreements")
    String createAgreement(Agreement agreement) {
        this.agreementRepository.save(agreement);

        return "redirect:/admin/agreements";
    }

}
