CREATE CONSTANT ADMIN_USER_ID VALUE 'af409c2d-95e0-432e-a6fc-6ef55cb4430d';
CREATE CONSTANT PERA_USER_ID VALUE 'ff1d6606-e1f5-4e26-8a32-a14800b42a27';
CREATE CONSTANT MIKA_USER_ID VALUE '0e7f2a1d-49d0-44cd-8a01-4d40186f6f08';

CREATE CONSTANT BLOCKED_USER_ID VALUE '8e9b90cd-5517-4375-99ae-57eb5e4870e4';
CREATE CONSTANT DELETED_USER_ID VALUE 'ecf1c234-f25d-442f-b045-fbca70d4a86f';

CREATE CONSTANT REPOSITORY_ID_1_UKS_TEST VALUE 'a3826e27-77d8-465c-9d9f-87ccbb04ecaf';
CREATE CONSTANT REPOSITORY_ID_2_UKS_TEST VALUE 'ba6dcc79-1444-4310-9e7d-9736def57f60';
CREATE CONSTANT REPOSITORY_ID_3_UKS_TEST VALUE 'b6677751-cc42-4fb7-a662-31dda9c4482b';
CREATE CONSTANT REPOSITORY_ID_4_UKS_TEST VALUE 'f30c955c-1fef-4ec5-a6f1-4477b8ff7f9e';

CREATE CONSTANT ISSUE_ID_1 VALUE '4822a7d1-5a79-4444-9065-256643c80ffc';
CREATE CONSTANT ISSUE_ID_2 VALUE '9d75ceda-974a-4e04-88dc-9c6e455ddcd1';
------------------------------------------------------------------------------------------------------------------------


INSERT INTO ROLE (name) VALUES
('ROLE_ADMIN'),         --1
('ROLE_USER');         --2

INSERT INTO USERS(id, first_name, last_name, email, custom_username, password, blocked_by_admin, deleted, role_id) VALUES
-- password: admin123
(ADMIN_USER_ID, 'Admin','Adminic', 'admin@gmail.com', 'AdminAdminic1234', '$2a$12$XlgKd3zOFrYYrjANJQzYJOTxTtMptJ93ICmHvmrnidzWz.TbvzZMe', false, false, 1),
-- password: pera123
(PERA_USER_ID, 'Pera','Peric', 'pera@gmail.com', 'PeraPeric1234', '$2a$12$uIjkE3hHR5xMJFKEFcBqw.LpKXKIK7HWs6nYXC/foShQvNq673bH2', false, false, 2),
-- password: mika123
(MIKA_USER_ID, 'Mika','Mikic', 'mika@gmail.com', 'MikaMikic1234', '$2a$12$3oftqT7voPybyTelelZotubxgrQPcKhyz3Cr6YWudJEyzBh319eLK', false, false, 2),

(BLOCKED_USER_ID, 'BLOCKED','BLOCKED', 'blocked@gmail.com', 'BLOCKEDBLOCKED1234', '$2a$12$3oftqT7voPybyTelelZotubxgrQPcKhyz3Cr6YWudJEyzBh319eLK', true, false, 2),
(DELETED_USER_ID, 'DELETED','DELETED', 'deleted@gmail.com', 'DELETEDDELETED1234', '$2a$12$3oftqT7voPybyTelelZotubxgrQPcKhyz3Cr6YWudJEyzBh319eLK', false, true, 2);


INSERT INTO REPO(id, name, description, owner_iD, is_public, created_at) VALUES
(REPOSITORY_ID_1_UKS_TEST, 'UKS-test', 'uks test opis', PERA_USER_ID, false, '2024-01-22 00:00:00'),
(REPOSITORY_ID_2_UKS_TEST, 'UKS-test-PUBLIC', 'uks repo test public opis', PERA_USER_ID, true, '2024-01-23 00:00:00'),
(REPOSITORY_ID_3_UKS_TEST, 'myPublicRepo', 'my public repo opis', MIKA_USER_ID, true, '2024-01-24 00:00:00'),
(REPOSITORY_ID_4_UKS_TEST, 'myPrivateRepo', 'my private repo opis', MIKA_USER_ID, false, '2024-01-25 00:00:00');

