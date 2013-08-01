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

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import com.gopivotal.cla.Agreement;
import com.gopivotal.cla.Version;
import com.gopivotal.cla.repository.JdbcAgreementRepository;
import com.gopivotal.cla.repository.JdbcVersionRepository;

public final class JdbcVersionRepositoryTest extends AbstractJdbcRepositoryTest {

    @Autowired
    private volatile JdbcAgreementRepository agreementRepository;

    @Autowired
    private volatile JdbcVersionRepository versionRepository;

    private volatile Agreement agreement;

    @Before
    public void createAgreement() {
        this.agreement = this.agreementRepository.create("test-name");
    }

    @Test
    public void find() {
        int initialSize = this.versionRepository.find(this.agreement.getId()).size();

        this.jdbcTemplate.update("INSERT INTO versions(id, agreementId, name, individualContent, corporateContent) VALUES(?, ?, ?, ?, ?)",
            Integer.MAX_VALUE - 1, this.agreement.getId(), "test-name-1", "test-individual-content-1", "test-corporate-content-1");
        this.jdbcTemplate.update("INSERT INTO versions(id, agreementId, name, individualContent, corporateContent) VALUES(?, ?, ?, ?, ?)",
            Integer.MAX_VALUE - 2, this.agreement.getId(), "test-name-2", "test-individual-content-2", "test-corporate-content-2");

        Set<Version> versions = this.versionRepository.find(this.agreement.getId());
        assertEquals(initialSize + 2, versions.size());
    }

    @Test
    public void create() {
        int initialSize = this.versionRepository.find(this.agreement.getId()).size();

        Version version = this.versionRepository.create(this.agreement.getId(), "test-name", "test-individual-content", "test-corporate-content");

        assertEquals(initialSize + 1, this.versionRepository.find(this.agreement.getId()).size());

        Map<String, Object> row = this.jdbcTemplate.queryForMap("SELECT * FROM versions WHERE id = ?", version.getId());
        assertEquals(this.agreement.getId(), new Long((int) row.get("agreementId")));
        assertEquals("test-name", row.get("name"));
        assertEquals("test-individual-content", row.get("individualContent"));
        assertEquals("test-corporate-content", row.get("corporateContent"));

        assertEquals(this.agreement.getId(), version.getAgreementId());
        assertEquals("test-name", version.getName());
        assertEquals("test-individual-content", version.getIndividualAgreementContent());
        assertEquals("test-corporate-content", version.getCorporateAgreementContent());
    }

    @Test
    public void read() {
        this.jdbcTemplate.update("INSERT INTO versions(id, agreementId, name, individualContent, corporateContent) VALUES(?, ?, ?, ?, ?)",
            Integer.MAX_VALUE - 1, this.agreement.getId(), "test-name", "test-individual-content", "test-corporate-content");

        Version version = this.versionRepository.read((long) Integer.MAX_VALUE - 1);

        assertEquals(this.agreement.getId(), version.getAgreementId());
        assertEquals("test-name", version.getName());
        assertEquals("test-individual-content", version.getIndividualAgreementContent());
        assertEquals("test-corporate-content", version.getCorporateAgreementContent());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void readUnknownId() {
        this.versionRepository.read((long) Integer.MAX_VALUE);
    }

}
