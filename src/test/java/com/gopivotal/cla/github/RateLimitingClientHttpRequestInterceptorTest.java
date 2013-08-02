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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;

public final class RateLimitingClientHttpRequestInterceptorTest {

    private final RateLimitingClientHttpRequestInterceptor interceptor = new RateLimitingClientHttpRequestInterceptor();

    @Test
    public void noBlock() throws IOException {
        MockClientHttpRequest request = new MockClientHttpRequest();
        MockClientHttpResponse response = new MockClientHttpResponse(new byte[0], HttpStatus.OK);
        ClientHttpRequestExecution execution = mock(ClientHttpRequestExecution.class);

        when(execution.execute(request, new byte[0])).thenReturn(response);

        this.interceptor.intercept(request, new byte[0], execution);
    }

    @Test
    public void block() throws InterruptedException, IOException {
        CountDownLatch latch = new CountDownLatch(1);

        MockClientHttpRequest request = new MockClientHttpRequest();
        MockClientHttpResponse response = new MockClientHttpResponse(new byte[0], HttpStatus.OK);
        ClientHttpRequestExecution execution = mock(ClientHttpRequestExecution.class);

        request.setMethod(HttpMethod.GET);
        request.setURI(URI.create("http://localhost"));

        when(execution.execute(request, new byte[0])).thenReturn(response);

        new Thread(new Trigger(this.interceptor, latch)).start();
        latch.await();

        this.interceptor.intercept(request, new byte[0], execution);
    }

    private static final class Trigger implements Runnable {

        private final ClientHttpRequestInterceptor interceptor;

        private final CountDownLatch latch;

        private Trigger(ClientHttpRequestInterceptor interceptor, CountDownLatch latch) {
            this.interceptor = interceptor;
            this.latch = latch;
        }

        @Override
        public void run() {
            MockClientHttpRequest request = new MockClientHttpRequest();
            MockClientHttpResponse response = new MockClientHttpResponse(new byte[0], HttpStatus.OK);
            ClientHttpRequestExecution execution = mock(ClientHttpRequestExecution.class);

            response.getHeaders().add("X-RateLimit-Remaining", "50");
            response.getHeaders().add("X-RateLimit-Reset", String.valueOf((System.currentTimeMillis() / 1000) + 1));

            try {
                when(execution.execute(request, new byte[0])).thenReturn(response);

                this.interceptor.intercept(request, new byte[0], execution);
            } catch (IOException e) {
                // TODO Auto-generated catch block
            } finally {
                this.latch.countDown();
            }
        }

    }
}
