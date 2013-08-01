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

package com.gopivotal.cla.web.security;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
final class RateLimitingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private static final int RATE_LIMIT_BUFFER = 100;

    private static final String REMAINING = "X-RateLimit-Remaining";

    private static final String RESET = "X-RateLimit-Reset";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Object monitor = new Object();

    private volatile long reset = 0;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        checkRateLimit(request);
        ClientHttpResponse response = execution.execute(request, body);
        updateRateLimit(response);

        return response;
    }

    private void checkRateLimit(HttpRequest request) {
        synchronized (this.monitor) {
            try {
                while (this.reset > System.currentTimeMillis()) {
                    this.logger.warn("Request '{}' blocked by rate limit until {}", getRequestString(request), new Date(this.reset));
                    this.monitor.wait();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                this.logger.warn("Request '{}' interrupted waiting for rate limit to expire", getRequestString(request));
                throw new RuntimeException(String.format("Request '%s' interrupted waiting for rate limit to expire", getRequestString(request)), e);
            }
        }
    }

    private void updateRateLimit(ClientHttpResponse response) {
        synchronized (this.monitor) {
            if (getRemaining(response) < RATE_LIMIT_BUFFER) {
                this.reset = getReset(response);
                new Thread(new RateLimitNotifier(this.reset, this.monitor), "Rate limit notifier " + new Date(this.reset)).start();
            }
        }
    }

    private int getRemaining(ClientHttpResponse response) {
        String remaining = response.getHeaders().getFirst(REMAINING);
        this.logger.debug("{} requests remaining before rate limit", remaining);
        return remaining != null ? Integer.parseInt(remaining) : Integer.MAX_VALUE;
    }

    private String getRequestString(HttpRequest request) {
        return String.format("%s %s", request.getMethod(), request.getURI());
    }

    private long getReset(ClientHttpResponse response) {
        String reset = response.getHeaders().getFirst(RESET);
        return reset != null ? Long.parseLong(reset) * 1000 : System.currentTimeMillis();
    }

    private static final class RateLimitNotifier implements Runnable {

        private final Logger logger = LoggerFactory.getLogger(this.getClass());

        private final long reset;

        private final Object monitor;

        private RateLimitNotifier(long reset, Object monitor) {
            this.reset = reset;
            this.monitor = monitor;
        }

        @Override
        public void run() {
            synchronized (this.monitor) {
                try {
                    while (System.currentTimeMillis() < this.reset) {
                        this.logger.warn("Approaching rate limit.  Blocking all outgoing connections until {}", new Date(this.reset));
                        Thread.sleep(this.reset - System.currentTimeMillis());
                    }

                    this.monitor.notifyAll();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    this.logger.warn("Rate limit notifier interrupted waiting for rate limit to exipre");
                }
            }
        }
    }

}
