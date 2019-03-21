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

package com.gopivotal.cla.testutil;

import static org.junit.Assert.assertTrue;

import org.springframework.util.StringUtils;

/**
 * A utility class for testing that the {@link Object#toString()} method on an instance works properly
 */
public final class ToStringTestUtils {

    private final Object control;

    /**
     * Creates an instance of this utility with an {@link Object} to use as a control instance
     * 
     * @param control The {@link Object} to use as a control instance
     */
    public ToStringTestUtils(Object control) {
        this.control = control;
    }

    /**
     * Asserts that an instance has a {@code toString()} method that renders the class name and the values of the
     * specified {@code fields}
     * 
     * @param fields The names of the fields to require in the {@code toString() method}
     */
    public void assertToString(String... keys) {
        String s = this.control.toString();

        assertTrue("control does not start with simple class name", s.startsWith(this.control.getClass().getSimpleName()));
        for (String key : keys) {
            boolean contains = s.contains(String.format("%s=", key)) || s.contains(String.format("get%s()=", StringUtils.capitalize(key)));
            assertTrue(String.format("control does not contain key '%s', '%s'", key, s), contains);
        }
    }
}
