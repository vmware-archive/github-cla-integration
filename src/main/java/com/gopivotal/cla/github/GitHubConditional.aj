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

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

@Component
final aspect GitHubConditional {

    private static final Pattern NEXT_LINK = Pattern.compile("<(.*)>; rel=\"next\"");

    private static final HttpEntity<?> REQUEST_ENTITY;
    static {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType("application", "vnd.github.v3")));
        REQUEST_ENTITY = new HttpEntity<>(headers);
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private volatile RestOperations restOperations;

    pointcut fields(): get(private volatile * AbstractGitHubType+.*);

    @SuppressWarnings({ "rawtypes", "unchecked" })
    before(AbstractGitHubType instance) : this(instance) && fields() {
        if (isNull(instance, thisJoinPointStaticPart)) {
            ResponseEntity<?> response = getResponse(instance);
            instance.initialize(response.getBody());
        }
    }

    private boolean isNull(Object instance, JoinPoint.StaticPart staticPart) {
        try {
            Field field = getField(instance.getClass(), staticPart.getSignature().getName());
            AccessController.doPrivileged(new SetAccessiblePrivilegedAction(field));
            return field.get(instance) == null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Field getField(Class<?> klass, String name) {
        for (Field field : klass.getDeclaredFields()) {
            if(name.equals(field.getName())) {
                return field;
            }
        }
        
        return getField(klass.getSuperclass(), name);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private ResponseEntity<?> getResponse(AbstractGitHubType instance) {
        Class responseType = instance.getResponseType();
        RestOperations restOperations = this.restOperations;

        ResponseEntity<?> response = exchange(instance.getUrl(), REQUEST_ENTITY, responseType, restOperations);
        Object body = response.getBody();
        String nextUrl = getNextUrl(response.getHeaders().get("Link"));

        if (body instanceof Collection) {
            while (nextUrl != null) {
                ResponseEntity<?> nextResponse = exchange(nextUrl, REQUEST_ENTITY, responseType, restOperations);
                ((Collection) body).addAll((Collection) nextResponse.getBody());
                nextUrl = getNextUrl(nextResponse.getHeaders().get("Link"));
            }
        }

        return response;
    }

    private ResponseEntity<?> exchange(String url, HttpEntity<?> requestEntity, Class<?> responseType, RestOperations restOperations) {
        this.logger.debug("Executing a GET for {}", url);
        return restOperations.exchange(url, HttpMethod.GET, requestEntity, responseType);
    }

    private String getNextUrl(List<String> links) {
        if (links == null) {
            return null;
        }

        for (String link : links) {
            Matcher matcher = NEXT_LINK.matcher(link);

            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        return null;
    }

    public void setRestOperations(RestOperations restOperations) {
        this.restOperations = restOperations;
    }

    private static final class SetAccessiblePrivilegedAction implements PrivilegedAction<Void> {

        private final Field field;

        private SetAccessiblePrivilegedAction(Field field) {
            this.field = field;
        }

        @Override
        public Void run() {
            this.field.setAccessible(true);
            return null;
        }

    }

}
