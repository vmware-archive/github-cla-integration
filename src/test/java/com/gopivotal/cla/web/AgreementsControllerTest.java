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
import org.mockito.internal.matchers.Matches;
import org.springframework.beans.factory.annotation.Autowired;

import com.gopivotal.cla.github.MarkdownService;
import com.gopivotal.cla.model.Agreement;
import com.gopivotal.cla.model.Version;
import com.gopivotal.cla.repository.AgreementRepository;
import com.gopivotal.cla.repository.VersionRepository;

public final class AgreementsControllerTest extends AbstractControllerTest {

    @Autowired
    private volatile AgreementRepository agreementRepository;

    @Autowired
    private volatile VersionRepository versionRepository;

    @Autowired
    private volatile MarkdownService markdownService;

    @Test
    public void listAgreements() throws Exception {
        Agreement bravo = this.agreementRepository.save(new Agreement("bravo"));
        Agreement alpha = this.agreementRepository.save(new Agreement("alpha"));

        this.mockMvc.perform(get("/agreements")) //
        .andExpect(status().isOk()) //
        .andExpect(view().name("agreements")) //
        .andExpect(model().attribute("agreements", Arrays.asList(alpha, bravo)));
    }

    @Test
    public void createAgreement() throws Exception {
        this.mockMvc.perform(post("/agreements").param("name", "test-name")) //
        .andExpect(status().isFound()) //
        .andExpect(view().name(new Matches("redirect:/agreements/[\\d]+/versions")));

        assertEquals(1, countRowsInTable("agreements"));
        Map<String, Object> row = this.jdbcTemplate.queryForMap("SELECT * FROM agreements");
        assertEquals("test-name", row.get("name"));
    }

    @Test
    public void listVersions() throws Exception {
        Agreement agreement = this.agreementRepository.save(new Agreement("alpha"));
        Version bravo = this.versionRepository.save(new Version(agreement, "bravo", "test-content", "test-content"));
        Version alpha = this.versionRepository.save(new Version(agreement, "alpha", "test-content", "test-content"));

        this.mockMvc.perform(get("/agreements/{agreementId}/versions", agreement.getId())) //
        .andExpect(status().isOk()) //
        .andExpect(view().name("versions")) //
        .andExpect(model().attribute("agreement", agreement)) //
        .andExpect(model().attribute("versions", Arrays.asList(alpha, bravo)));
    }

    @Test
    public void createVersion() throws Exception {
        when(this.markdownService.render("test-individual-content")).thenReturn("test-individual-html");
        when(this.markdownService.render("test-corporate-content")).thenReturn("test-corporate-html");

        Agreement agreement = this.agreementRepository.save(new Agreement("test-name"));

        this.mockMvc.perform(
            post("/agreements/{agreementId}/versions", agreement.getId()).param("name", "test-name").param("individualContent",
                "test-individual-content").param("corporateContent", "test-corporate-content")) //
        .andExpect(status().isFound()) //
        .andExpect(view().name(new Matches("redirect:/agreements/[\\d]+/versions/[\\d]+")));

        assertEquals(1, countRowsInTable("versions"));
        Map<String, Object> row = this.jdbcTemplate.queryForMap("SELECT * FROM versions");
        assertEquals(agreement.getId(), row.get("agreementId"));
        assertEquals("test-name", row.get("name"));
        assertEquals("test-individual-html", row.get("individualContent"));
        assertEquals("test-corporate-html", row.get("corporateContent"));
    }

    @Test
    public void readVersion() throws Exception {
        Agreement agreement = this.agreementRepository.save(new Agreement("test-name"));
        Version version = this.versionRepository.save(new Version(agreement, "test-name", "test-individual-content", "test-corporate-content"));

        this.mockMvc.perform(get("/agreements/{agreementId}/versions/{versionId}", agreement.getId(), version.getId())) //
        .andExpect(status().isOk()) //
        .andExpect(view().name("version")) //
        .andExpect(model().attribute("version", version)) //
        .andExpect(model().attribute("individualContent", "test-individual-content")) //
        .andExpect(model().attribute("corporateContent", "test-corporate-content"));
    }

}
