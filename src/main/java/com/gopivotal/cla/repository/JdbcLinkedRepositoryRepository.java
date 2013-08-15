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

import org.jasypt.util.text.TextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.gopivotal.cla.Agreement;
import com.gopivotal.cla.LinkedRepository;

@org.springframework.stereotype.Repository
final class JdbcLinkedRepositoryRepository implements LinkedRepositoryRepository {

    private final JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert createStatement;

    private final TextEncryptor textEncryptor;

    private final RowMapper<LinkedRepository> rowMapper;

    @Autowired
    JdbcLinkedRepositoryRepository(DataSource dataSource, AgreementRepository agreementRepository, TextEncryptor textEncryptor) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.createStatement = new SimpleJdbcInsert(dataSource).withTableName("repositories").usingGeneratedKeyColumns("id");
        this.textEncryptor = textEncryptor;
        this.rowMapper = new RepositoryRowMapper(agreementRepository, textEncryptor);
    }

    @Override
    public SortedSet<LinkedRepository> find() {
        return new TreeSet<>(this.jdbcTemplate.query("SELECT * FROM repositories", this.rowMapper));
    }

    @Override
    public LinkedRepository create(String name, Long agreementId, String accessToken) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("agreementId", agreementId);
        parameters.put("accessToken", this.textEncryptor.encrypt(accessToken));

        long id = this.createStatement.executeAndReturnKey(parameters).longValue();

        return read(id);
    }

    @Override
    public LinkedRepository read(Long id) {
        return this.jdbcTemplate.queryForObject("SELECT * FROM repositories WHERE id = ?", this.rowMapper, id);
    }

    @Override
    public LinkedRepository read(String organization, String name) {
        return this.jdbcTemplate.queryForObject("SELECT * FROM repositories WHERE name = ?", this.rowMapper,
            String.format("%s/%s", organization, name));
    }

    private static final class RepositoryRowMapper implements RowMapper<LinkedRepository> {

        private final AgreementRepository agreementRepository;

        private final TextEncryptor textEncryptor;

        private RepositoryRowMapper(AgreementRepository agreementRepository, TextEncryptor textEncryptor) {
            this.agreementRepository = agreementRepository;
            this.textEncryptor = textEncryptor;
        }

        @Override
        public LinkedRepository mapRow(ResultSet rs, int rowNum) throws SQLException {
            Agreement agreement = this.agreementRepository.read(rs.getLong(3));
            return new LinkedRepository(rs.getLong(1), agreement, rs.getString(2), this.textEncryptor.decrypt(rs.getString(4)));
        }

    }

}
