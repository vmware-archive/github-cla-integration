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
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gopivotal.cla.github.Email;
import com.gopivotal.cla.github.Emails;
import com.gopivotal.cla.github.GitHubClient;
import com.gopivotal.cla.model.Agreement;
import com.gopivotal.cla.model.LinkedRepository;
import com.gopivotal.cla.model.Version;
import com.gopivotal.cla.repository.AgreementRepository;
import com.gopivotal.cla.repository.LinkedRepositoryRepository;
import com.gopivotal.cla.repository.VersionRepository;
import com.gopivotal.cla.testutil.Sets;

public final class CorporateSignatoryControllerTest extends AbstractControllerTest {

    @Autowired
    private volatile GitHubClient gitHubClient;

    @Autowired
    private volatile AgreementRepository agreementRepository;

    @Autowired
    private volatile LinkedRepositoryRepository linkedRepositoryRepository;

    @Autowired
    private volatile VersionRepository versionRepository;

    @Test
    public void readCorporate() throws Exception {
        Agreement agreement = this.agreementRepository.save(new Agreement("test-name"));
        LinkedRepository linkedRepository = this.linkedRepositoryRepository.save(new LinkedRepository(agreement, "org/repo", "test-access-token"));

        this.versionRepository.save(new Version(agreement, "alpha", "test-individual-content", "test-corporate-content"));
        Version bravoVersion = this.versionRepository.save(new Version(agreement, "bravo", "test-individual-content", "test-corporate-content"));

        Emails emails = new Emails();
        when(this.gitHubClient.getEmails()).thenReturn(emails);

        Email charlieEmail = new Email("charlie@charlie", false, true);
        Email bravoEmail = new Email("bravo@bravo", false, false);
        Email alphaEmail = new Email("alpha@alpha", false, true);
        emails.add(charlieEmail);
        emails.add(bravoEmail);
        emails.add(alphaEmail);

        this.mockMvc.perform(get("/org/repo/corporate")) //
        .andExpect(status().isOk()) //
        .andExpect(view().name("corporate")) //
        .andExpect(model().attribute("repository", linkedRepository)) //
        .andExpect(model().attribute("version", bravoVersion)) //
        .andExpect(model().attribute("emails", Sets.asSortedSet(alphaEmail, charlieEmail))) //
        .andExpect(model().attribute("domains", Sets.asSortedSet("@alpha", "@charlie")));
    }

    @Test
    public void createCorporate() throws Exception {
        Agreement agreement = this.agreementRepository.save(new Agreement("test-name"));
        Version version = this.versionRepository.save(new Version(agreement, "test-name", "test-individual-content", "test-corporate-content"));
        this.linkedRepositoryRepository.save(new LinkedRepository(agreement, "org/repo", "test-access-token"));

        this.mockMvc.perform(post("/org/repo/corporate") //
        .param("company", "test-company") //
        .param("title", "test-title") //
        .param("name", "test-name") //
        .param("email", "test-email") //
        .param("mailingAddress", "test-mailing-address") //
        .param("country", "test-country") //
        .param("telephoneNumber", "test-telephone-number") //
        .param("contribution", "contribution-1,contribution-2") //
        .param("versionId", String.valueOf(version.getId()))) //
        .andExpect(status().isFound()) //
        .andExpect(view().name("redirect:/confirmation")) //
        .andExpect(flash().attribute("organization", "org")) //
        .andExpect(flash().attribute("repository", "repo"));

        assertEquals(1, countRowsInTable("signatories"));
        Map<String, Object> row = this.jdbcTemplate.queryForMap("SELECT * FROM signatories");
        assertEquals(version.getId(), row.get("versionId"));
        assertNotNull(row.get("signingDate"));
        assertEquals("test-name", decrypt(row.get("name")));
        assertEquals("test-email", decrypt(row.get("emailAddress")));
        assertEquals("test-mailing-address", decrypt(row.get("mailingAddress")));
        assertEquals("test-country", decrypt(row.get("country")));
        assertEquals("test-telephone-number", decrypt(row.get("telephoneNumber")));
        assertEquals("test-company", decrypt(row.get("company")));
        assertEquals("test-title", decrypt(row.get("title")));

        assertEquals(2, countRowsInTable("signedDomains"));
    }

}
