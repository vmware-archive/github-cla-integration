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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gopivotal.cla.github.GitHubClient;
import com.gopivotal.cla.github.Permissions;
import com.gopivotal.cla.github.Repository;
import com.gopivotal.cla.model.Agreement;
import com.gopivotal.cla.model.LinkedRepository;
import com.gopivotal.cla.repository.AgreementRepository;
import com.gopivotal.cla.repository.LinkedRepositoryRepository;
import com.gopivotal.cla.testutil.Sets;

public final class RepositoriesControllerTest extends AbstractControllerTest {

    @Autowired
    private volatile GitHubClient gitHubClient;

    @Autowired
    private volatile AgreementRepository agreementRepository;

    @Autowired
    private volatile LinkedRepositoryRepository linkedRepositoryRepository;

    @Test
    public void listRepositories() throws Exception {
        Agreement bravoAgreement = this.agreementRepository.save(new Agreement("bravo"));
        Agreement alphaAgreement = this.agreementRepository.save(new Agreement("alpha"));
        this.linkedRepositoryRepository.save(new LinkedRepository(bravoAgreement, "foxtrot", "test-access-token"));
        LinkedRepository bravoLinkedRepository = this.linkedRepositoryRepository.save(new LinkedRepository(bravoAgreement, "bravo",
            "test-access-token"));
        LinkedRepository alphaLinkedRepository = this.linkedRepositoryRepository.save(new LinkedRepository(alphaAgreement, "alpha",
            "test-access-token"));

        Repository echoRepository = new Repository("echo", null, new Permissions(true, false, false));
        Repository deltaRepository = new Repository("delta", null, new Permissions(false, false, false));
        Repository charlieRepository = new Repository("charlie", null, new Permissions(true, false, false));
        Repository bravoRepository = new Repository("bravo", null, new Permissions(true, false, false));
        Repository alphaRepository = new Repository("alpha", null, new Permissions(true, false, false));

        this.organization.getRepositories().add(deltaRepository);
        this.organization.getRepositories().add(charlieRepository);
        this.organization.getRepositories().add(alphaRepository);

        this.user.getRepositories().add(echoRepository);
        this.user.getRepositories().add(bravoRepository);

        this.mockMvc.perform(get("/repositories")) //
        .andExpect(status().isOk()) //
        .andExpect(view().name("repositories")) //
        .andExpect(model().attribute("linkedRepositories", Arrays.asList(alphaLinkedRepository, bravoLinkedRepository))) //
        .andExpect(model().attribute("candidateAgreements", Arrays.asList(alphaAgreement, bravoAgreement))).andExpect(
            model().attribute("candidateRepositories", Sets.asSortedSet(charlieRepository, echoRepository)));
    }

    @Test
    public void createLinkedRepository() throws Exception {
        when(this.gitHubClient.getAccessToken()).thenReturn("test-access-token");
        Agreement agreement = this.agreementRepository.save(new Agreement("test-name"));

        this.mockMvc.perform(post("/repositories") //
        .param("repository", "org/repo") //
        .param("agreement", String.valueOf(agreement.getId()))) //
        .andExpect(status().isFound()) //
        .andExpect(view().name("redirect:/repositories"));

        assertEquals(1, countRowsInTable("repositories"));
        Map<String, Object> row = this.jdbcTemplate.queryForMap("SELECT * FROM repositories");
        assertEquals("org/repo", row.get("name"));
        assertEquals(agreement.getId(), row.get("agreementId"));
        assertEquals("test-access-token", decrypt(row.get("accessToken")));
    }
}
