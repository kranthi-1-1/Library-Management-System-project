create database Library_Management;
use Library_Management;
show databases;
CREATE TABLE member (
    member_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(15),
    department VARCHAR(50)
);
show tables;
desc member;
INSERT INTO member
(name, email, phone, department)
VALUES
('Rahul', 'rahul@gmail.com', '9876543210', 'CSE'),
('Priya', 'priya@gmail.com', '9876543211', 'ECE'),
('Arjun', 'arjun@gmail.com', '9876543212', 'CSE');
select * from member;
CREATE TABLE author (
    author_id INT PRIMARY KEY AUTO_INCREMENT,
    author_name VARCHAR(100) NOT NULL
);
INSERT INTO author (author_name)
VALUES
('Robert C. Martin'),
('Herbert Schildt'),
('Ramez Elmasri'),
('James Clear');
select * from author;
CREATE TABLE publisher (
    publisher_id INT PRIMARY KEY AUTO_INCREMENT,
    publisher_name VARCHAR(100) NOT NULL
);
INSERT INTO publisher (publisher_name)
VALUES
('Pearson'),
('McGraw Hill'),
('OReilly'),
('Penguin');
select * from publisher;
delete from publisher where publisher_id in(5,6,7,8);
select * from publisher;
CREATE TABLE category (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(50) NOT NULL
);
INSERT INTO category (category_name)
VALUES
('Programming'),
('Database'),
('Computer Science'),
('Self Help'),
('Fiction');
select * from category;
CREATE TABLE book (
    book_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(150) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    author_id INT,
    publisher_id INT,
    category_id INT,
    quantity INT DEFAULT 1,

    FOREIGN KEY (author_id)
        REFERENCES author(author_id),

    FOREIGN KEY (publisher_id)
        REFERENCES publisher(publisher_id),

    FOREIGN KEY (category_id)
        REFERENCES category(category_id)
);
INSERT INTO book
(title, isbn, author_id, publisher_id, category_id, quantity)
VALUES
('Clean Code', '9780132350884', 1, 3, 1, 5),
('Java: The Complete Reference', '9781260440232', 2, 2, 1, 4),
('Fundamentals of Database Systems', '9780133970777', 3, 1, 2, 3),
('Atomic Habits', '9780735211292', 4, 4, 4, 6);
select * from book;
SELECT
    b.book_id,
    b.title,
    a.author_name,
    p.publisher_name,
    c.category_name,
    b.quantity
FROM book b
JOIN author a
    ON b.author_id = a.author_id
JOIN publisher p
    ON b.publisher_id = p.publisher_id
JOIN category c
    ON b.category_id = c.category_id;
CREATE TABLE issue (
    issue_id INT PRIMARY KEY AUTO_INCREMENT,
    member_id INT NOT NULL,
    book_id INT NOT NULL,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,

    FOREIGN KEY (member_id) REFERENCES member(member_id),
    FOREIGN KEY (book_id) REFERENCES book(book_id)
);
INSERT INTO issue
(member_id, book_id, issue_date, due_date)
VALUES
(1, 1, '2026-10-07', '2026-10-14');
select * from issue;
SELECT
    i.issue_id,
    m.name AS member_name,
    b.title AS book_title,
    i.issue_date,
    i.due_date,
    i.return_date
FROM issue i
JOIN member m ON i.member_id = m.member_id
JOIN book b ON i.book_id = b.book_id;
CREATE TABLE fine (
    fine_id INT PRIMARY KEY AUTO_INCREMENT,
    issue_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL DEFAULT 0,
    paid_status BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (issue_id) REFERENCES issue(issue_id)
);
desc fine;
show tables;
SELECT book_id, title, quantity
FROM book
WHERE book_id = 1;
select * from issue;
UPDATE book
SET quantity = quantity - 1
WHERE book_id = 1;
SELECT book_id, title, quantity
FROM book
WHERE book_id = 1;
UPDATE issue
SET return_date = '2026-10-10'
WHERE issue_id = 1;
select * from issue;
UPDATE book
SET quantity = quantity + 1
WHERE book_id = 1;
SELECT book_id, title, quantity
FROM book
WHERE book_id = 1;
SELECT
    i.issue_id,
    m.name AS member_name,
    b.title AS book_title,
    i.issue_date,
    i.due_date,
    i.return_date
