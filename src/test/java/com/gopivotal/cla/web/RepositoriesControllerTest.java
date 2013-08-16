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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.ui.ModelMap;

import com.gopivotal.cla.Agreement;
import com.gopivotal.cla.LinkedRepository;
import com.gopivotal.cla.github.GitHubClient;
import com.gopivotal.cla.github.Organization;
import com.gopivotal.cla.github.Organizations;
import com.gopivotal.cla.github.Permissions;
import com.gopivotal.cla.github.Repositories;
import com.gopivotal.cla.github.Repository;
import com.gopivotal.cla.github.User;
import com.gopivotal.cla.repository.AgreementRepository;
import com.gopivotal.cla.repository.LinkedRepositoryRepository;
import com.gopivotal.cla.util.Sets;

public final class RepositoriesControllerTest {

    private static final String ACCESS_TOKEN = "access-token";

    private static final Agreement AGREEMENT = new Agreement(Long.MIN_VALUE, "test-agreement");

    private static final LinkedRepository LINKED_REPOSITORY = new LinkedRepository(Long.MIN_VALUE + 1, AGREEMENT, "admin", ACCESS_TOKEN);

    private final GitHubClient gitHubClient = mock(GitHubClient.class);

    private final AgreementRepository agreementRepository = mock(AgreementRepository.class);

    private final LinkedRepositoryRepository linkedRepositoryRepository = mock(LinkedRepositoryRepository.class);

    private final RepositoriesController controller = new RepositoriesController(this.gitHubClient, this.agreementRepository,
        this.linkedRepositoryRepository);

    @Test
    public void listRepositories() {
        when(this.linkedRepositoryRepository.find()).thenReturn(
            Sets.asSortedSet(LINKED_REPOSITORY, new LinkedRepository(Long.MIN_VALUE + 11, AGREEMENT, "alpha/bravo", ACCESS_TOKEN)));
        when(this.agreementRepository.find()).thenReturn(Sets.asSortedSet(AGREEMENT));

        getAdminRepositories();

        ModelMap model = new ModelMap();
        String result = this.controller.listRepositories(model);

        assertEquals("repositories", result);
        assertEquals(Sets.asSortedSet(LINKED_REPOSITORY), model.get("linkedRepositories"));
        assertEquals(Sets.asSortedSet(AGREEMENT), model.get("candidateAgreements"));
        assertEquals(Sets.asSortedSet(new StubRepository("admin-2", null), new StubRepository("admin-3", null)), model.get("candidateRepositories"));
    }

    @Test
    public void createRepository() {
        when(this.gitHubClient.getAccessToken()).thenReturn(ACCESS_TOKEN);
        when(this.linkedRepositoryRepository.create("test-linked-repository", Long.MIN_VALUE, ACCESS_TOKEN)).thenReturn(LINKED_REPOSITORY);

        String result = this.controller.createLinkedRepository("test-linked-repository", AGREEMENT.getId());

        assertEquals("redirect:/repositories", result);
    }

    @Test
    public void hrefPrefixNull() {
        String result = this.controller.hrefPrefix(null);
        assertEquals("", result);
    }

    @Test
    public void hrefPrefixDefaultScheme() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSecure(false);
        request.addHeader("HOST", "test.host");

        String result = this.controller.hrefPrefix(request);
        assertEquals("http://test.host", result);
    }

    @Test
    public void hreafPrefixSecureScheme() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSecure(true);
        request.addHeader("HOST", "test.host");

        String result = this.controller.hrefPrefix(request);
        assertEquals("https://test.host", result);
    }

    private void getAdminRepositories() {
        User user = mock(User.class);
        when(this.gitHubClient.getUser()).thenReturn(user);

        Organizations organizations = new StubOrganizations();
        when(user.getOrganizations()).thenReturn(organizations);

        Organization organization = mock(Organization.class);
        organizations.add(organization);

        Repositories organizationRepositories = new StubRepositories();
        when(organization.getRepositories()).thenReturn(organizationRepositories);

        organizationRepositories.add(new StubRepository("non-admin", new StubPermissions(false, false, false)));
        organizationRepositories.add(new StubRepository("admin", new StubPermissions(true, false, false)));
        organizationRepositories.add(new StubRepository("admin-2", new StubPermissions(true, false, false)));

        Repositories userRepositories = new StubRepositories();
        when(user.getRepositories()).thenReturn(userRepositories);

        userRepositories.add(new StubRepository("admin-3", new StubPermissions(true, false, false)));
    }

    private static final class StubOrganizations extends HashSet<Organization> implements Organizations {

        private static final long serialVersionUID = -3881602907254273474L;

    }

    private static final class StubRepositories extends HashSet<Repository> implements Repositories {

        private static final long serialVersionUID = 4300270506619743421L;

    }

    private static final class StubRepository implements Repository {

        private final String fullName;

        private final Permissions permissions;

        private StubRepository(String fullName, Permissions permissions) {
            this.fullName = fullName;
            this.permissions = permissions;
        }

        @Override
        public int compareTo(Repository o) {
            return this.fullName.compareToIgnoreCase(o.getFullName());
        }

        @Override
        public String getFullName() {
            return this.fullName;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public Permissions getPermissions() {
            return this.permissions;
        }

    }

    private static final class StubPermissions implements Permissions {

        private final Boolean admin;

        private final Boolean push;

        private final Boolean pull;

        private StubPermissions(Boolean admin, Boolean push, Boolean pull) {
            this.admin = admin;
            this.push = push;
            this.pull = pull;
        }

        @Override
        public int compareTo(Permissions o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Boolean isAdmin() {
            return this.admin;
        }

        @Override
        public Boolean isPush() {
            return this.push;
        }

        @Override
        public Boolean isPull() {
            return this.pull;
        }

    }
}
