DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS client;
DROP TABLE IF EXISTS instructor;
DROP TABLE IF EXISTS admin;

-------------------------------------------------------------------------
DROP TABLE IF EXISTS offering;
DROP TABLE IF EXISTS booking;

DROP TABLE IF EXISTS schedule;
DROP TABLE IF EXISTS location;
DROP TABLE IF EXISTS timeslot;
-------------------------------------------------------------------------

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

-------------------------------------------------------------------------
-- WIP
-------------------------------------------------------------------------
CREATE TABLE location (
    locationId INTEGER PRIMARY KEY AUTOINCREMENT,
    city TEXT NOT NULL
);

CREATE TABLE timeslot (
    timeslotId INTEGER PRIMARY KEY AUTOINCREMENT,
    day TEXT CHECK (day IN ('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday')) NOT NULL,
    date INTEGER CHECK (date BETWEEN 1 AND 31) NOT NULL,
    startTime TIME NOT NULL,
    endTime TIME NOT NULL
);

CREATE TABLE schedule (
    scheduleId INTEGER PRIMARY KEY AUTOINCREMENT,
    locationId INTEGER NOT NULL,
    timeslotId INTEGER NOT NULL,
    FOREIGN KEY (locationId) REFERENCES location(locationId),
    FOREIGN KEY (timeslotId) REFERENCES timeslot(timeslotId)
);

CREATE TABLE offering (
    offeringId INTEGER PRIMARY KEY AUTOINCREMENT,
    lessonType TEXT CHECK (lessonType IN ('group', 'private')) NOT NULL,
    lessonName TEXT NOT NULL,
    capacity INTEGER NOT NULL,
    isAvailable BOOLEAN NOT NULL,
    instructorId INTEGER,
    scheduleId INTEGER NOT NULL,
    FOREIGN KEY (instructorId) REFERENCES instructor(instructorId),
    FOREIGN KEY (scheduleId) REFERENCES schedule(scheduleId)
);

CREATE TABLE booking (
    bookingId INTEGER PRIMARY KEY AUTOINCREMENT,
    clientId INTEGER NOT NULL,
    offeringId INTEGER NOT NULL,
    FOREIGN KEY (clientId) REFERENCES client(clientId),
    FOREIGN KEY (offeringId) REFERENCES offering(offeringId)
);
-------------------------------------------------------------------------


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
SELECT accountId, 'Vancouver', 'Yoga'
FROM accounts
WHERE name = 'instructor';



INSERT INTO admin(accountId)
SELECT accountId
FROM accounts
WHERE name = 'admin';


-------------------------------------------------------------------------
INSERT INTO location(city)
VALUES ('Montreal'),
       ('Toronto'),
       ('Vancouver');

INSERT INTO timeslot(day, date, startTime, endTime)
VALUES ('Monday', 1, '08:00:00', '10:00:00'),
       ('Wednesday', 2, '13:00:00', '15:00:00'),
       ('Friday', 3, '14:00:00', '14:30:00');



INSERT INTO schedule (locationId, timeslotId)
SELECT l.locationId, t.timeslotId
FROM location l, timeslot t
WHERE l.locationId = 1 AND t.timeslotId = 1;

INSERT INTO schedule (locationId, timeslotId)
SELECT l.locationId, t.timeslotId
FROM location l, timeslot t
WHERE l.locationId = 2 AND t.timeslotId = 2;

INSERT INTO schedule (locationId, timeslotId)
SELECT l.locationId, t.timeslotId
FROM location l, timeslot t
WHERE l.locationId = 3 AND t.timeslotId = 3;




INSERT INTO offering(lessonType, lessonName, capacity, isAvailable, instructorId, scheduleId)
SELECT 'group', 'Yoga', 2, 'true', i.instructorId, s.scheduleId
FROM instructor i, schedule s
WHERE i.accountId = 2 AND s.scheduleId = 1;

INSERT INTO offering(lessonType, lessonName, capacity, isAvailable, instructorId, scheduleId)
SELECT 'group', 'Volleyball', 5, 'true', 0, s.scheduleId
FROM schedule s
WHERE s.scheduleId = 2;

INSERT INTO offering(lessonType, lessonName, capacity, isAvailable, instructorId, scheduleId)
SELECT 'private', 'Swimming', 1, 'true', 0, s.scheduleId
FROM schedule s
WHERE s.scheduleId = 3;



INSERT INTO booking(clientId, offeringId)
SELECT c.clientId, o.offeringId
FROM client c, offering o
WHERE c.clientId = 1 AND o.offeringId = 1;
-------------------------------------------------------------------------



SELECT * FROM accounts;

SELECT * FROM client;

SELECT * FROM instructor;

SELECT * FROM admin;


-------------------------------------------------------------------------
SELECT * from offering;
SELECT * from schedule;
SELECT * from location;
SELECT * from timeslot;
SELECT * from booking;
-------------------------------------------------------------------------