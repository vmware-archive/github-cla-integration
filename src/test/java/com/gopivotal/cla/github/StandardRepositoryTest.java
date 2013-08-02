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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import com.gopivotal.cla.testutil.AbstractTypeTest;
import com.gopivotal.cla.testutil.ComparableTestUtils;
import com.gopivotal.cla.testutil.EqualsAndHashCodeTestUtils;
import com.gopivotal.cla.testutil.ToStringTestUtils;

public final class StandardRepositoryTest extends AbstractTypeTest<Repository> {

    @Override
    protected Repository getInstance() {
        Map<String, Object> raw = new HashMap<>();
        raw.put("full_name", "B");
        raw.put("name", "test-name");
        raw.put("url", "test-repository-url");

        Map<String, Boolean> rawPermissions = new HashMap<>();
        rawPermissions.put("admin", true);
        rawPermissions.put("push", false);
        rawPermissions.put("pull", true);
        raw.put("permissions", rawPermissions);

        return new StandardRepository(raw);
    }

    @Override
    protected Repository getInstanceWithNulls() {
        return null;
    }

    @Override
    protected void assertState(Repository instance) {
        assertEquals("B", instance.getFullName());
        assertEquals("test-name", instance.getName());
        assertEquals(new StubPermissions(true, false, true), instance.getPermissions());
    }

    @Override
    protected void assertEqualsAndHashCode(EqualsAndHashCodeTestUtils<Repository> instance, EqualsAndHashCodeTestUtils<Repository> instanceWithNulls) {
        Repository repository = mock(Repository.class);
        when(repository.getFullName()).thenReturn("b", "A");

        // @formatter:off
        instance
            .assertEqual(
                getInstance(),
                repository)
            .assertNotEqual(repository);
        // @formatter:on
    }

    @Override
    protected void assertComparable(ComparableTestUtils<Repository> instance) {
        Repository repository = mock(Repository.class);
        when(repository.getFullName()).thenReturn("a", "A", "b", "B", "c", "C");

        // @formatter:off
        instance
            .assertBefore(repository, repository)
            .assertEqual(repository, repository)
            .assertAfter(repository, repository);
        // @formatter:on
    }

    @Override
    protected void assertToString(ToStringTestUtils instance) {
        instance.assertToString("fullName", "name", "permissions");
    }

    private static final class StubPermissions implements Permissions {

        private final Boolean admin;

        private final Boolean push;

        private final Boolean pull;

        private StubPermissions(Boolean admin, Boolean push, Boolean pull) {
            this.admin = admin;
            this.push = push;
            this.pull = pull;
        }

        @Override
        public int compareTo(Permissions o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = (prime * result) + ((this.admin == null) ? 0 : this.admin.hashCode());
            result = (prime * result) + ((this.pull == null) ? 0 : this.pull.hashCode());
            result = (prime * result) + ((this.push == null) ? 0 : this.push.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof Permissions)) {
                return false;
            }
            Permissions other = (Permissions) obj;
            if (!this.admin.equals(other.isAdmin())) {
                return false;
            }
            if (!this.pull.equals(other.isPull())) {
                return false;
            }
            if (!this.push.equals(other.isPush())) {
                return false;
            }
            return true;
        }

        @Override
        public Boolean isAdmin() {
            return this.admin;
        }

        @Override
        public Boolean isPush() {
            return this.push;
        }

        @Override
        public Boolean isPull() {
            return this.pull;
        }

    }

}
