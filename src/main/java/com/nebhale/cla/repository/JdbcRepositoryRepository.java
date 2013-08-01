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

import org.jasypt.util.text.TextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.nebhale.cla.Repository;

@org.springframework.stereotype.Repository
final class JdbcRepositoryRepository implements RepositoryRepository {

    private final RowMapper<Repository> rowMapper;

    private final JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert createStatement;

    private final TextEncryptor textEncryptor;

    @Autowired
    JdbcRepositoryRepository(DataSource dataSource, TextEncryptor textEncryptor) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.createStatement = new SimpleJdbcInsert(dataSource).withTableName("repositories").usingGeneratedKeyColumns("id");
        this.textEncryptor = textEncryptor;
        this.rowMapper = new RepositoryRowMapper(textEncryptor);
    }

    @Override
    public SortedSet<Repository> find() {
        return new TreeSet<>(this.jdbcTemplate.query("SELECT * FROM repositories", this.rowMapper));
    }

    @Override
    public Repository create(String name, Long agreementId, String accessToken) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("agreementId", agreementId);
        parameters.put("accessToken", this.textEncryptor.encrypt(accessToken));

        long id = this.createStatement.executeAndReturnKey(parameters).longValue();

        return read(id);
    }

    @Override
    public Repository read(Long id) {
        return this.jdbcTemplate.queryForObject("SELECT * FROM repositories WHERE id = ?", this.rowMapper, id);
    }

    private static final class RepositoryRowMapper implements RowMapper<Repository> {

        private final TextEncryptor textEncryptor;

        private RepositoryRowMapper(TextEncryptor textEncryptor) {
            this.textEncryptor = textEncryptor;
        }

        @Override
        public Repository mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Repository(rs.getLong(1), rs.getString(2), rs.getLong(3), this.textEncryptor.decrypt(rs.getString(4)));
        }

    }

}
