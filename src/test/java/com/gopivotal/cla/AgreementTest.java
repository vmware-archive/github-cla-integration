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

public final class AgreementTest extends AbstractTypeTest<Agreement> {

    @Override
    protected Agreement getInstance() {
        return new Agreement(Long.MIN_VALUE, "B");
    }

    @Override
    protected Agreement getInstanceWithNulls() {
        return new Agreement(null, null);
    }

    @Override
    protected void assertState(Agreement instance) {
        assertEquals((Long) Long.MIN_VALUE, instance.getId());
        assertEquals("B", instance.getName());
    }

    @Override
    protected void assertEqualsAndHashCode(EqualsAndHashCodeTestUtils<Agreement> instance, EqualsAndHashCodeTestUtils<Agreement> instanceWithNulls) {
        // @formatter:off
        instance
            .assertEqual(getInstance())
            .assertNotEqual(new Agreement(Long.MIN_VALUE + 1, null));

        instanceWithNulls
            .assertEqual(getInstanceWithNulls())
            .assertNotEqual(new Agreement(Long.MIN_VALUE + 1, null));
        // @formatter:on
    }

    @Override
    protected void assertComparable(ComparableTestUtils<Agreement> instance) {
        // @formatter:off
        instance
            .assertBefore(
                new Agreement(null, "a"),
                new Agreement(null, "A"))
            .assertEqual(
                new Agreement(null, "b"),
                new Agreement(null, "B"))
            .assertAfter(
                new Agreement(null, "c"),
                new Agreement(null, "C"));
        // @formatter:on
    }

    @Override
    protected void assertToString(ToStringTestUtils instance) {
        instance.assertToString("id", "name");
    }

}
