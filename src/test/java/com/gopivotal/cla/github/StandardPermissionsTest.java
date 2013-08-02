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

import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.Map;

import com.gopivotal.cla.testutil.AbstractTypeTest;
import com.gopivotal.cla.testutil.ComparableTestUtils;
import com.gopivotal.cla.testutil.EqualsAndHashCodeTestUtils;
import com.gopivotal.cla.testutil.ToStringTestUtils;

public final class StandardPermissionsTest extends AbstractTypeTest<Permissions> {

    @Override
    protected Permissions getInstance() {
        return createPermissions(false, false, false);
    }

    @Override
    protected Permissions getInstanceWithNulls() {
        return null;
    }

    @Override
    protected void assertState(Permissions instance) {
        assertFalse(instance.isAdmin());
        assertFalse(instance.isPull());
        assertFalse(instance.isPush());
    }

    @Override
    protected void assertEqualsAndHashCode(EqualsAndHashCodeTestUtils<Permissions> instance, EqualsAndHashCodeTestUtils<Permissions> instanceWithNulls) {
        // @formatter:off
        instance
            .assertEqual(getInstance())
            .assertNotEqual(
                createPermissions(true, false, false),
                createPermissions(false, true, false),
                createPermissions(false, false, true));
        // @formatter:on
    }

    @Override
    protected void assertComparable(ComparableTestUtils<Permissions> instance) {
        // @formatter:off
        instance
            .assertEqual(createPermissions(false, false, false))
            .assertAfter(
                createPermissions(true, false, false),
                createPermissions(false, true, false),
                createPermissions(false, false, true));
        // @formatter:on
    }

    @Override
    protected void assertToString(ToStringTestUtils instance) {
        instance.assertToString("admin", "pull", "push");
    }

    private Permissions createPermissions(Boolean admin, Boolean push, Boolean pull) {
        Map<String, Boolean> raw = new HashMap<>();
        raw.put("admin", admin);
        raw.put("push", push);
        raw.put("pull", pull);

        return new StandardPermissions(raw);
    }

}
