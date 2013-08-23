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

public final class OrganizationTest extends AbstractGitHubTest<Organization> {

    @Override
    protected Organization getInstance() {
        Map<String, Object> raw = new HashMap<>();
        raw.put("login", "B");
        raw.put("repos_url", "test-repos-url");
        raw.put("url", "test-organization-url");

        Organization organization = new Organization(raw);
        organization.getRepositories().initialize(Sets.asSet());

        return organization;
    }

    @Override
    protected void assertState(Organization instance) {
        assertEquals("B", instance.getName());
        assertNotNull(instance.getRepositories());
    }

    @Override
    protected void assertEqualsAndHashCode(EqualsAndHashCodeTestUtils<Organization> instance) {
        // @formatter:off
        instance
            .assertEqual(
                getInstance(),
                new Organization("b", null))
            .assertNotEqual(new Organization("A", null));
        // @formatter:on
    }

    @Override
    protected void assertComparable(ComparableTestUtils<Organization> instance) {
        // @formatter:off
        instance
            .assertBefore(
                new Organization("a", null),
                new Organization("A", null))
            .assertEqual(
                new Organization("b", null),
                new Organization("B", null))
            .assertAfter(
                new Organization("c", null),
                new Organization("C", null));
        // @formatter:on
    }

    @Override
    protected void assertToString(ToStringTestUtils instance) {
        instance.assertToString("name", "repositories");
    }

}
