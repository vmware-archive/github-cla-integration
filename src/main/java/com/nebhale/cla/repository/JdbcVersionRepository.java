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

import com.nebhale.cla.Version;

@Repository
final class JdbcVersionRepository implements VersionRepository {

    private static final RowMapper<Version> ROW_MAPPER = new VersionRowMapper();

    private final JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert createStatement;

    @Autowired
    JdbcVersionRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.createStatement = new SimpleJdbcInsert(dataSource).withTableName("versions").usingGeneratedKeyColumns("id");
    }

    @Override
    @Transactional(readOnly = true)
    public SortedSet<Version> find(Long agreementId) {
        return new TreeSet<>(this.jdbcTemplate.query("SELECT * FROM versions WHERE agreementId = ?", ROW_MAPPER, agreementId));
    }

    @Override
    @Transactional
    public Version create(Long agreementId, String version, String content) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("agreementId", agreementId);
        parameters.put("version", version);
        parameters.put("content", content);

        long id = this.createStatement.executeAndReturnKey(parameters).longValue();

        return read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Version read(Long id) {
        return this.jdbcTemplate.queryForObject("SELECT * FROM versions WHERE id = ?", ROW_MAPPER, id);
    }

    private static final class VersionRowMapper implements RowMapper<Version> {

        @Override
        public Version mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Version(rs.getLong(1), rs.getLong(2), rs.getString(3), rs.getString(4));
        }

    }

}
