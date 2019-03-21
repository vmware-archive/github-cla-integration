/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

CREATE TABLE signatories(
	id SERIAL PRIMARY KEY,
	versionId SERIAL NOT NULL,
	signingDate INTEGER NOT NULL,
	name VARCHAR(128) NOT NULL,
	emailAddress VARCHAR(128) NOT NULL,
	mailingAddress VARCHAR(1024) NOT NULL,
	country VARCHAR(128) NOT NULL,
	telephoneNumber VARCHAR(64) NOT NULL,
	company VARCHAR(128),
	title VARCHAR(128),
	
	FOREIGN KEY(versionId) REFERENCES versions(id) ON DELETE CASCADE
);