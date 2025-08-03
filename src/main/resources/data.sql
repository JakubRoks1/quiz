DELETE FROM quiz_questions;
DELETE FROM quiz;
DELETE FROM questions;

INSERT INTO questions (id, text, correct_answer) VALUES
                                                     (1, 'What is the capital of France?', 'Paris'),
                                                     (2, 'Which continent is the Sahara Desert located on?', 'Africa'),
                                                     (3, 'Which is the largest country in the world by area?', 'Russia'),
                                                     (4, 'What is the longest river in the world?', 'Nile'),
                                                     (5, 'Which ocean lies on the east coast of the United States?', 'Atlantic Ocean'),
                                                     (6, 'Mount Everest is located in which mountain range?', 'Himalayas'),
                                                     (7, 'What is the capital city of Australia?', 'Canberra'),
                                                     (8, 'Which country has the most islands in the world?', 'Sweden'),
                                                     (9, 'Which U.S. state is known as the ''Sunshine State''?', 'Florida'),
                                                     (10, 'What is the smallest country in the world?', 'Vatican City');

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