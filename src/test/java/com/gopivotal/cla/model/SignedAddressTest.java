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

import com.gopivotal.cla.testutil.ToStringTestUtils;

public final class SignedAddressTest extends AbstractModelTest<SignedAddress> {

    private static final Agreement AGREEMENT = new Agreement("test-agreement");

    private static final IndividualSignatory INDIVIDUAL_SIGNATORY = new IndividualSignatory(null, null, null, null, null, null);

    @Override
    protected SignedAddress getInstance() {
        return setId(new SignedAddress("test-address", AGREEMENT, INDIVIDUAL_SIGNATORY), Integer.MIN_VALUE + 5);
    }

    @Override
    protected void assertState(SignedAddress instance) {
        assertEquals((Integer) (Integer.MIN_VALUE + 5), instance.getId());
        assertEquals("test-address", instance.getAddress());
        assertEquals(AGREEMENT, instance.getAgreement());
        assertEquals(INDIVIDUAL_SIGNATORY, instance.getIndividualSignatory());
    }

    @Override
    protected void assertToString(ToStringTestUtils instance) {
        instance.assertToString("id", "address", "agreement", "individualSignatory");
    }

}