FROM issue i
JOIN member m ON i.member_id = m.member_id
JOIN book b ON i.book_id = b.book_id;
UPDATE issue
SET return_date = '2026-10-17'
WHERE issue_id = 1;
SELECT
    issue_id,
    due_date,
    return_date,
    DATEDIFF(return_date, due_date) AS late_days
FROM issue
WHERE issue_id = 1;
SELECT
    issue_id,
    DATEDIFF(return_date, due_date) * 10 AS fine_amount
FROM issue
WHERE issue_id = 1
AND return_date > due_date;
INSERT INTO fine
(issue_id, amount, paid_status)
VALUES
(1, 30.00, FALSE);
select * from fine;
UPDATE fine
SET paid_status = TRUE
WHERE fine_id = 1;
select * from fine;

select * from book;
select * from book where title like '%java%';
select book_id,title,quantity from book where quantity>0;
SELECT
    i.issue_id,
    m.name AS member_name,
    b.title AS book_title,
    i.issue_date,
    i.due_date
FROM issue i
JOIN member m
    ON i.member_id = m.member_id
JOIN book b
    ON i.book_id = b.book_id
WHERE i.return_date IS NOT NULL;
SELECT
    m.name AS member_name,
    b.title AS book_title,
    i.due_date,
    DATEDIFF(CURDATE(), i.due_date) AS overdue_days
FROM issue i
JOIN member m
    ON i.member_id = m.member_id
JOIN book b
    ON i.book_id = b.book_id
WHERE i.return_date IS NOT NULL
  AND i.due_date > CURDATE();
select count(*) from book;
select count(*) as total_members from member;
select sum(quantity) as total_books from book;
select avg(quantity) as average from book;
select max(quantity) as max_quantity from book;
select title,quantity as max_quantity from book where quantity=(select max(quantity) from book); 
select min(quantity) as min_quantity from book;
select title,quantity as min_quantity from book where quantity=(select min(quantity) from book); 
SELECT
    c.category_name,
    COUNT(b.book_id) AS number_of_books
FROM category c
LEFT JOIN book b
    ON c.category_id = b.category_id
GROUP BY c.category_id, c.category_name;
SELECT member_id, name
FROM member
WHERE member_id IN (
    SELECT member_id
    FROM issue
);
SELECT
    m.member_id,
    m.name,
    b.title AS book_title,
    i.issue_date
FROM member m
  LEFT JOIN issue i
    ON m.member_id = i.member_id
 LEFT JOIN book b
    ON i.book_id = b.book_id;
SELECT
    f.fine_id,
    m.name AS member_name,
    b.title AS book_title,
    f.amount,
    f.paid_status
FROM fine f
JOIN issue i
    ON f.issue_id = i.issue_id
JOIN member m
    ON i.member_id = m.member_id
JOIN book b
    ON i.book_id = b.book_id
WHERE f.paid_status = TRUE;

CREATE VIEW book_details AS
SELECT
    b.book_id,
    b.title,
    b.isbn,
    a.author_name,
    p.publisher_name,
    c.category_name,
    b.quantity
FROM book b
JOIN author a
    ON b.author_id = a.author_id
JOIN publisher p
    ON b.publisher_id = p.publisher_id
JOIN category c
    ON b.category_id = c.category_id;
select * from book_details;
USE library_management;

DELIMITER //

CREATE PROCEDURE SearchBook(IN search_title VARCHAR(150))
BEGIN
    SELECT *
    FROM book
    WHERE title LIKE CONCAT('%', search_title, '%');
END //

DELIMITER ;
call SearchBook('java');
call SearchBook('code');
DELIMITER //

CREATE TRIGGER decrease_book_quantity
AFTER INSERT ON issue
FOR EACH ROW
BEGIN
    UPDATE book
    SET quantity = quantity - 1
    WHERE book_id = NEW.book_id;
