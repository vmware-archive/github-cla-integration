/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gopivotal.cla.github;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import com.gopivotal.cla.testutil.ComparableTestUtils;
import com.gopivotal.cla.testutil.EqualsAndHashCodeTestUtils;
import com.gopivotal.cla.testutil.ToStringTestUtils;

public final class RepositoryTest extends AbstractGitHubTest<Repository> {

    @Override
    protected Repository getInstance() {
        Map<String, Object> raw = new HashMap<>();
        raw.put("full_name", "B");
        raw.put("name", "test-name");
        raw.put("url", "test-repository-url");

        Map<String, Boolean> rawPermissions = new HashMap<>();
        rawPermissions.put("admin", true);
        rawPermissions.put("push", false);
        rawPermissions.put("pull", true);
        raw.put("permissions", rawPermissions);

        return new Repository(raw);
    }

    @Override
    protected void assertState(Repository instance) {
        assertEquals("B", instance.getFullName());
        assertEquals("test-name", instance.getName());
        assertEquals(new Permissions(true, false, true), instance.getPermissions());
    }

    @Override
    protected void assertEqualsAndHashCode(EqualsAndHashCodeTestUtils<Repository> instance) {
        // @formatter:off
        instance
            .assertEqual(
                getInstance(),
                new Repository("b", null, null))
            .assertNotEqual(new Repository("A", null, null));
        // @formatter:on
    }

    @Override
    protected void assertComparable(ComparableTestUtils<Repository> instance) {
        // @formatter:off
        instance
            .assertBefore(
                new Repository("a", null, null),
                new Repository("A", null, null))
            .assertEqual(
                new Repository("b", null, null),
                new Repository("B", null, null))
            .assertAfter(
                new Repository("c", null, null),
                new Repository("C", null, null));
        // @formatter:on
    }

    @Override
    protected void assertToString(ToStringTestUtils instance) {
        instance.assertToString("fullName", "name", "permissions");
    }

}
