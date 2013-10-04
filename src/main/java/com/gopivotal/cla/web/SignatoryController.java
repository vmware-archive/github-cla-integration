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

import com.gopivotal.cla.domain.LinkedRepository;
import com.gopivotal.cla.domain.LinkedRepositoryRepository;
import com.gopivotal.cla.domain.Signatory;
import com.gopivotal.cla.domain.SignatoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
final class SignatoryController {

    private final LinkedRepositoryRepository linkedRepositoryRepository;

    private final SignatoryRepository signatoryRepository;

    @Autowired
    SignatoryController(LinkedRepositoryRepository linkedRepositoryRepository, SignatoryRepository signatoryRepository) {
        this.linkedRepositoryRepository = linkedRepositoryRepository;
        this.signatoryRepository = signatoryRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/signatory")
    String signatory(Model model, @AuthenticationPrincipal User user) {
        Signatory signatory = getSignatory(user);
        List<LinkedRepository> linkedRepositories = this.linkedRepositoryRepository.findAll(new Sort("name"));

        model.addAttribute("unsignedRepositories", linkedRepositories);

        return "signatory";
    }

    private Signatory getSignatory(User user) {
        Signatory signatory = this.signatoryRepository.findByUsername(user.getUsername());

        if (signatory == null) {
            signatory = this.signatoryRepository.saveAndFlush(new Signatory(user.getUsername()));
        }

        return signatory;
    }

}
