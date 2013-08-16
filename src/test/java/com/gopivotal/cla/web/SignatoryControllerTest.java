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

import org.junit.Test;
import org.springframework.ui.ModelMap;

import com.gopivotal.cla.Agreement;
import com.gopivotal.cla.LinkedRepository;
import com.gopivotal.cla.Version;
import com.gopivotal.cla.github.GitHubClient;
import com.gopivotal.cla.repository.LinkedRepositoryRepository;
import com.gopivotal.cla.repository.VersionRepository;
import com.gopivotal.cla.util.Sets;

public final class SignatoryControllerTest {

    private static final String ACCESS_TOKEN = "access-token";

    private static final Agreement AGREEMENT = new Agreement(Long.MIN_VALUE, "test-agreement");

    private static final LinkedRepository LINKED_REPOSITORY = new LinkedRepository(Long.MIN_VALUE + 1, AGREEMENT, "test/name", ACCESS_TOKEN);

    private static final Version VERSION = new Version(Long.MIN_VALUE + 2, AGREEMENT, "test-name", "test-individual-content",
        "test-corporate-content");

    private final GitHubClient gitHubClient = mock(GitHubClient.class);

    private final LinkedRepositoryRepository linkedRepositoryRepository = mock(LinkedRepositoryRepository.class);

    private final VersionRepository versionRepository = mock(VersionRepository.class);

    private final SignatoryController controller = new SignatoryController(this.gitHubClient, this.linkedRepositoryRepository, this.versionRepository);

    @Test
    public void readRepository() {
        when(this.linkedRepositoryRepository.read("test", "name")).thenReturn(LINKED_REPOSITORY);

        ModelMap model = new ModelMap();
        String result = this.controller.readRepository("test", "name", model);

        assertEquals("repository", result);
        assertEquals(LINKED_REPOSITORY, model.get("repository"));
    }

    @Test
    public void readIndividual() {
        when(this.linkedRepositoryRepository.read("test", "name")).thenReturn(LINKED_REPOSITORY);
        when(this.versionRepository.find(AGREEMENT.getId())).thenReturn(Sets.asSortedSet(VERSION));

        ModelMap model = new ModelMap();
        String result = this.controller.readIndividual("test", "name", model);

        assertEquals("individual", result);
        assertEquals(LINKED_REPOSITORY, model.get("repository"));
        assertEquals(VERSION, model.get("version"));
    }

    @Test
    public void readCorporate() {
        when(this.linkedRepositoryRepository.read("test", "name")).thenReturn(LINKED_REPOSITORY);
        when(this.versionRepository.find(AGREEMENT.getId())).thenReturn(Sets.asSortedSet(VERSION));

        ModelMap model = new ModelMap();
        String result = this.controller.readCorporate("test", "name", model);

        assertEquals("corporate", result);
        assertEquals(LINKED_REPOSITORY, model.get("repository"));
        assertEquals(VERSION, model.get("version"));
    }

}
