INSERT INTO ROLE (name) VALUES
('ROLE_ADMIN'),         --1
('ROLE_USER');         --2

INSERT INTO USERS(id, first_name, last_name, email, custom_username, password, blocked_by_admin, deleted, role_id, created_at) VALUES
-- password: admin123
('af409c2d-95e0-432e-a6fc-6ef55cb4430d', 'Admin','Adminic', 'admin@gmail.com', 'AdminAdminic1234',
'$2a$12$XlgKd3zOFrYYrjANJQzYJOTxTtMptJ93ICmHvmrnidzWz.TbvzZMe', false, false, 1, '2024-01-25 00:00:00'),
-- password: pera123
('ff1d6606-e1f5-4e26-8a32-a14800b42a27', 'Pera','Peric', 'pera@gmail.com', 'PeraPeric1234',
'$2a$12$uIjkE3hHR5xMJFKEFcBqw.LpKXKIK7HWs6nYXC/foShQvNq673bH2', false, false, 2, '2024-01-25 00:00:00'),
-- password: mika123
('0e7f2a1d-49d0-44cd-8a01-4d40186f6f08', 'Mika','Mikic', 'mika@gmail.com', 'MikaMikic1234',
'$2a$12$3oftqT7voPybyTelelZotubxgrQPcKhyz3Cr6YWudJEyzBh319eLK', false, false, 2, '2024-01-25 00:00:00');


INSERT INTO REPO(id, name, description, owner_iD, is_public, created_at) VALUES
('a3826e27-77d8-465c-9d9f-87ccbb04ecaf', 'UKS-test', 'uks test opis',
'ff1d6606-e1f5-4e26-8a32-a14800b42a27', false, '2024-01-25 00:00:00'),
('ba6dcc79-1444-4310-9e7d-9736def57f60', 'UKS-test-PUBLIC', 'uks repo test public opis',
'ff1d6606-e1f5-4e26-8a32-a14800b42a27', true, '2024-01-25 00:00:00'),
('b6677751-cc42-4fb7-a662-31dda9c4482b', 'myPublicRepo', 'my public repo opis',
'0e7f2a1d-49d0-44cd-8a01-4d40186f6f08', true, '2024-01-25 00:00:00'),
('f30c955c-1fef-4ec5-a6f1-4477b8ff7f9e', 'myPrivateRepo', 'my private repo opis',
'0e7f2a1d-49d0-44cd-8a01-4d40186f6f08', false, '2024-01-25 00:00:00');

INSERT INTO MEMBER(repository_id, user_id, repository_role) VALUES
('a3826e27-77d8-465c-9d9f-87ccbb04ecaf', 'ff1d6606-e1f5-4e26-8a32-a14800b42a27', 'OWNER'),
('a3826e27-77d8-465c-9d9f-87ccbb04ecaf', '0e7f2a1d-49d0-44cd-8a01-4d40186f6f08', 'COLLABORATOR'),

('ba6dcc79-1444-4310-9e7d-9736def57f60', 'ff1d6606-e1f5-4e26-8a32-a14800b42a27', 'OWNER'),
('b6677751-cc42-4fb7-a662-31dda9c4482b', '0e7f2a1d-49d0-44cd-8a01-4d40186f6f08', 'OWNER'),
('f30c955c-1fef-4ec5-a6f1-4477b8ff7f9e', '0e7f2a1d-49d0-44cd-8a01-4d40186f6f08', 'OWNER');

INSERT INTO LABEL(repository_id, name, description, color) VALUES
('a3826e27-77d8-465c-9d9f-87ccbb04ecaf', 'feature', 'test description','#6bbf26'),
('a3826e27-77d8-465c-9d9f-87ccbb04ecaf', 'bug', 'test description2', '#e7e019');

INSERT INTO MILESTONE(repository_id, name, description, due_date, state) VALUES
('a3826e27-77d8-465c-9d9f-87ccbb04ecaf', 'mile stone', 'test milestone', '2024-01-25 23:59:59', 'CLOSE'),
('a3826e27-77d8-465c-9d9f-87ccbb04ecaf', 'milestone2', 'test milestone2', '2024-02-25 23:59:59', 'OPEN');

INSERT INTO ISSUE (id, name, description, state, repository_id,
created_at, author_id, milestone_id) VALUES
('4822a7d1-5a79-4444-9065-256643c80ffc', 'issue1', 'description1', 'OPEN', 'a3826e27-77d8-465c-9d9f-87ccbb04ecaf',
'2024-01-25 15:00:00', 'ff1d6606-e1f5-4e26-8a32-a14800b42a27', 1),
('9d75ceda-974a-4e04-88dc-9c6e455ddcd1', 'issue2', 'description2', 'CLOSE', 'a3826e27-77d8-465c-9d9f-87ccbb04ecaf',
'2024-01-25 15:01:00', 'ff1d6606-e1f5-4e26-8a32-a14800b42a27', 2);


INSERT INTO ITEM_LABELS(items_id, labels_id) VALUES
('4822a7d1-5a79-4444-9065-256643c80ffc', 2),
('4822a7d1-5a79-4444-9065-256643c80ffc', 1);

INSERT INTO ISSUE_PARTICIPANTS(issue_id, participants_id) VALUES
('4822a7d1-5a79-4444-9065-256643c80ffc', 'ff1d6606-e1f5-4e26-8a32-a14800b42a27'),
('4822a7d1-5a79-4444-9065-256643c80ffc', '0e7f2a1d-49d0-44cd-8a01-4d40186f6f08'),
('9d75ceda-974a-4e04-88dc-9c6e455ddcd1', 'ff1d6606-e1f5-4e26-8a32-a14800b42a27'),
('9d75ceda-974a-4e04-88dc-9c6e455ddcd1', '0e7f2a1d-49d0-44cd-8a01-4d40186f6f08');

INSERT INTO ITEM_ASSIGNEES(item_id, assignees_id) VALUES
('4822a7d1-5a79-4444-9065-256643c80ffc', '0e7f2a1d-49d0-44cd-8a01-4d40186f6f08'),
('9d75ceda-974a-4e04-88dc-9c6e455ddcd1', '0e7f2a1d-49d0-44cd-8a01-4d40186f6f08');


INSERT INTO ISSUE_EVENT (issue_id, author_id, new_value, type, created_at) VALUES
-- 1. issue pera-feature, mika-bug
('4822a7d1-5a79-4444-9065-256643c80ffc', 'ff1d6606-e1f5-4e26-8a32-a14800b42a27', 'feature', 'LABEL', '2024-01-25 15:00:00'),
('4822a7d1-5a79-4444-9065-256643c80ffc', '0e7f2a1d-49d0-44cd-8a01-4d40186f6f08', 'bug', 'LABEL', '2024-01-25 15:01:00'),
-- 2. issue pera-'mile stone', mika-'milestone2'
('9d75ceda-974a-4e04-88dc-9c6e455ddcd1', 'ff1d6606-e1f5-4e26-8a32-a14800b42a27', 'mile stone', 'MILESTONE', '2024-01-25 15:00:00'),
('9d75ceda-974a-4e04-88dc-9c6e455ddcd1', '0e7f2a1d-49d0-44cd-8a01-4d40186f6f08', 'milestone2', 'MILESTONE', '2024-01-25 15:01:00');
