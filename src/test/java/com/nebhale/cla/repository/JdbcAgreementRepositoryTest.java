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

package com.nebhale.cla.repository;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Set;

import org.junit.Test;
//import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import com.nebhale.cla.Agreement;
import com.nebhale.cla.Type;

public final class JdbcAgreementRepositoryTest extends AbstractJdbcRepositoryTest {

    @Autowired
    private volatile JdbcAgreementRepository agreementRepository;

    @Test
    public void find() {
        int initialSize = this.agreementRepository.find().size();

        this.jdbcTemplate.update("INSERT INTO agreements(id, name, agreementType) VALUES(?, ?, ?::cla_type)", Integer.MAX_VALUE, "test-name-1",
            Type.INDIVIDUAL.toString());
        this.jdbcTemplate.update("INSERT INTO agreements(id, name, agreementType) VALUES(?, ?, ?::cla_type)", Integer.MAX_VALUE - 1, "test-name-2",
            Type.CORPORATE.toString());

        Set<Agreement> agreements = this.agreementRepository.find();
        assertEquals(initialSize + 2, agreements.size());
    }

    @Test
    public void create() {
        int initialSize = this.agreementRepository.find().size();

        Agreement agreement = this.agreementRepository.create(Type.CORPORATE, "test-name");

        assertEquals(initialSize + 1, this.agreementRepository.find().size());

        Map<String, Object> row = this.jdbcTemplate.queryForMap("SELECT name, agreementType FROM agreements WHERE id = ?", agreement.getId());
        assertEquals("test-name", row.get("name"));
        // assertEquals(Type.CORPORATE.toString(), ((PGobject) row.get("agreementType")).toString());

        assertEquals(Type.CORPORATE, agreement.getType());
        assertEquals("test-name", agreement.getName());
    }

    @Test
    public void read() {
        this.jdbcTemplate.update("INSERT INTO agreements(id, name, agreementType) VALUES(?, ?, ?::cla_type)", Integer.MAX_VALUE, "test-name",
            Type.INDIVIDUAL.toString());

        Agreement agreement = this.agreementRepository.read((long) Integer.MAX_VALUE);

        assertEquals("test-name", agreement.getName());
        assertEquals(Type.INDIVIDUAL, agreement.getType());
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void readUnknownId() {
        this.agreementRepository.read((long) Integer.MAX_VALUE);
    }
}
