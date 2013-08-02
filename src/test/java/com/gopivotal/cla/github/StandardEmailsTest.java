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

import org.junit.Before;
import org.junit.Test;

import com.gopivotal.cla.util.Sets;

public final class StandardEmailsTest {

    private final StandardEmails emails = new StandardEmails("test-emails-url");

    @Before
    public void initialize() {
        Map<String, Object> raw = new HashMap<>();
        raw.put("email", "test-email");
        raw.put("primary", true);
        raw.put("verified", true);

        this.emails.initialize(Sets.asSet(raw));
    }

    @Test
    public void test() {
        assertFalse(this.emails.isEmpty());
    }

}
