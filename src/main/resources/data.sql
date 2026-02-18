DELETE FROM quiz_questions;
DELETE FROM quiz;
DELETE FROM questions;

INSERT INTO questions (id, text, correct_answer, difficulty) VALUES
                                                                 (1, 'What is the capital of France?', 'Paris', 'EASY'),
                                                                 (2, 'Which continent is the Sahara Desert located on?', 'Africa', 'EASY'),
                                                                 (3, 'Which is the largest country in the world by area?', 'Russia', 'MEDIUM'),
                                                                 (4, 'What is the longest river in the world?', 'Nile', 'MEDIUM'),
                                                                 (5, 'Which ocean lies on the east coast of the United States?', 'Atlantic Ocean', 'EASY'),
                                                                 (6, 'Mount Everest is located in which mountain range?', 'Himalayas', 'HARD'),
                                                                 (7, 'What is the capital city of Australia?', 'Canberra', 'MEDIUM'),
                                                                 (8, 'Which country has the most islands in the world?', 'Sweden', 'HARD'),
                                                                 (9, 'Which U.S. state is known as the ''Sunshine State''?', 'Florida', 'EASY'),
                                                                 (10, 'What is the smallest country in the world?', 'Vatican City', 'MEDIUM');

INSERT INTO quiz (id, title) VALUES (1, 'Geography Quiz');

INSERT INTO quiz_questions (quiz_id, questions_id) VALUES
                                                       (1, 1),
                                                       (1, 2),
                                                       (1, 3),
                                                       (1, 4),
                                                       (1, 5),
                                                       (1, 6),
                                                       (1, 7),
                                                       (1, 8),
                                                       (1, 9),
                                                       (1, 10);

INSERT INTO questions (id, text, correct_answer) VALUES
                                                     (11, 'What planet is known as the Red Planet?', 'Mars'),
                                                     (12, 'What is the chemical symbol for water?', 'H2O'),
                                                     (13, 'What gas do plants absorb from the atmosphere?', 'Carbon dioxide'),
                                                     (14, 'How many bones are in the adult human body?', '206'),
                                                     (15, 'What force keeps us on the ground?', 'Gravity'),
                                                     (16, 'What is the largest organ in the human body?', 'Skin'),
                                                     (17, 'What particle carries a negative charge?', 'Electron'),
                                                     (18, 'Who developed the theory of relativity?', 'Albert Einstein'),
                                                     (19, 'What is the boiling point of water at sea level in Celsius?', '100'),
                                                     (20, 'What is the process by which plants make their food?', 'Photosynthesis');

INSERT INTO quiz (id, title) VALUES (2, 'Science Quiz');

INSERT INTO quiz_questions (quiz_id, questions_id) VALUES
                                                       (2, 11),
                                                       (2, 12),
                                                       (2, 13),
                                                       (2, 14),
                                                       (2, 15),
                                                       (2, 16),
                                                       (2, 17),
                                                       (2, 18),
                                                       (2, 19),
                                                       (2, 20);

INSERT INTO questions (id, text, correct_answer, difficulty) VALUES
                                                                 (101, 'EASY', 'EASY', 'EASY'),
                                                                 (102, 'EASY', 'EASY', 'EASY'),
                                                                 (103, 'EASY', 'EASY', 'EASY'),
                                                                 (104, 'EASY', 'EASY', 'EASY'),
                                                                 (105, 'EASY', 'EASY', 'EASY'),
                                                                 (106, 'MEDIUM', 'MEDIUM', 'MEDIUM'),
                                                                 (107, 'MEDIUM', 'MEDIUM', 'MEDIUM'),
                                                                 (108, 'MEDIUM', 'MEDIUM', 'MEDIUM'),
                                                                 (109, 'MEDIUM', 'MEDIUM', 'MEDIUM'),
                                                                 (110, 'MEDIUM', 'MEDIUM', 'MEDIUM'),
                                                                 (111, 'HARD', 'HARD', 'HARD'),
                                                                 (112, 'HARD', 'HARD', 'HARD'),
                                                                 (113, 'HARD', 'HARD', 'HARD'),
                                                                 (114, 'HARD', 'HARD', 'HARD'),
                                                                 (115, 'HARD', 'HARD', 'HARD');


INSERT INTO quiz (id, title) VALUES (3, 'Difficulty Test Quiz');

INSERT INTO quiz_questions (quiz_id, questions_id) VALUES
                                                       (3, 101),
                                                       (3, 102),
                                                       (3, 103),
                                                       (3, 104),
                                                       (3, 105),
                                                       (3, 106),
                                                       (3, 107),
                                                       (3, 108),
                                                       (3, 109),
                                                       (3, 110),
                                                       (3, 111),
                                                       (3, 112),
                                                       (3, 113),
                                                       (3, 114),
                                                       (3, 115);
