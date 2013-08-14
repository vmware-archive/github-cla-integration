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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import com.gopivotal.cla.testutil.AbstractTypeTest;
import com.gopivotal.cla.testutil.ComparableTestUtils;
import com.gopivotal.cla.testutil.EqualsAndHashCodeTestUtils;
import com.gopivotal.cla.testutil.ToStringTestUtils;
import com.gopivotal.cla.util.Sets;

public final class StandardUserTest extends AbstractTypeTest<User> {

    @Override
    protected User getInstance() {
        StandardUser user = new StandardUser("test-user-url");

        Map<String, Object> raw = new HashMap<>();
        raw.put("avatar_url", "test-avatar-url");
        raw.put("login", "test-login");
        raw.put("name", "B");
        raw.put("organizations_url", "test-organizations-url");
        raw.put("repos_url", "test-repos-url");

        user.initialize(raw);
        ((StandardOrganizations) user.getOrganizations()).initialize(Sets.asSet());
        ((StandardRepositories) user.getRepositories()).initialize(Sets.asSet());

        return user;
    }

    @Override
    protected User getInstanceWithNulls() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void assertState(User instance) {
        assertEquals("test-avatar-url", instance.getAvatarUrl());
        assertEquals("test-login", instance.getLogin());
        assertEquals("B", instance.getName());
        assertNotNull(instance.getOrganizations());
        assertNotNull(instance.getRepositories());
    }

    @Override
    protected void assertEqualsAndHashCode(EqualsAndHashCodeTestUtils<User> instance, EqualsAndHashCodeTestUtils<User> instanceWithNulls) {
        User user = mock(User.class);
        when(user.getName()).thenReturn("b", "A");

        // @formatter:off
        instance
            .assertEqual(
                getInstance(),
                user)
            .assertNotEqual(user);
        // @formatter:on
    }

    @Override
    protected void assertComparable(ComparableTestUtils<User> instance) {
        User user = mock(User.class);
        when(user.getName()).thenReturn("a", "A", "b", "B", "c", "C");

        // @formatter:off
        instance
            .assertBefore(user, user)
            .assertEqual(user, user)
            .assertAfter(user, user);
        // @formatter:on
    }

    @Override
    protected void assertToString(ToStringTestUtils instance) {
        instance.assertToString("avatarUrl", "login", "name", "organizations", "repositories");
    }

}
