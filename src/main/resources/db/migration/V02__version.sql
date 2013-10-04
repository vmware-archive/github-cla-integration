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

CREATE TABLE version (
  id          SERIAL PRIMARY KEY,
  agreementId INTEGER   NOT NULL,
  creation    TIMESTAMP NOT NULL,
  content     TEXT      NOT NULL,

  FOREIGN KEY (agreementId) REFERENCES agreement (id) ON DELETE CASCADE,
  CONSTRAINT versionCreation UNIQUE (creation)
);

INSERT INTO version (id, agreementId, creation, content)
VALUES (0, 0, current_time, '<p>In consideration of the opportunity to participate in the community of
    Pivotal. contributors, You accept and agree to the following terms and
    conditions for Your present and future Contributions submitted to Pivotal.
    Except for the license granted herein to Pivotal and recipients of software
    distributed by Pivotal, You reserve all right, title, and interest in and
    to Your Contributions.</p>
<ol>
    <li>
        <p>
            <strong>Definitions.</strong>"You" (or "Your") shall mean the copyright owner
            or legal entity authorized by the copyright owner that is making this Agreement
            with Pivotal. For legal entities, the entity making a Contribution and
            all other entities that control, are controlled by, or are under common
            control with that entity are considered to be a single Contributor. For
            the purposes of this definition, "control" means (i) the power, direct
            or indirect, to cause the direction or management of such entity, whether
            by contract or otherwise, or (ii) ownership of fifty percent (50%) or more
            of the outstanding shares, or (iii) beneficial ownership of such entity.
            <br>
            <br>"Contribution" shall mean any original work of authorship, including any
            modifications or additions to an existing work, that is intentionally submitted
            by You to Pivotal for inclusion in, or documentation of, any of the projects
            owned or managed by Pivotal (the "Project"). For the purposes of this definition,
            "submitted" means any form of electronic, verbal, or written communication
            sent to Pivotal or its representatives, including but not limited to communication
            on electronic mailing lists, source code control systems, and issue tracking
            systems that are managed by, or on behalf of, Pivotal for the purpose of
            discussing and improving the Project, but excluding communication that
            is conspicuously marked or otherwise designated in writing by You as "Not
            a Contribution."</p>
    </li>
    <li>
        <p>
            <strong>Grant of Copyright License.</strong>Subject to the terms and conditions
            of this Agreement, You hereby grant to Pivotal and to recipients of software
            distributed by Pivotal a perpetual, worldwide, non-exclusive, no-charge,
            royalty-free, irrevocable copyright license to reproduce, prepare derivative
            works of, publicly display, publicly perform, sublicense, and distribute
            Your Contributions and such derivative works.</p>
    </li>
    <li>
        <p>
            <strong>Grant of Patent License.</strong>Subject to the terms and conditions of
            this Agreement, You hereby grant to Pivotal and to recipients of software
            distributed by Pivotal a perpetual, worldwide, non-exclusive, no-charge,
            royalty-free, irrevocable (except as stated in this section), patent license
            to make, have made, use, offer to sell, sell, import, and otherwise transfer
            the Project, where such license applies only to those patent claims licensable
            by You that are necessarily infringed by Your Contribution(s) alone or
            by combination of Your Contribution(s) with the Project to which such Contribution(s)
            was submitted. If any entity institutes patent litigation against You or
            any other entity (including a cross-claim or counterclaim in a lawsuit)
            alleging that your Contribution, or the Project to which you have contributed,
            constitutes direct or contributory patent infringement, then any patent
            licenses granted to that entity under this Agreement for that Contribution
            or Project shall terminate as of the date such litigation is filed.</p>
    </li>
    <li>
        <p>You represent that you are legally entitled to grant the above license.
            If your employer(s) has rights to intellectual property that you create
            that includes your Contributions, you represent that you have received
            permission to make Contributions on behalf of that employer, that your
            employer has waived such rights for your Contributions to Pivotal, or that
            your employer has executed a separate Corporate CLA with Pivotal.</p>
    </li>
    <li>
        <p>You represent that each of Your Contributions is Your original creation
            (see section 7 for submissions on behalf of others). You represent that
            Your Contribution submissions include complete details of any third-party
            license or other restriction (including, but not limited to, related patents
            and trademarks) of which you are personally aware and which are associated
            with any part of Your Contributions.</p>
    </li>
    <li>
        <p>You are not expected to provide support for Your Contributions, except
            to the extent You desire to provide support. You may provide support for
            free, for a fee, or not at all. Unless required by applicable law or agreed
            to in writing, You provide Your Contributions on an "AS IS" BASIS, WITHOUT
            WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied, including,
            without limitation, any warranties or conditions of TITLE, NON-INFRINGEMENT,
            MERCHANTABILITY, or FITNESS FOR A PARTICULAR PURPOSE.</p>
    </li>
    <li>
        <p>Should You wish to submit work that is not Your original creation, You
            may submit it to Pivotal separately from any Contribution, identifying
            the complete details of its source and of any license or other restriction
            (including, but not limited to, related patents, trademarks, and license
            agreements) of which you are personally aware, and conspicuously marking
            the work as "Submitted on behalf of a third-party: [named here]".</p>
    </li>
    <li>
        <p>You agree to notify Pivotal of any facts or circumstances of which you
            become aware that would make these representations inaccurate in any respect.</p>
    </li>
    <li>
        <p>This Agreement is governed by the laws of the State of California, without
            regard to its choice of law provisions, and by the laws of the United States.
            This Agreement sets forth the entire understanding and agreement between
            the parties, and supersedes any previous communications, representations
            or agreements, whether oral or written, regarding the subject matter herein.</p>
    </li>');
