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

import org.junit.Test;

import com.gopivotal.cla.testutil.ComparableTestUtils;
import com.gopivotal.cla.testutil.EqualsAndHashCodeTestUtils;
import com.gopivotal.cla.testutil.ToStringTestUtils;

public abstract class AbstractGitHubTest<T extends Comparable<T>> {

    @Test
    public final void testState() {
        assertState(getInstance());
    }

    @Test
    public final void testEqualsAndHashCode() {
        T instance = getInstance();
        EqualsAndHashCodeTestUtils<T> instanceTestUtils = new EqualsAndHashCodeTestUtils<>(instance);

        assertEqualsAndHashCode(instanceTestUtils);
    }

    @Test
    public final void testComparable() {
        assertComparable(new ComparableTestUtils<>(getInstance()));
    }

    @Test
    public final void testToString() {
        assertToString(new ToStringTestUtils(getInstance()));
    }

    protected abstract T getInstance();

    protected abstract void assertState(T instance);

    protected abstract void assertEqualsAndHashCode(EqualsAndHashCodeTestUtils<T> instance);

    protected abstract void assertComparable(ComparableTestUtils<T> instance);

    protected abstract void assertToString(ToStringTestUtils instance);

}
