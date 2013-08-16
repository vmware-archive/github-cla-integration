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

import java.util.HashSet;

import org.junit.Test;
import org.springframework.ui.ModelMap;

import com.gopivotal.cla.Agreement;
import com.gopivotal.cla.LinkedRepository;
import com.gopivotal.cla.Version;
import com.gopivotal.cla.github.Email;
import com.gopivotal.cla.github.Emails;
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

    private static final Email EMAIL = new StubEmail("test@address", false, true);

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

        Emails emails = new StubEmails();
        when(this.gitHubClient.getEmails()).thenReturn(emails);
        emails.add(EMAIL);
        emails.add(new StubEmail("email@other.domain", false, false));

        ModelMap model = new ModelMap();
        String result = this.controller.readIndividual("test", "name", model);

        assertEquals("individual", result);
        assertEquals(LINKED_REPOSITORY, model.get("repository"));
        assertEquals(VERSION, model.get("version"));
        assertEquals(Sets.asSortedSet(EMAIL), model.get("emails"));
    }

    @Test
    public void readCorporate() {
        when(this.linkedRepositoryRepository.read("test", "name")).thenReturn(LINKED_REPOSITORY);
        when(this.versionRepository.find(AGREEMENT.getId())).thenReturn(Sets.asSortedSet(VERSION));

        Emails emails = new StubEmails();
        when(this.gitHubClient.getEmails()).thenReturn(emails);
        emails.add(EMAIL);
        emails.add(new StubEmail("email@other.domain", false, false));

        ModelMap model = new ModelMap();
        String result = this.controller.readCorporate("test", "name", model);

        assertEquals("corporate", result);
        assertEquals(LINKED_REPOSITORY, model.get("repository"));
        assertEquals(VERSION, model.get("version"));
        assertEquals(Sets.asSortedSet(EMAIL), model.get("emails"));
    }

    private static final class StubEmails extends HashSet<Email> implements Emails {

        private static final long serialVersionUID = 5555232529640927085L;

    }

    private static final class StubEmail implements Email {

        private final String address;

        private final Boolean primary;

        private final Boolean verified;

        private StubEmail(String address, Boolean primary, Boolean verified) {
            this.address = address;
            this.primary = primary;
            this.verified = verified;
        }

        @Override
        public int compareTo(Email o) {
            return this.address.compareToIgnoreCase(o.getAddress());
        }

        @Override
        public String getAddress() {
            return this.address;
        }

        @Override
        public Boolean isPrimary() {
            return this.primary;
        }

        @Override
        public Boolean isVerified() {
            return this.verified;
        }

    }

}
