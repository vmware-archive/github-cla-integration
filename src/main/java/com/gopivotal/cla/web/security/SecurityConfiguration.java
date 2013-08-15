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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.access.ExceptionTranslationFilter;

/**
 * Configuration of security components
 */
@Configuration
@ComponentScan
@EnableWebSecurity
@ImportResource("classpath:META-INF/spring/security.xml")
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/", "/errors/**", "/resources/**");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return new AuthenticationManagerBuilder().inMemoryAuthentication().and().build();
    }

    @Override
    protected void registerAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication();
    }

    private static HttpSecurity ssoHttpConfiguration(HttpSecurity http, OAuth2ClientContextFilter client) throws Exception {
        // @formatter:off
        http
            .addFilterAfter(client, ExceptionTranslationFilter.class)
            .anonymous()
                .disable()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/");
        // @formatter:on

        return http;
    }

    @Configuration
    @Order(1)
    public static class AdminSecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        private volatile AdminEmailDomainFilter adminEmailDomainFilter;

        @Autowired
        private volatile OAuth2ClientContextFilter client;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            ssoHttpConfiguration(http, this.client)
                .requestMatchers()
                    .antMatchers("/agreements/**", "/repositories/**")
                    .and()
                .addFilterAfter(this.adminEmailDomainFilter, OAuth2ClientContextFilter.class)
                .exceptionHandling()
                    .accessDeniedPage("/errors/403");
            // @formatter:on
        }
    }

    @Configuration
    @Order(2)
    public static class SignatorySecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        private volatile OAuth2ClientContextFilter client;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            ssoHttpConfiguration(http, this.client);
        }
    }

}
