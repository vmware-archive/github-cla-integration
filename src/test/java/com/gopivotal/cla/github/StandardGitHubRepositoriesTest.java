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

package com.gopivotal.cla.github;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.gopivotal.cla.github.GitHubRestOperations;
import com.gopivotal.cla.github.StandardGitHubRepositories;
import com.gopivotal.cla.util.Sets;

public final class StandardGitHubRepositoriesTest {

    private final GitHubRestOperations gitHubRestOperations = mock(GitHubRestOperations.class);

    private final StandardGitHubRepositories gitHubRepositories = new StandardGitHubRepositories(this.gitHubRestOperations);

    @Test
    public void getAdminRepositories() {
        when(this.gitHubRestOperations.getForObject("/user/orgs", Set.class)).thenReturn(Sets.asSet(createOrg("admin"), createOrg("non-admin")));
        when(this.gitHubRestOperations.getForObject("/orgs/{org}/repos", Set.class, "admin")).thenReturn(Sets.asSet(createRepo("admin/repo", true)));
        when(this.gitHubRestOperations.getForObject("/orgs/{org}/repos", Set.class, "non-admin")).thenReturn(
            Sets.asSet(createRepo("non-admin/repo", false)));
        when(this.gitHubRestOperations.getForObject("/user/repos", Set.class)).thenReturn(Sets.asSet(createRepo("user/repo", true)));

        Set<String> result = this.gitHubRepositories.getAdminRepositories();

        assertEquals(Sets.asSortedSet("admin/repo", "user/repo"), result);
    }

    private Map<String, Object> createOrg(String login) {
        Map<String, Object> org = new HashMap<>();
        org.put("login", login);

        return org;
    }

    private Map<String, Object> createRepo(String name, boolean admin) {
        Map<String, Object> repo = new HashMap<>();
        repo.put("full_name", name);

        Map<String, Object> permissions = new HashMap<>();
        permissions.put("admin", admin);
        repo.put("permissions", permissions);

        return repo;
    }
}
