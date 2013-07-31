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

package com.nebhale.cla.web;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.SortedSet;

import org.junit.Test;
import org.springframework.ui.ModelMap;

import com.nebhale.cla.github.GitHubRepositories;
import com.nebhale.cla.github.GitHubRestOperations;
import com.nebhale.cla.util.Sets;

public final class RepositoriesControllerTest {

    private final GitHubRestOperations gitHubRestOperations = mock(GitHubRestOperations.class);

    private final GitHubRepositories gitHubRepositories = mock(GitHubRepositories.class);

    private final RepositoriesController controller = new RepositoriesController(this.gitHubRestOperations, this.gitHubRepositories);

    @Test
    public void listRepositories() {
        SortedSet<String> adminRepositories = Sets.asSortedSet("test-repo1", "test-repo2");
        when(this.gitHubRepositories.getAdminRepositories()).thenReturn(adminRepositories);

        ModelMap model = new ModelMap();
        String result = this.controller.listRepositories(model);

        assertEquals("repositories", result);
        assertEquals(adminRepositories, model.get("candidateRepositories"));
    }

}
