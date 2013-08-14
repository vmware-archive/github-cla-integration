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

/**
 * A interface to a service that takes raw Markdown and returns it rendered as HTML
 */
public interface MarkdownService {

    /**
     * Render raw Markdown into HTML
     * 
     * @param markdown The raw Markdown
     * @return The rendered HTML
     */
    String render(String markdown);
}
