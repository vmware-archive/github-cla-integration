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
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import com.gopivotal.cla.testutil.ComparableTestUtils;
import com.gopivotal.cla.testutil.EqualsAndHashCodeTestUtils;
import com.gopivotal.cla.testutil.Sets;
import com.gopivotal.cla.testutil.ToStringTestUtils;

public final class UserTest extends AbstractGitHubTest<User> {

    @Override
    protected User getInstance() {
        User user = new User("test-user-url");

        Map<String, Object> raw = new HashMap<>();
        raw.put("avatar_url", "test-avatar-url");
        raw.put("company", "test-company");
        raw.put("login", "test-login");
        raw.put("name", "B");
        raw.put("organizations_url", "test-organizations-url");
        raw.put("repos_url", "test-repos-url");

        user.initialize(raw);
        user.getOrganizations().initialize(Sets.asSet());
        user.getRepositories().initialize(Sets.asSet());

        return user;
    }

    @Override
    protected void assertState(User instance) {
        assertEquals("test-avatar-url", instance.getAvatarUrl());
        assertEquals("test-company", instance.getCompany());
        assertEquals("test-login", instance.getLogin());
        assertEquals("B", instance.getName());
        assertNotNull(instance.getOrganizations());
        assertNotNull(instance.getRepositories());
    }

    @Override
    protected void assertEqualsAndHashCode(EqualsAndHashCodeTestUtils<User> instance) {
        // @formatter:off
        instance
            .assertEqual(
                getInstance(),
                new User(null, null, null, "b", null, null))
            .assertNotEqual(new User(null, null, null, "A", null, null));
        // @formatter:on
    }

    @Override
    protected void assertComparable(ComparableTestUtils<User> instance) {
        // @formatter:off
        instance
            .assertBefore(
                new User(null, null, null, "a", null, null),
                new User(null, null, null, "A", null, null))
            .assertEqual(
                new User(null, null, null, "b", null, null),
                new User(null, null, null, "B", null, null))
            .assertAfter(
                new User(null, null, null, "c", null, null),
                new User(null, null, null, "C", null, null));
        // @formatter:on
    }

    @Override
    protected void assertToString(ToStringTestUtils instance) {
        instance.assertToString("avatarUrl", "company", "login", "name", "organizations", "repositories");
    }

}
