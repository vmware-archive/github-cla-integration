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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.SortedSet;

import org.junit.Test;
import org.springframework.ui.ModelMap;

import com.gopivotal.cla.Agreement;
import com.gopivotal.cla.Version;
import com.gopivotal.cla.github.GitHubClient;
import com.gopivotal.cla.github.MarkdownService;
import com.gopivotal.cla.repository.AgreementRepository;
import com.gopivotal.cla.repository.VersionRepository;
import com.gopivotal.cla.util.Sets;

public final class AgreementsControllerTest {

    private static final Agreement AGREEMENT = new Agreement(Long.MIN_VALUE, "test-name");

    private static final Version VERSION = new Version(Long.MIN_VALUE + 1, AGREEMENT, "test-name", "test-individual-content",
        "test-corporate-content");

    private final GitHubClient gitHubClient = mock(GitHubClient.class);

    private final AgreementRepository agreementRepository = mock(AgreementRepository.class);

    private final VersionRepository versionRepository = mock(VersionRepository.class);

    private final MarkdownService markdownService = mock(MarkdownService.class);

    private final AgreementsController controller = new AgreementsController(this.gitHubClient, this.agreementRepository, this.versionRepository,
        this.markdownService);

    @Test
    public void listAgreements() {
        SortedSet<Agreement> agreements = Sets.asSortedSet();
        agreements.add(AGREEMENT);

        when(this.agreementRepository.find()).thenReturn(agreements);

        ModelMap model = new ModelMap();
        String result = this.controller.listAgreements(model);

        assertEquals("agreements", result);
        assertEquals(new ModelMap("agreements", agreements), model);
    }

    @Test
    public void createAgreement() {
        when(this.agreementRepository.create("test-name")).thenReturn(AGREEMENT);

        String result = this.controller.createAgreement("test-name");

        assertEquals(String.format("redirect:/agreements/%d/versions", Long.MIN_VALUE), result);
    }

    @Test
    public void listVersions() {
        SortedSet<Version> versions = Sets.asSortedSet();
        versions.add(VERSION);

        when(this.agreementRepository.read(Long.MIN_VALUE)).thenReturn(AGREEMENT);
        when(this.versionRepository.find(Long.MIN_VALUE)).thenReturn(versions);

        ModelMap model = new ModelMap();
        String result = this.controller.listVersions(Long.MIN_VALUE, model);

        assertEquals("versions", result);
        assertEquals(AGREEMENT, model.get("agreement"));
        assertEquals(versions, model.get("versions"));
    }

    @Test
    public void createVersion() {
        when(this.versionRepository.create(AGREEMENT.getId(), "test-name", "test-individual-content", "test-corporate-content")).thenReturn(VERSION);

        String result = this.controller.createVersion(Long.MIN_VALUE, "test-name", "test-individual-content", "test-corporate-content");

        assertEquals(String.format("redirect:/agreements/%d/versions/%d", AGREEMENT.getId(), VERSION.getId()), result);
    }

    @Test
    public void readVersion() {
        when(this.versionRepository.read(VERSION.getId())).thenReturn(VERSION);
        when(this.markdownService.render(VERSION.getIndividualAgreementContent())).thenReturn("test-individual-html");
        when(this.markdownService.render(VERSION.getCorporateAgreementContent())).thenReturn("test-corporate-html");

        ModelMap model = new ModelMap();
        String result = this.controller.readVersion(VERSION.getId(), model);

        assertEquals("version", result);
        assertEquals(VERSION, model.get("version"));
        assertEquals("test-individual-html", model.get("individualContent"));
        assertEquals("test-corporate-html", model.get("corporateContent"));
    }
}
