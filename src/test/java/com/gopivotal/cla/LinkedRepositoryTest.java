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

package com.gopivotal.cla;

import static org.junit.Assert.assertEquals;

import com.gopivotal.cla.testutil.AbstractTypeTest;
import com.gopivotal.cla.testutil.ComparableTestUtils;
import com.gopivotal.cla.testutil.EqualsAndHashCodeTestUtils;
import com.gopivotal.cla.testutil.ToStringTestUtils;

public final class LinkedRepositoryTest extends AbstractTypeTest<LinkedRepository> {

    private static final Agreement AGREEMENT = new Agreement(Long.MIN_VALUE, "test-agreement");

    @Override
    protected LinkedRepository getInstance() {
        return new LinkedRepository(Long.MIN_VALUE + 2, AGREEMENT, "B", "test-access-token");
    }

    @Override
    protected LinkedRepository getInstanceWithNulls() {
        return new LinkedRepository(null, null, null, null);
    }

    @Override
    protected void assertState(LinkedRepository instance) {
        assertEquals((Long) (Long.MIN_VALUE + 2), instance.getId());
        assertEquals(AGREEMENT, instance.getAgreement());
        assertEquals("B", instance.getName());
        assertEquals("test-access-token", instance.getAccessToken());
    }

    @Override
    protected void assertEqualsAndHashCode(EqualsAndHashCodeTestUtils<LinkedRepository> instance,
        EqualsAndHashCodeTestUtils<LinkedRepository> instanceWithNulls) {
        // @formatter:off
        instance
            .assertEqual(getInstance())
            .assertNotEqual(new LinkedRepository(Long.MIN_VALUE + 3, null, null, null));

        instanceWithNulls
            .assertEqual(getInstanceWithNulls())
            .assertNotEqual(new LinkedRepository(Long.MIN_VALUE + 3, null, null, null));
        // @formatter:on
    }

    @Override
    protected void assertComparable(ComparableTestUtils<LinkedRepository> instance) {
        // @formatter:off
        instance
            .assertBefore(
                new LinkedRepository(null, null, "a",null),
                new LinkedRepository(null, null, "A", null))
            .assertEqual(
                new LinkedRepository(null, null, "b", null),
                new LinkedRepository(null, null, "B",  null))
            .assertAfter(
                new LinkedRepository(null, null, "c",  null),
                new LinkedRepository(null, null, "C",  null));
        // @formatter:on
    }

    @Override
    protected void assertToString(ToStringTestUtils instance) {
        instance.assertToString("id", "agreement", "name", "accessToken");
    }
}
