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

import org.junit.Test;
import org.springframework.web.client.RestOperations;

public final class AbstractControllerTest {

    private final RestOperations restOperations = mock(RestOperations.class);

    private final StubController controller = new StubController(this.restOperations);

    @Test
    public void userInfo() {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("login", "test-login");

        when(this.restOperations.getForObject("https://api.github.com/user", Map.class)).thenReturn(userInfo);

        Map<String, Object> result = this.controller.userInfo();

        assertEquals(userInfo, result);
    }

    private static final class StubController extends AbstractController {

        private StubController(RestOperations restOperations) {
            super(restOperations);
        }

    }

}
