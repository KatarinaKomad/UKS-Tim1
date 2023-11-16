INSERT INTO ROLE (name) VALUES
('ROLE_ADMIN'),         --1
('ROLE_OWNER'),         --2
('ROLE_COLLABORATOR'),  --3
('ROLE_CONTRIBUTOR');   --4

-- password: admin123
INSERT INTO USERS(id, first_name, last_name, name, email, password, blocked_by_admin, deleted) VALUES
('af409c2d-95e0-432e-a6fc-6ef55cb4430d', 'Admin','Adminic', 'Admin Adminic', 'admin@gmail.com',
'$2a$12$XlgKd3zOFrYYrjANJQzYJOTxTtMptJ93ICmHvmrnidzWz.TbvzZMe', false, false),

-- password: pera123
('ff1d6606-e1f5-4e26-8a32-a14800b42a27', 'Pera','Peric', 'Pera Peric', 'pera@gmail.com',
'$2a$12$uIjkE3hHR5xMJFKEFcBqw.LpKXKIK7HWs6nYXC/foShQvNq673bH2', false, false),

('0e7f2a1d-49d0-44cd-8a01-4d40186f6f08', 'Mika','Mikic', 'Mika Mikic', 'mika@gmail.com',
'$2a$12$3oftqT7voPybyTelelZotubxgrQPcKhyz3Cr6YWudJEyzBh319eLK', false, false);

INSERT INTO USER_ROLE(user_id, role_id) VALUES
('af409c2d-95e0-432e-a6fc-6ef55cb4430d', 1),
('ff1d6606-e1f5-4e26-8a32-a14800b42a27', 2),
('0e7f2a1d-49d0-44cd-8a01-4d40186f6f08', 3);
