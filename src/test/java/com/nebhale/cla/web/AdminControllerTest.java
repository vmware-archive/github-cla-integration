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

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;
import org.springframework.ui.ModelMap;
import org.springframework.web.client.RestOperations;

import com.nebhale.cla.Agreement;
import com.nebhale.cla.Type;
import com.nebhale.cla.repository.AgreementRepository;

public final class AdminControllerTest {

    private final RestOperations restOperations = mock(RestOperations.class);

    private final AgreementRepository agreementRepository = mock(AgreementRepository.class);

    private final AdminController controller = new AdminController(this.agreementRepository, this.restOperations);

    @Test
    public void index() {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("login", "test-login");

        SortedSet<Agreement> agreements = new TreeSet<>();
        agreements.add(new Agreement(Long.MIN_VALUE, Type.INDIVIDUAL, "test-name"));

        ModelMap expected = new ModelMap();
        expected.putAll(userInfo);
        expected.put("agreements", agreements);

        when(this.restOperations.getForObject("https://api.github.com/user", Map.class)).thenReturn(userInfo);
        when(this.agreementRepository.find()).thenReturn(agreements);

        ModelMap model = new ModelMap();
        String result = this.controller.index(model);

        assertEquals("admin", result);
        assertEquals(expected, model);
    }

    @Test
    public void create() {
        when(this.agreementRepository.create(Type.INDIVIDUAL, "test-name")).thenReturn(new Agreement(Long.MIN_VALUE, Type.INDIVIDUAL, "test-name"));

        String result = this.controller.createAgreement(Type.INDIVIDUAL, "test-name");

        assertEquals("redirect:/admin/agreement/" + Long.MIN_VALUE, result);
    }

}
