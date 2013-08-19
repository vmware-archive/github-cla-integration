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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gopivotal.cla.model.Agreement;
import com.gopivotal.cla.model.LinkedRepository;
import com.gopivotal.cla.repository.AgreementRepository;
import com.gopivotal.cla.repository.LinkedRepositoryRepository;

public final class SignatoryControllerTest extends AbstractControllerTest {

    @Autowired
    private volatile AgreementRepository agreementRepository;

    @Autowired
    private volatile LinkedRepositoryRepository linkedRepositoryRepository;

    @Test
    public void readRepository() throws Exception {
        Agreement agreement = this.agreementRepository.save(new Agreement("test-name"));
        LinkedRepository linkedRepository = this.linkedRepositoryRepository.save(new LinkedRepository(agreement, "org/repo", "test-access-token"));

        this.mockMvc.perform(get("/org/repo")) //
        .andExpect(status().isOk()) //
        .andExpect(view().name("repository")) //
        .andExpect(model().attribute("repository", linkedRepository));
    }

}
