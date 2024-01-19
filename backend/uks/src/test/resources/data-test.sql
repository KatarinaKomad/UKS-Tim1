CREATE CONSTANT ADMIN_USER_ID VALUE 'af409c2d-95e0-432e-a6fc-6ef55cb4430d';
CREATE CONSTANT PERA_USER_ID VALUE 'ff1d6606-e1f5-4e26-8a32-a14800b42a27';
CREATE CONSTANT MIKA_USER_ID VALUE '0e7f2a1d-49d0-44cd-8a01-4d40186f6f08';

CREATE CONSTANT BLOCKED_USER_ID VALUE '8e9b90cd-5517-4375-99ae-57eb5e4870e4';
CREATE CONSTANT DELETED_USER_ID VALUE 'ecf1c234-f25d-442f-b045-fbca70d4a86f';

CREATE CONSTANT REPOSITORY_ID_1_UKS_TEST VALUE 'a3826e27-77d8-465c-9d9f-87ccbb04ecaf';
CREATE CONSTANT REPOSITORY_ID_2_UKS_TEST VALUE 'ba6dcc79-1444-4310-9e7d-9736def57f60';
CREATE CONSTANT REPOSITORY_ID_3_UKS_TEST VALUE 'b6677751-cc42-4fb7-a662-31dda9c4482b';
CREATE CONSTANT REPOSITORY_ID_4_UKS_TEST VALUE 'f30c955c-1fef-4ec5-a6f1-4477b8ff7f9e';

------------------------------------------------------------------------------------------------------------------------


INSERT INTO ROLE (name) VALUES
('ROLE_ADMIN'),         --1
('ROLE_USER');         --2

INSERT INTO USERS(id, first_name, last_name, email, password, blocked_by_admin, deleted, role_id) VALUES
-- password: admin123
(ADMIN_USER_ID, 'Admin','Adminic', 'admin@gmail.com', '$2a$12$XlgKd3zOFrYYrjANJQzYJOTxTtMptJ93ICmHvmrnidzWz.TbvzZMe', false, false, 1),
-- password: pera123
(PERA_USER_ID, 'Pera','Peric', 'pera@gmail.com', '$2a$12$uIjkE3hHR5xMJFKEFcBqw.LpKXKIK7HWs6nYXC/foShQvNq673bH2', false, false, 2),
-- password: mika123
(MIKA_USER_ID, 'Mika','Mikic', 'mika@gmail.com', '$2a$12$3oftqT7voPybyTelelZotubxgrQPcKhyz3Cr6YWudJEyzBh319eLK', false, false, 2),

(BLOCKED_USER_ID, 'BLOCKED','BLOCKED', 'blocked@gmail.com', '$2a$12$3oftqT7voPybyTelelZotubxgrQPcKhyz3Cr6YWudJEyzBh319eLK', true, false, 2),
(DELETED_USER_ID, 'DELETED','DELETED', 'deleted@gmail.com', '$2a$12$3oftqT7voPybyTelelZotubxgrQPcKhyz3Cr6YWudJEyzBh319eLK', false, true, 2);


INSERT INTO REPO(id, name, owner_iD, is_public) VALUES
(REPOSITORY_ID_1_UKS_TEST, 'UKS-test', PERA_USER_ID, false),
(REPOSITORY_ID_2_UKS_TEST, 'UKS-test-PUBLIC', PERA_USER_ID, true),
(REPOSITORY_ID_3_UKS_TEST, 'myPublicRepo', MIKA_USER_ID, true),
(REPOSITORY_ID_4_UKS_TEST, 'myPrivateRepo', MIKA_USER_ID, false);

INSERT INTO MEMBER(repository_id, user_id, repository_role) VALUES
(REPOSITORY_ID_1_UKS_TEST, PERA_USER_ID, 'OWNER'),
(REPOSITORY_ID_1_UKS_TEST, MIKA_USER_ID, 'COLLABORATOR'),
(REPOSITORY_ID_2_UKS_TEST, PERA_USER_ID, 'OWNER'),
(REPOSITORY_ID_3_UKS_TEST, MIKA_USER_ID, 'OWNER'),
(REPOSITORY_ID_4_UKS_TEST, MIKA_USER_ID, 'OWNER');

INSERT INTO LABEL(repository_id, name, description, color) VALUES
(REPOSITORY_ID_1_UKS_TEST, 'test name', 'test description','#6bbf26'),
(REPOSITORY_ID_1_UKS_TEST, 'test name2', 'test description2', '#e7e019');


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