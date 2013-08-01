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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gopivotal.cla.Agreement;

@Repository
final class JdbcAgreementRepository implements AgreementRepository {

    private static final RowMapper<Agreement> ROW_MAPPER = new AgreementRowMapper();

    private final JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert createStatement;

    @Autowired
    JdbcAgreementRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.createStatement = new SimpleJdbcInsert(dataSource).withTableName("agreements").usingGeneratedKeyColumns("id");
    }

    @Override
    @Transactional(readOnly = true)
    public SortedSet<Agreement> find() {
        return new TreeSet<>(this.jdbcTemplate.query("SELECT * FROM agreements", ROW_MAPPER));
    }

    @Override
    @Transactional
    public Agreement create(String name) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);

        long id = this.createStatement.executeAndReturnKey(parameters).longValue();

        return read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Agreement read(Long id) {
        return this.jdbcTemplate.queryForObject("SELECT * FROM agreements WHERE id = ?", ROW_MAPPER, id);
    }

    private static final class AgreementRowMapper implements RowMapper<Agreement> {

        @Override
        public Agreement mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Agreement(rs.getLong(1), rs.getString(2));
        }

    }

}
