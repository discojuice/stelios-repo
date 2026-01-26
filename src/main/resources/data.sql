-- ===============================
-- CLEAN EXISTING DATA (order matters because of FK)
-- ===============================
DELETE FROM BOOKS;
DELETE FROM AUTHORS;

-- Optional: reset identity counters in H2 (helps keep IDs neat)
ALTER TABLE BOOKS ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE AUTHORS ALTER COLUMN ID RESTART WITH 1;

-- ===============================
-- INSERT AUTHORS
-- ===============================
INSERT INTO AUTHORS (NAME) VALUES ('George Orwell');
INSERT INTO AUTHORS (NAME) VALUES ('J.K. Rowling');

-- ===============================
-- INSERT BOOKS (lookup author id by name)
-- ===============================
INSERT INTO BOOKS (TITLE, AUTHOR_ID)
SELECT '1984', ID FROM AUTHORS WHERE NAME='George Orwell';

INSERT INTO BOOKS (TITLE, AUTHOR_ID)
SELECT 'Animal Farm', ID FROM AUTHORS WHERE NAME='George Orwell';

INSERT INTO BOOKS (TITLE, AUTHOR_ID)
SELECT 'Harry Potter', ID FROM AUTHORS WHERE NAME='J.K. Rowling';



-- INSERT INTO AUTHORS (NAME) VALUES ('George Orwell');
-- INSERT INTO AUTHORS (NAME) VALUES ('J.K. Rowling');

-- INSERT INTO BOOKS (TITLE, AUTHOR_ID) VALUES ('1984', 1);
-- INSERT INTO BOOKS (TITLE, AUTHOR_ID) VALUES ('Animal Farm', 1);
-- INSERT INTO BOOKS (TITLE, AUTHOR_ID) VALUES ('Harry Potter', 2);
