DROP TABLE IF EXISTS accounts;

CREATE TABLE accounts(
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    email TEXT NOT NULL,
    phoneNumber TEXT NOT NULL,
    password TEXT NOT NULL
);

INSERT INTO accounts(name, email, phoneNumber, password) 
VALUES ('admin', 'admin@admin.com', 12345, 'admin');
    
INSERT INTO accounts(name, email, phoneNumber, password) 
VALUES ('john', 'john@test.com', 09876, 'john');

SELECT * FROM accounts;