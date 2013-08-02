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

public final class StandardOrganizationTest extends AbstractTypeTest<Organization> {

    @Override
    protected Organization getInstance() {
        Map<String, Object> raw = new HashMap<>();
        raw.put("login", "B");
        raw.put("repos_url", "test-repos-url");
        raw.put("url", "test-organization-url");

        Organization organization = new StandardOrganization(raw);
        ((StandardRepositories) organization.getRepositories()).initialize(Sets.asSet());

        return organization;
    }

    @Override
    protected Organization getInstanceWithNulls() {
        return null;
    }

    @Override
    protected void assertState(Organization instance) {
        assertEquals("B", instance.getName());
        assertNotNull(instance.getRepositories());
    }

    @Override
    protected void assertEqualsAndHashCode(EqualsAndHashCodeTestUtils<Organization> instance,
        EqualsAndHashCodeTestUtils<Organization> instanceWithNulls) {
        Organization organization = mock(Organization.class);
        when(organization.getName()).thenReturn("b", "A");

        // @formatter:off
        instance
            .assertEqual(
                getInstance(),
                organization)
            .assertNotEqual(organization);
        // @formatter:on
    }

    @Override
    protected void assertComparable(ComparableTestUtils<Organization> instance) {
        Organization organization = mock(Organization.class);
        when(organization.getName()).thenReturn("a", "A", "b", "B", "c", "C");

        // @formatter:off
        instance
            .assertBefore(organization, organization)
            .assertEqual(organization, organization)
            .assertAfter(organization, organization);
        // @formatter:on
    }

    @Override
    protected void assertToString(ToStringTestUtils instance) {
        instance.assertToString("name", "repositories");
    }

}
