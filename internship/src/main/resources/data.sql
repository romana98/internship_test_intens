INSERT INTO candidates (full_name, date_of_birth, contact_number, email) values ('John Smith', '1997-06-17 00:00:00.000000','063505050', 'john.smith@email.com');
INSERT INTO candidates (full_name, date_of_birth, contact_number, email) values ('Robert Galbraith', '1992-05-26 00:00:00.000000','069404040', 'robert.galbraith@email.com');
INSERT INTO candidates (full_name, date_of_birth, contact_number, email) values ('Karen Maitland', '2000-03-15 00:00:00.000000','062303030', 'karen.maitland@email.com');

INSERT INTO skills (name) values ('Java programming');
INSERT INTO skills (name) values ('Python programming');
INSERT INTO skills (name) values ('C# programming');

INSERT INTO candidates_skills (candidate_id, skill_id) values (1, 1);
INSERT INTO candidates_skills (candidate_id, skill_id) values (1, 2);
INSERT INTO candidates_skills (candidate_id, skill_id) values (1, 3);

INSERT INTO candidates_skills (candidate_id, skill_id) values (2, 1);
INSERT INTO candidates_skills (candidate_id, skill_id) values (2, 3);