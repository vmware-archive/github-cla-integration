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

package com.gopivotal.cla.web;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.springframework.ui.ModelMap;

import com.gopivotal.cla.Agreement;
import com.gopivotal.cla.LinkedRepository;
import com.gopivotal.cla.repository.LinkedRepositoryRepository;
import com.gopivotal.cla.util.Sets;

public final class RootControllerTest {

    private static final String ACCESS_TOKEN = "access-token";

    private static final Agreement AGREEMENT = new Agreement(Long.MIN_VALUE, "test-agreement");

    private static final LinkedRepository LINKED_REPOSITORY = new LinkedRepository(Long.MIN_VALUE + 1, AGREEMENT, "admin", ACCESS_TOKEN);

    private final LinkedRepositoryRepository linkedRepositoryRepository = mock(LinkedRepositoryRepository.class);

    private final RootController controller = new RootController(this.linkedRepositoryRepository);

    @Test
    public void index() {
        when(this.linkedRepositoryRepository.find()).thenReturn(Sets.asSortedSet(LINKED_REPOSITORY));

        ModelMap model = new ModelMap();
        String result = this.controller.index(model);

        assertEquals("index", result);
        assertEquals(Sets.asSortedSet(LINKED_REPOSITORY), model.get("linkedRepositories"));
    }

}
