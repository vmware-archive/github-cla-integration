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

package com.gopivotal.cla.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.gopivotal.cla.testutil.ToStringTestUtils;

public final class IndividualSignatoryTest extends AbstractModelTest<IndividualSignatory> {

    private static final Version VERSION = new Version(null, "test-name", null, null);

    @Override
    protected IndividualSignatory getInstance() {
        return setId(new IndividualSignatory(VERSION, "test-name", "test-email-address", "test-mailing-address", "test-country",
            "test-telephone-number"), Integer.MIN_VALUE + 3);
    }

    @Override
    protected void assertState(IndividualSignatory instance) {
        assertEquals((Integer) (Integer.MIN_VALUE + 3), instance.getId());
        assertEquals(VERSION, instance.getVersion());
        assertNotNull(instance.getSigningDate());
        assertEquals("test-name", instance.getName());
        assertEquals("test-email-address", instance.getEmailAddress());
        assertEquals("test-mailing-address", instance.getMailingAddress());
        assertEquals("test-country", instance.getCountry());
        assertEquals("test-telephone-number", instance.getTelephoneNumber());
    }

    @Override
    protected void assertToString(ToStringTestUtils instance) {
        instance.assertToString("id", "version", "signingDate", "name", "emailAddress", "mailingAddress", "country", "telephoneNumber");
    }

}
