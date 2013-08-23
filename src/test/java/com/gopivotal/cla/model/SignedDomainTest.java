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

public final class SignedDomainTest extends AbstractModelTest<SignedDomain> {

    private static final Agreement AGREEMENT = new Agreement("test-agreement");

    private static final CorporateSignatory CORPORATE_SIGNATORY = new CorporateSignatory(null, null, null, null, null, null, null, null);

    @Override
    protected SignedDomain getInstance() {
        return setId(new SignedDomain("test-domain", AGREEMENT, CORPORATE_SIGNATORY), Integer.MIN_VALUE + 6);
    }

    @Override
    protected void assertState(SignedDomain instance) {
        assertEquals((Integer) (Integer.MIN_VALUE + 6), instance.getId());
        assertEquals("test-domain", instance.getDomain());
        assertEquals(AGREEMENT, instance.getAgreement());
        assertEquals(CORPORATE_SIGNATORY, instance.getCorporateSignatory());
    }

    @Override
    protected void assertToString(ToStringTestUtils instance) {
        instance.assertToString("id", "domain", "agreement", "corporateSignatory");
    }

}
