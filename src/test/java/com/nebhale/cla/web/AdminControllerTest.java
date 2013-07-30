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

package com.nebhale.cla.web;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;
import org.springframework.ui.ModelMap;
import org.springframework.web.client.RestOperations;

import com.nebhale.cla.Agreement;
import com.nebhale.cla.Type;
import com.nebhale.cla.Version;
import com.nebhale.cla.repository.AgreementRepository;
import com.nebhale.cla.repository.VersionRepository;

public final class AdminControllerTest {

    private static final Agreement AGREEMENT = new Agreement(Long.MIN_VALUE, Type.INDIVIDUAL, "test-name");

    private static final Version VERSION = new Version(Long.MIN_VALUE + 1, Long.MIN_VALUE, "test-version", "test-content");

    private final RestOperations restOperations = mock(RestOperations.class);

    private final AgreementRepository agreementRepository = mock(AgreementRepository.class);

    private final VersionRepository versionRepository = mock(VersionRepository.class);

    private final AdminController controller = new AdminController(this.restOperations, this.agreementRepository, this.versionRepository);

    @Test
    public void index() {
        SortedSet<Agreement> agreements = new TreeSet<>();
        agreements.add(AGREEMENT);

        when(this.agreementRepository.find()).thenReturn(agreements);

        ModelMap model = new ModelMap();
        String result = this.controller.index(model);

        assertEquals("admin", result);
        assertEquals(new ModelMap("agreements", agreements), model);
    }

    @Test
    public void createAgreement() {
        when(this.agreementRepository.create(Type.INDIVIDUAL, "test-name")).thenReturn(AGREEMENT);

        String result = this.controller.createAgreement(Type.INDIVIDUAL, "test-name");

        assertEquals("redirect:/admin/agreements/" + Long.MIN_VALUE, result);
    }

    @Test
    public void readAgreement() {
        when(this.agreementRepository.read(Long.MIN_VALUE)).thenReturn(AGREEMENT);

        ModelMap model = new ModelMap();
        String result = this.controller.readAgreement(Long.MIN_VALUE, model);

        assertEquals("agreement", result);
        assertEquals(AGREEMENT, model.get("agreement"));
    }

    @Test
    public void createVersion() {
        when(this.versionRepository.create(Long.MIN_VALUE, "test-version", "test-content")).thenReturn(VERSION);

        String result = this.controller.createVersion(Long.MIN_VALUE, "test-version", "test-content");

        assertEquals("redirect:/admin/agreements/" + Long.MIN_VALUE + "/versions/" + (Long.MIN_VALUE + 1), result);
    }
}
