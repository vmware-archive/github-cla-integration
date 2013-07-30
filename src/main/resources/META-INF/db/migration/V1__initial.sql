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

CREATE TYPE cla_type AS ENUM (
	'CORPORATE',
	'INDIVIDUAL'
);

CREATE TABLE agreements(
	id SERIAL PRIMARY KEY,
	name VARCHAR(128) NOT NULL,
	agreementType cla_type NOT NULL
);

CREATE TABLE versions(
	id SERIAL PRIMARY KEY,
	agreementId INTEGER NOT NULL,
	version VARCHAR(16) NOT NULL,
	content TEXT NOT NULL,
	
	FOREIGN KEY(agreementId) REFERENCES agreements(id) ON DELETE CASCADE
);
--
--CREATE TABLE repositories(
--	id SERIAL PRIMARY KEY,
--	versionId INTEGER NOT NULL,
--	organization VARCHAR(64) NOT NULL,
--	repository VARCHAR(64) NOT NULL,
--	token VARCHAR(64) NOT NULL,
--	
--	FOREIGN KEY(versionId) REFERENCES versions(id) ON DELETE CASCADE
--);
--
--CREATE TABLE email_addresses(
--	id SERIAL PRIMARY KEY,
--	repositoryId INTEGER NOT NULL,
--	address VARCHAR(128) NOT NULL,
--	
--	FOREIGN KEY(repositoryId) REFERENCES repositories(id) ON DELETE CASCADE
--);