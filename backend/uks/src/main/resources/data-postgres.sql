INSERT INTO ROLE (name) VALUES
('ROLE_ADMIN'),         --1
('ROLE_USER');         --2

INSERT INTO USERS(id, first_name, last_name, email, password, blocked_by_admin, deleted, role_id) VALUES
-- password: admin123
('af409c2d-95e0-432e-a6fc-6ef55cb4430d', 'Admin','Adminic', 'admin@gmail.com',
'$2a$12$XlgKd3zOFrYYrjANJQzYJOTxTtMptJ93ICmHvmrnidzWz.TbvzZMe', false, false, 1),
-- password: pera123
('ff1d6606-e1f5-4e26-8a32-a14800b42a27', 'Pera','Peric', 'pera@gmail.com',
'$2a$12$uIjkE3hHR5xMJFKEFcBqw.LpKXKIK7HWs6nYXC/foShQvNq673bH2', false, false, 2),
-- password: mika123
('0e7f2a1d-49d0-44cd-8a01-4d40186f6f08', 'Mika','Mikic', 'mika@gmail.com',
'$2a$12$3oftqT7voPybyTelelZotubxgrQPcKhyz3Cr6YWudJEyzBh319eLK', false, false, 2);


INSERT INTO REPO(id, name, owner_iD, is_public) VALUES
('a3826e27-77d8-465c-9d9f-87ccbb04ecaf', 'UKS-test', 'ff1d6606-e1f5-4e26-8a32-a14800b42a27', false),
('ba6dcc79-1444-4310-9e7d-9736def57f60', 'UKS-test-PUBLIC', 'ff1d6606-e1f5-4e26-8a32-a14800b42a27', true),
('b6677751-cc42-4fb7-a662-31dda9c4482b', 'myPublicRepo', '0e7f2a1d-49d0-44cd-8a01-4d40186f6f08', true),
('f30c955c-1fef-4ec5-a6f1-4477b8ff7f9e', 'myPrivateRepo', '0e7f2a1d-49d0-44cd-8a01-4d40186f6f08', false);

INSERT INTO MEMBER(repository_id, user_id, repository_role) VALUES
('a3826e27-77d8-465c-9d9f-87ccbb04ecaf', 'ff1d6606-e1f5-4e26-8a32-a14800b42a27', 'OWNER'),
('a3826e27-77d8-465c-9d9f-87ccbb04ecaf', '0e7f2a1d-49d0-44cd-8a01-4d40186f6f08', 'COLLABORATOR'),

('ba6dcc79-1444-4310-9e7d-9736def57f60', 'ff1d6606-e1f5-4e26-8a32-a14800b42a27', 'OWNER'),
('b6677751-cc42-4fb7-a662-31dda9c4482b', '0e7f2a1d-49d0-44cd-8a01-4d40186f6f08', 'OWNER'),
('f30c955c-1fef-4ec5-a6f1-4477b8ff7f9e', '0e7f2a1d-49d0-44cd-8a01-4d40186f6f08', 'OWNER');
