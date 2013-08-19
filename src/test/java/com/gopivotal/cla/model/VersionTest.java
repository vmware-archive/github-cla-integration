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

public final class VersionTest extends AbstractModelTest<Version> {

    private static final Agreement AGREEMENT = new Agreement("test-agreement");

    @Override
    protected Version getInstance() {
        return setId(new Version(AGREEMENT, "B", "test-individual-content", "test-corporate-content"), Integer.MIN_VALUE + 1);
    }

    @Override
    protected void assertState(Version instance) {
        assertEquals((Integer) (Integer.MIN_VALUE + 1), instance.getId());
        assertEquals(AGREEMENT, instance.getAgreement());
        assertEquals("B", instance.getName());
        assertEquals("test-individual-content", instance.getIndividualAgreementContent());
        assertEquals("test-corporate-content", instance.getCorporateAgreementContent());
    }

    @Override
    protected void assertToString(ToStringTestUtils instance) {
        instance.assertToString("id", "agreement", "name", "individualAgreementContent", "corporateAgreementContent");
    }

}
