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

import com.gopivotal.cla.github.GitHubClient;
import com.gopivotal.cla.github.User;

public final class AbstractControllerTest {

    private final GitHubClient gitHubClient = mock(GitHubClient.class);

    private final User user = mock(User.class);

    private final StubController controller = new StubController(this.gitHubClient);

    @Test
    public void user() {
        when(this.gitHubClient.getUser()).thenReturn(this.user);

        User result = this.controller.user();
        assertEquals(this.user, result);
    }

    private static final class StubController extends AbstractController {

        private StubController(GitHubClient gitHubClient) {
            super(gitHubClient);
        }

    }

}
