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

CREATE TABLE signedAddresses(
	id SERIAL PRIMARY KEY,
	address VARCHAR(128) NOT NULL,
	agreementId SERIAL NOT NULL,
	signatoryId SERIAL NOT NULL,
	
	FOREIGN KEY(agreementId) REFERENCES agreements(id) ON DELETE CASCADE,
	FOREIGN KEY(signatoryId) REFERENCES signatories(id) ON DELETE CASCADE,
	CONSTRAINT signedAddressesTuple UNIQUE (address, agreementId)
);