END //

DELIMITER ;
INSERT INTO issue
(member_id, book_id, issue_date, due_date)
VALUES
(2, 2, '2026-10-13', '2026-10-20');
SELECT book_id, title, quantity
FROM book
WHERE book_id = 2;
DELIMITER //

CREATE TRIGGER increase_book_quantity_after_return
AFTER UPDATE ON issue
FOR EACH ROW
BEGIN
IF OLD.return_date IS NULL AND NEW.return_date IS NOT NULL THEN
UPDATE book
SET quantity = quantity + 1
WHERE book_id = NEW.book_id;
END IF;
END //

DELIMITER ;
select * from issue;
update issue set return_date='2026-10-22' where issue_id=2; 
select * from issue;
SELECT book_id, title, quantity
FROM book
WHERE book_id = 2;
DELIMITER //

CREATE TRIGGER check_book_availability
BEFORE INSERT ON issue
FOR EACH ROW
BEGIN
IF COALESCE(
(SELECT quantity
FROM book
WHERE book_id = NEW.book_id),
0
) <= 0 THEN
SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT = 'Book is not available for issuing';
END IF;
END //

DELIMITER ;

DELIMITER //

CREATE FUNCTION CalculateFine(late_days INT)
RETURNS DECIMAL(10,2)
DETERMINISTIC
BEGIN
RETURN GREATEST(late_days, 0) * 10.00;
END //

DELIMITER ;
SELECT CalculateFine(2) AS fine;

SELECT book_id, title, quantity
FROM book;
start transaction;
update book set quantity=quantity-1 where book_id=3;
select book_id,title,quantity from book;
rollback;
select book_id,title,quantity from book;
select * from book;

CREATE TEMPORARY TABLE transaction_test (
    id INT PRIMARY KEY,
    quantity INT
);
INSERT INTO transaction_test (id, quantity)
VALUES (1, 5);
select * from transaction_test;
start transaction;
update transaction_test set quantity=6 where id=1;
select * from transaction_test;
commit;
rollback;
select * from transaction_test;

show tables;
desc member;
desc book; 
select * from member;
select count(*) from member;
select * from book;
select count(*) from book;
select * from issue;
select count(*) from issue;
select * from fine;
select count(*) from fine;
SELECT
    b.book_id,
    b.title,
    a.author_name,
    p.publisher_name,
    c.category_name,
    b.quantity
FROM book b
INNER JOIN author a
    ON b.author_id = a.author_id
INNER JOIN publisher p
    ON b.publisher_id = p.publisher_id
INNER JOIN category c
    ON b.category_id = c.category_id;
select * from book_details;
select m.member_id,m.name as member_name,i.issue_id,i.book_id,i.issue_date from member m left join issue i on m.member_id=i.member_id;
call SearchBook('database');
select CalculateFine(3) as fine;
SHOW PROCEDURE STATUS
WHERE Db = 'library_management';
SHOW FUNCTION STATUS
WHERE Db = 'library_management';
show triggers;
SHOW CREATE TRIGGER check_book_availability;
SHOW CREATE TRIGGER decrease_book_quantity;
SHOW CREATE TRIGGER increase_book_quantity_after_return;
show create table book;
show create table issue;
show create table fine;
SELECT book_id, title, quantity
FROM book
WHERE quantity < 0;
SELECT i.*
FROM issue i
LEFT JOIN member m
    ON i.member_id = m.member_id
LEFT JOIN book b
    ON i.book_id = b.book_id
WHERE m.member_id IS NULL
   OR b.book_id IS NULL;
   SELECT
    i.issue_id,
    m.name,
    b.title,
    i.issue_date,
    i.due_date
FROM issue i
JOIN member m
    ON i.member_id = m.member_id
JOIN book b
    ON i.book_id = b.book_id
WHERE i.return_date IS NULL;
SELECT
    m.name,
    b.title,
    i.due_date
FROM issue i
JOIN member m
    ON i.member_id = m.member_id
JOIN book b
    ON i.book_id = b.book_id
WHERE i.return_date IS NULL
  AND i.due_date < CURDATE();