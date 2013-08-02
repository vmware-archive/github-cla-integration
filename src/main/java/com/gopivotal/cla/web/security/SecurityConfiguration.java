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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import com.gopivotal.cla.github.GitHubClient;

/**
 * Configuration of security components
 */
@Configuration
@ComponentScan
@EnableWebSecurity
@ImportResource("classpath:META-INF/spring/security.xml")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String LOGIN_URL = "/login";

    private static final String LOGOUT_URL = "/logout";

    @Autowired
    private volatile OAuth2ClientContextFilter client;

    @Autowired
    private volatile OAuth2SsoFilter ssoFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .addFilterAfter(this.client, ExceptionTranslationFilter.class)
            .addFilterBefore(this.ssoFilter, FilterSecurityInterceptor.class)
            .logout()
                .logoutUrl(LOGOUT_URL)
                .logoutSuccessUrl("/")
                .and()
            .anonymous()
                .disable()
            .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(LOGIN_URL))
                .and()
            .authorizeUrls()
                .antMatchers("/admin/**").hasRole("ADMIN");
        // @formatter:on
    }

    @Override
    protected void registerAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication();
    }

    @Bean
    OAuth2SsoFilter ssoFilter(@Value("#{@adminEmailDomains}") String[] adminEmailDomains, GitHubClient gitHubClient) throws Exception {
        OAuth2SsoFilter filter = new OAuth2SsoFilter(adminEmailDomains, LOGIN_URL, gitHubClient);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

}
