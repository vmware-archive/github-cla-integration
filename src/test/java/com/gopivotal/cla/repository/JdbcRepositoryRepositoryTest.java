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

package com.gopivotal.cla.repository;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Set;

import org.jasypt.util.text.TextEncryptor;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import com.gopivotal.cla.Agreement;
import com.gopivotal.cla.Repository;
import com.gopivotal.cla.repository.JdbcAgreementRepository;
import com.gopivotal.cla.repository.JdbcRepositoryRepository;

public final class JdbcRepositoryRepositoryTest extends AbstractJdbcRepositoryTest {

    @Autowired
    private volatile JdbcAgreementRepository agreementRepository;

    @Autowired
    private volatile JdbcRepositoryRepository repositoryRepository;

    @Autowired
    private volatile TextEncryptor textEncryptor;

    private volatile Agreement agreement;

    @Before
    public void createAgreement() {
        this.agreement = this.agreementRepository.create("test-name");
    }

    @Test
    public void find() {
        int initialSize = this.repositoryRepository.find().size();

        this.jdbcTemplate.update("INSERT INTO repositories(id, name, agreementId, accessToken) VALUES(?, ?, ?, ?)", Integer.MAX_VALUE - 1,
            "test-name-1", this.agreement.getId(), this.textEncryptor.encrypt("test-access-token-1"));
        this.jdbcTemplate.update("INSERT INTO repositories(id, name, agreementId, accessToken) VALUES(?, ?, ?, ?)", Integer.MAX_VALUE - 2,
            "test-name-2", this.agreement.getId(), this.textEncryptor.encrypt("test-access-token-2"));

        Set<Repository> repositories = this.repositoryRepository.find();
        assertEquals(initialSize + 2, repositories.size());
    }

    @Test
    public void create() {
        int initialSize = this.repositoryRepository.find().size();

        Repository repository = this.repositoryRepository.create("test-name", this.agreement.getId(), "test-access-token");

        assertEquals(initialSize + 1, this.repositoryRepository.find().size());

        Map<String, Object> row = this.jdbcTemplate.queryForMap("SELECT * FROM repositories WHERE id = ?", repository.getId());
        assertEquals("test-name", row.get("name"));
        assertEquals(this.agreement.getId(), new Long((int) row.get("agreementId")));
        assertEquals("test-access-token", this.textEncryptor.decrypt((String) row.get("accessToken")));

        assertEquals("test-name", repository.getName());
        assertEquals(this.agreement.getId(), repository.getAgreementId());
        assertEquals("test-access-token", repository.getAccessToken());
    }

    @Test
    public void read() {
        this.jdbcTemplate.update("INSERT INTO repositories(id, name, agreementId, accessToken) VALUES(?, ?, ?, ?)", Integer.MAX_VALUE - 1,
            "test-name", this.agreement.getId(), this.textEncryptor.encrypt("test-access-token"));

        Repository repository = this.repositoryRepository.read((long) Integer.MAX_VALUE - 1);

        assertEquals("test-name", repository.getName());
        assertEquals(this.agreement.getId(), repository.getAgreementId());
        assertEquals("test-access-token", repository.getAccessToken());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void readUnknownId() {
        this.repositoryRepository.read((long) Integer.MAX_VALUE);
    }

}
