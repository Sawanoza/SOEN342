DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS client;
DROP TABLE IF EXISTS instructor;
DROP TABLE IF EXISTS admin;

-- TEMP
DROP TABLE IF EXISTS offering;
DROP TABLE IF EXISTS booking;

CREATE TABLE accounts (
    accountId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    age INTEGER NOT NULL,
    email TEXT NOT NULL,
    phoneNumber TEXT NOT NULL,
    password TEXT NOT NULL
);

CREATE TABLE client (
    clientId INTEGER PRIMARY KEY AUTOINCREMENT,
    accountId INTEGER,
    guardianAccountId INTEGER,
    FOREIGN KEY (accountId) REFERENCES accounts(accountId),
    FOREIGN KEY (guardianAccountId) REFERENCES client(clientId)
);

CREATE TABLE instructor (
    instructorId INTEGER PRIMARY KEY AUTOINCREMENT,
    accountId INTEGER NOT NULL,
    availability TEXT NOT NULL,
    speciality TEXT NOT NULL,
    FOREIGN KEY (accountId) REFERENCES accounts(accountId)
);

CREATE TABLE admin (
    adminId INTEGER PRIMARY KEY AUTOINCREMENT,
    accountId INTEGER NOT NULL,
    FOREIGN KEY (accountId) REFERENCES accounts(accountId)
);

-- TEMPORARY
CREATE TABLE offering (
    offeringId INTEGER PRIMARY KEY AUTOINCREMENT,
    status TEXT NOT NULL,
    lessonType TEXT NOT NULL,
    available BOOLEAN NOT NULL,
    instructorId INTEGER NOT NULL,
    FOREIGN KEY (instructorId) REFERENCES instructor(instructorId)
    -- ADD SCHEDULED FOREIGN KEY
);

-- TEMP
CREATE TABLE booking (
    bookingId INTEGER PRIMARY KEY AUTOINCREMENT,
    clientId INTEGER NOT NULL,
    offeringId INTEGER NOT NULL,
    FOREIGN KEY (clientId) REFERENCES client(clientId),
    FOREIGN KEY (offeringId) REFERENCES offering(offeringId)
);



INSERT INTO accounts(name, age, email, phoneNumber, password) 
VALUES ('admin', 0, 'admin', '0000000000', 'admin');

INSERT INTO accounts(name, age, email, phoneNumber, password) 
VALUES ('instructor', 1, 'instructor', '1111111111', 'instructor');

INSERT INTO accounts(name, age, email, phoneNumber, password) 
VALUES ('paul', 30, 'paul@test.com', '1231231234', 'paul');

INSERT INTO accounts(name, age, email, phoneNumber, password) 
VALUES ('john', 25, 'john@test.com', '1231231234', 'john');




INSERT INTO client(accountId, guardianAccountId)
SELECT accountId, 0
FROM accounts
WHERE name = 'paul';  

INSERT INTO client(accountId, guardianAccountId)
SELECT accountId, 0
FROM accounts
WHERE name = 'john';  



INSERT INTO instructor(accountId, availability, speciality)
SELECT accountId, 'M-F 9am-5pm', 'Yoga'
FROM accounts
WHERE name = 'instructor';



INSERT INTO admin(accountId)
SELECT accountId
FROM accounts
WHERE name = 'admin';


-- TEMP
INSERT INTO offering(status, lessonType, available, instructorId)
SELECT 'empty', 'Group', 'True', instructorId
FROM instructor
WHERE accountId = 2;




SELECT * FROM accounts;

SELECT * FROM client;

SELECT * FROM instructor;

SELECT * FROM admin;


-- TEMP
SELECT * from offering;
SELECT * from booking;