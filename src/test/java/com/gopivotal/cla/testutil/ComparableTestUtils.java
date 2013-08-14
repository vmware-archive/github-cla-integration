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

package com.gopivotal.cla.testutil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.mockito.Mockito;

/**
 * A utility class for testing that the {@link Comparable#compareTo(Object)} method on an instance works properly. This
 * utility class is designed such that chained invocation is possible. It is typically used in the following fashion:
 * 
 * <pre>
 * new ComparableTestUtils&lt;String&gt;(&quot;b&quot;) //
 * .assertBefore(&quot;a&quot;) //
 * .assertEqual(&quot;b&quot;) //
 * .assertAfter(&quot;c&quot;); //
 * </pre>
 */
public final class ComparableTestUtils<T extends Comparable<T>> {

    private final T control;

    /**
     * Creates an instance of this utility with an {@link Object} to use as a control instance. Ensures the following:
     * 
     * <ul>
     * <li>{@code control} returns {@code 0} when compared to itself
     * </ul>
     * 
     * @param control The {@link Object} to use as a control instance
     */
    public ComparableTestUtils(T control) {
        this.control = control;

        assertEquals("control returns non-zero when compared to itself", 0, control.compareTo(control));
    }

    /**
     * Asserts that an instance is "before" the control instance. This means the following:
     * 
     * <ul>
     * <li>{@code control} returns greater than zero when compared to {@code before}</li>
     * <li>{@code before} returns less than zero when compared to {@code control}</li>
     * </ul>
     * 
     * @param befores The instances that should be before the control instance
     * 
     * @return The called instance to allow changed invocations
     */
    @SuppressWarnings("unchecked")
    public ComparableTestUtils<T> assertBefore(T... befores) {
        for (T before : befores) {
            assertTrue(String.format("'%s' returns less than or equal to zero when compared to '%s'", this.control, before),
                this.control.compareTo(before) > 0);

            if (!Mockito.mockingDetails(before).isMock()) {
                assertTrue(String.format("'%s' returns greater than or equal to zero when compared to '%s'", before, this.control),
                    before.compareTo(this.control) < 0);
            }
        }

        return this;
    }

    /**
     * Asserts that an instance is "equal" to the control instance. This means the following:
     * 
     * <ul>
     * <li>{@code control} returns zero when compared to {@code equal}</li>
     * <li>{@code equal} returns zero when compared to {@code control}</li>
     * </ul>
     * 
     * @param equals The instances that should be equal to the control instance
     * 
     * @return The called instance to allow changed invocations
     */
    @SuppressWarnings("unchecked")
    public ComparableTestUtils<T> assertEqual(T... equals) {
        for (T equal : equals) {
            assertEquals(String.format("'%s' returns non-zero when compared to '%s'", this.control, equal), 0, this.control.compareTo(equal));

            if (!Mockito.mockingDetails(equal).isMock()) {
                assertEquals(String.format("'%s' returns non-zero when compared to '%s'", equal, this.control), 0, equal.compareTo(this.control));
            }
        }

        return this;
    }

    /**
     * Asserts that an instance is "after" the control instance. This means the following:
     * 
     * <ul>
     * <li>{@code control} returns less than zero when compared to {@code after}</li>
     * <li>{@code after} returns greater than zero when compared to {@code control}</li>
     * </ul>
     * 
     * @param after The instances that should be after the control instance
     * 
     * @return The called instance to allow changed invocations
     */
    @SuppressWarnings("unchecked")
    public ComparableTestUtils<T> assertAfter(T... afters) {
        for (T after : afters) {
            assertTrue(String.format("'%s' returns less than or equal to zero when compared to '%s'", this.control, after),
                this.control.compareTo(after) < 0);

            if (!Mockito.mockingDetails(after).isMock()) {
                assertTrue(String.format("'%s' returns greater than or equal to zero when compared to '%s'", after, this.control),
                    after.compareTo(this.control) > 0);
            }
        }

        return this;
    }
}
