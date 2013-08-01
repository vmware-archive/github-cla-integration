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

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import org.junit.Test;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.ui.ModelMap;

import com.gopivotal.cla.Agreement;
import com.gopivotal.cla.Repository;
import com.gopivotal.cla.github.GitHubRepositories;
import com.gopivotal.cla.github.GitHubRestOperations;
import com.gopivotal.cla.repository.AgreementRepository;
import com.gopivotal.cla.repository.RepositoryRepository;
import com.gopivotal.cla.util.Sets;
import com.gopivotal.cla.web.RepositoriesController;

public final class RepositoriesControllerTest {

    private static final OAuth2AccessToken ACCESS_TOKEN = new DefaultOAuth2AccessToken("access_token");

    private static final Agreement AGREEMENT = new Agreement(Long.MIN_VALUE, "test-name");

    private static final Repository REPOSITORY = new Repository(Long.MIN_VALUE + 1, "test-name", AGREEMENT.getId(), ACCESS_TOKEN.getValue());

    private final GitHubRestOperations gitHubRestOperations = mock(GitHubRestOperations.class);

    private final AgreementRepository agreementRepository = mock(AgreementRepository.class);

    private final GitHubRepositories gitHubRepositories = mock(GitHubRepositories.class);

    private final RepositoryRepository repositoryRepository = mock(RepositoryRepository.class);

    private final OAuth2RestOperations oAuth2RestOperations = mock(OAuth2RestOperations.class);

    private final RepositoriesController controller = new RepositoriesController(this.gitHubRestOperations, this.agreementRepository,
        this.gitHubRepositories, this.repositoryRepository, this.oAuth2RestOperations);

    @Test
    public void listRepositories() {
        SortedSet<String> adminRepositories = Sets.asSortedSet("test-name", "test-repo2");
        when(this.gitHubRepositories.getAdminRepositories()).thenReturn(adminRepositories);
        SortedSet<Agreement> agreements = Sets.asSortedSet(new Agreement(Long.MIN_VALUE, "test-name"));
        when(this.agreementRepository.find()).thenReturn(agreements);
        when(this.repositoryRepository.find()).thenReturn(Sets.asSortedSet(REPOSITORY));
        when(this.agreementRepository.read(REPOSITORY.getAgreementId())).thenReturn(AGREEMENT);
        Map<String, String> repositoryMapping = new HashMap<>();
        repositoryMapping.put("test-name", "test-name");

        ModelMap model = new ModelMap();
        String result = this.controller.listRepositories(model);

        assertEquals("repositories", result);
        assertEquals(repositoryMapping, model.get("repositoryMapping"));
        assertEquals(Sets.asSortedSet("test-repo2"), model.get("candidateRepositories"));
        assertEquals(agreements, model.get("candidateAgreements"));
    }

    @Test
    public void createRepository() {
        when(this.oAuth2RestOperations.getAccessToken()).thenReturn(ACCESS_TOKEN);
        when(this.repositoryRepository.create("test-name", Long.MIN_VALUE, ACCESS_TOKEN.getValue())).thenReturn(REPOSITORY);

        String result = this.controller.createRepository("test-name", AGREEMENT.getId());

        assertEquals("redirect:/repositories", result);
    }
}
