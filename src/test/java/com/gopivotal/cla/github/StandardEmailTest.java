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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import com.gopivotal.cla.testutil.AbstractTypeTest;
import com.gopivotal.cla.testutil.ComparableTestUtils;
import com.gopivotal.cla.testutil.EqualsAndHashCodeTestUtils;
import com.gopivotal.cla.testutil.ToStringTestUtils;

public final class StandardEmailTest extends AbstractTypeTest<Email> {

    @Override
    protected Email getInstance() {
        return createEmail("B", true, false);
    }

    @Override
    protected Email getInstanceWithNulls() {
        return null;
    }

    @Override
    protected void assertState(Email instance) {
        assertEquals("B", instance.getAddress());
        assertTrue(instance.isPrimary());
        assertFalse(instance.isVerified());
    }

    @Override
    protected void assertEqualsAndHashCode(EqualsAndHashCodeTestUtils<Email> instance, EqualsAndHashCodeTestUtils<Email> instanceWithNulls) {
        // @formatter:off
        instance
            .assertEqual(
                getInstance(),
                createEmail("b", false, true))
            .assertNotEqual(createEmail("A", true, false));
        // @formatter:on
    }

    @Override
    protected void assertComparable(ComparableTestUtils<Email> instance) {
        // @formatter:off
        instance
            .assertBefore(
                createEmail("a", false, false),
                createEmail("A", false, false))
            .assertEqual(
                createEmail("b", false, false),
                createEmail("B", false, false))
            .assertAfter(
                createEmail("c", false, false),
                createEmail("C", false, false));
        // @formatter:on
    }

    @Override
    protected void assertToString(ToStringTestUtils instance) {
        instance.assertToString("address", "primary", "verified");
    }

    private Email createEmail(String address, Boolean primary, Boolean verified) {
        Map<String, Object> raw = new HashMap<>();
        raw.put("email", address);
        raw.put("primary", primary);
        raw.put("verified", verified);

        return new StandardEmail(raw);
    }

}
