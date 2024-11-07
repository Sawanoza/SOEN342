DROP TABLE IF EXISTS accounts;

CREATE TABLE accounts(
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    email TEXT NOT NULL,
    phoneNumber TEXT NOT NULL,
    password TEXT NOT NULL,
    role TEXT NOT NULL CHECK(role IN ('user', 'admin', 'instructor'))
);

INSERT INTO accounts(name, email, phoneNumber, password, role) 
VALUES ('admin', 'admin@admin.com', 1234567890, 'admin', 'admin');
    
INSERT INTO accounts(name, email, phoneNumber, password, role) 
VALUES ('paul', 'paul@test.com', 1111111111, 'paul', 'instructor');

INSERT INTO accounts(name, email, phoneNumber, password, role) 
VALUES ('john', 'john@test.com', 2222222222, 'john', 'user');

SELECT * FROM accounts;