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

import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gopivotal.cla.util.Sets;

@Component
final class StandardGitHubRepositories implements GitHubRepositories {

    private final GitHubRestOperations gitHubRestOperations;

    @Autowired
    StandardGitHubRepositories(GitHubRestOperations gitHubRestOperations) {
        this.gitHubRestOperations = gitHubRestOperations;
    }

    @Override
    public SortedSet<String> getAdminRepositories() {
        SortedSet<String> adminRepositories = Sets.asSortedSet();

        adminRepositories.addAll(getUserRepos());

        for (String org : getOrgs()) {
            for (String repo : getRepos(org)) {
                adminRepositories.add(repo);
            }
        }

        return adminRepositories;
    }

    @SuppressWarnings("unchecked")
    private Set<String> getOrgs() {
        Set<String> orgs = Sets.asSet();

        for (Map<String, Object> raw : (Set<Map<String, Object>>) this.gitHubRestOperations.getForObject("/user/orgs", Set.class)) {
            orgs.add((String) raw.get("login"));
        }

        return orgs;
    }

    @SuppressWarnings("unchecked")
    private Set<String> getRepos(String org) {
        Set<String> repos = Sets.asSet();

        for (Map<String, Object> raw : (Set<Map<String, Object>>) this.gitHubRestOperations.getForObject("/orgs/{org}/repos", Set.class, org)) {
            if (isAdmin(raw)) {
                repos.add((String) raw.get("full_name"));
            }
        }

        return repos;
    }

    @SuppressWarnings("unchecked")
    private Set<String> getUserRepos() {
        Set<String> repos = Sets.asSet();

        for (Map<String, Object> raw : (Set<Map<String, Object>>) this.gitHubRestOperations.getForObject("/user/repos", Set.class)) {
            repos.add((String) raw.get("full_name"));
        }

        return repos;
    }

    @SuppressWarnings("unchecked")
    private boolean isAdmin(Map<String, Object> raw) {
        return (boolean) ((Map<String, Object>) raw.get("permissions")).get("admin");
    }

}
