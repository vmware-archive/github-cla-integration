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

package com.nebhale.cla.github;

/**
 * A set of utility methods for dealing with the GitHub API
 */
public interface GitHubRestOperations {

    /**
     * Retrieve a representation by doing a GET on the specified path. The response (if any) is converted and returned.
     * <p>
     * URI Template variables are expanded using the given URI variables, if any.
     * 
     * @param path the path to be prepended with the API's base URI
     * @param responseType the type of the return value
     * @param uriVariables the variables to expand the template
     * @return the converted object
     * @see org.springframework.web.client.RestOperations#getForObject(String, Class, Object...)
     */
    <T> T getForObject(String path, Class<T> responseType, Object... uriVariables);

    /**
     * Retrieve a representation by doing a GET on the specified path using the GitHub V3 Accept header. The response
     * (if any) is converted and returned.
     * <p>
     * URI Template variables are expanded using the given URI variables, if any.
     * 
     * @param path the path to be prepended with the API's base URI
     * @param responseType the type of the return value
     * @param uriVariables the variables to expand the template
     * @return the converted object
     * @see org.springframework.web.client.RestOperations#getForObject(String, Class, Object...)
     */
    <T> T getForObjectV3(String path, Class<T> responseType, Object... uriVariables);

    /**
     * Create a new resource by POSTing the given object to the URI template, and returns the representation found in
     * the response.
     * <p>
     * URI Template variables are expanded using the given URI variables, if any.
     * <p>
     * The {@code request} parameter can be a {@link HttpEntity} in order to add additional HTTP headers to the request.
     * 
     * @param path the path to be prepended with the API's base URI
     * @param request the Object to be POSTed, may be {@code null}
     * @param responseType the type of the return value
     * @param uriVariables the variables to expand the template
     * @return the converted object
     * @see HttpEntity
     * @see org.springframework.web.client.RestOperations#postForObject(String, Object, Class, Object...)
     */
    <T> T postForObject(String path, Object request, Class<T> responseType, Object... uriVariables);

}