INSERT INTO MEMBER(repository_id, user_id, repository_role) VALUES
(REPOSITORY_ID_1_UKS_TEST, PERA_USER_ID, 'OWNER'),
(REPOSITORY_ID_1_UKS_TEST, MIKA_USER_ID, 'COLLABORATOR'),
(REPOSITORY_ID_2_UKS_TEST, PERA_USER_ID, 'OWNER'),
(REPOSITORY_ID_3_UKS_TEST, MIKA_USER_ID, 'OWNER'),
(REPOSITORY_ID_4_UKS_TEST, MIKA_USER_ID, 'OWNER');

INSERT INTO LABEL(repository_id, name, description, color) VALUES
(REPOSITORY_ID_1_UKS_TEST, 'test name', 'test description','#6bbf26'),
(REPOSITORY_ID_1_UKS_TEST, 'test name2', 'test description2', '#e7e019');

INSERT INTO MILESTONE(repository_id, name, description, due_date, state) VALUES
(REPOSITORY_ID_1_UKS_TEST, 'mile stone', 'test milestone', '2024-01-25 23:59:59', 'CLOSE'),
(REPOSITORY_ID_1_UKS_TEST, 'milestone2', 'test milestone2', '2024-02-25 23:59:59', 'OPEN');


INSERT INTO ISSUE (id, name, description, state, repository_id, created_at, author_id, milestone_id) VALUES
(ISSUE_ID_1, 'issue1', 'description1', 'OPEN', REPOSITORY_ID_1_UKS_TEST, '2024-01-25 15:00:00', PERA_USER_ID, 1),
(ISSUE_ID_2, 'issue2', 'description2', 'CLOSE', REPOSITORY_ID_1_UKS_TEST, '2024-01-25 15:01:00', PERA_USER_ID, 2);


INSERT INTO ITEM_LABELS(items_id, labels_id) VALUES
(ISSUE_ID_1, 2),
(ISSUE_ID_1, 1);

INSERT INTO ISSUE_PARTICIPANTS(issue_id, participants_id) VALUES
(ISSUE_ID_1, PERA_USER_ID),
(ISSUE_ID_1, MIKA_USER_ID),
(ISSUE_ID_2, PERA_USER_ID),
(ISSUE_ID_2, MIKA_USER_ID);

INSERT INTO ITEM_ASSIGNEES(item_id, assignees_id) VALUES
(ISSUE_ID_1, PERA_USER_ID),
(ISSUE_ID_2, PERA_USER_ID);


INSERT INTO ISSUE_EVENT (issue_id, author_id, new_value, type, created_at) VALUES
-- 1. issue pera-feature, mika-bug
(ISSUE_ID_1, PERA_USER_ID, 'feature', 'LABEL', '2024-01-25 15:00:00'),
(ISSUE_ID_1, MIKA_USER_ID, 'bug', 'LABEL', '2024-01-25 15:01:00'),
-- 2. issue pera-'mile stone', mika-'milestone2'
(ISSUE_ID_2, PERA_USER_ID, 'mile stone', 'MILESTONE', '2024-01-25 15:00:00'),
(ISSUE_ID_2, MIKA_USER_ID, 'milestone2', 'MILESTONE', '2024-01-25 15:01:00');

INSERT INTO BRANCH (name, repository_id) VALUES ('main', REPOSITORY_ID_1_UKS_TEST);
INSERT INTO BRANCH (name, repository_id) VALUES ('dev', REPOSITORY_ID_1_UKS_TEST);

------------------------------------------------------------------------------------------------------------------------

DROP CONSTANT ADMIN_USER_ID;
DROP CONSTANT PERA_USER_ID;
DROP CONSTANT MIKA_USER_ID;

DROP CONSTANT BLOCKED_USER_ID;
DROP CONSTANT DELETED_USER_ID;

DROP CONSTANT REPOSITORY_ID_1_UKS_TEST;
DROP CONSTANT REPOSITORY_ID_2_UKS_TEST;
DROP CONSTANT REPOSITORY_ID_3_UKS_TEST;
DROP CONSTANT REPOSITORY_ID_4_UKS_TEST;

DROP CONSTANT ISSUE_ID_1;
DROP CONSTANT ISSUE_ID_2;