# Operation Contract #1: `addOffering(scan:Scanner)`

## Inputs

1. **Scanner scan**: A `Scanner` object used to capture user input from the console.
   - This object allows the user to input data such as:
     - Lesson type (group/private)
     - Lesson name
     - Capacity (number of participants)
     - Day of the lesson
     - Date of the lesson (1-31)
     - Start and end times of the lesson
     - City where the lesson will take place

## Pre-Conditions
- The method assumes the connection to the database is already established.
- The `Scanner` object is correctly passed to the method, providing input from the user.
- The lesson type can either be "group" or "private". If the lesson is private, capacity will be set to 1 automatically.
- The city must either exist in the database or be added.

## Post-Conditions
- The lesson offering will be successfully added to the system, provided there are no conflicts.
- If the city does not already exist, it will be added to the system.
- A timeslot will be created for the specified lesson date and time.
- A schedule will be created to link the timeslot and location to the lesson offering.

# Operation Contract #2: `assignInstructorToOffering(offeringID:Integer, accountId:Integer)`

## Inputs

1. **Scanner scan**: A `Scanner` object used to capture user input from the console.
   - This object is used to input the `offeringId` for the lesson that the instructor will be assigned to.

2. **int accountId**: The ID of the account (instructor) attempting to be assigned as an instructor. This is used to retrieve the `instructorId` associated with the account.
ss
## Pre-Conditions
- The `accountId` provided must correspond to a valid account in the system.
- The account must be an instructor; otherwise, an error message is shown, and the assignment does not proceed.
- The offering specified by the `offeringId` must exist in the system.
- The `offeringId` must not already have an instructor assigned to it (i.e., `instructorId = 0`).

## Post-Conditions
- The `offering` record in the database will have the `instructorId` updated with the instructor's ID if the assignment was successful.
- If the assignment failed (due to the offering already having an instructor or being invalid), no changes will be made to the `offering` table.


# Operation Contract #3: `getLessons()`

## Pre-Conditions
- The method assumes a valid database connection has been established (using the `DriverManager.getConnection(url)`).
- The system contains at least one lesson with an assigned instructor in the database.
- The database schema must include tables for `offering`, `schedule`, `timeslot`, and `location`, with proper relationships between them.
- The `offering` table must have a non-zero `instructorId` to indicate the lesson has an assigned instructor.
- The `schedule`, `timeslot`, and `location` tables must contain valid records to retrieve lesson details.

## Post-Conditions
- The method will display a list of all lessons that have an instructor assigned, showing the following details in tabular format:
  - **Offering ID**
  - **Lesson Type** (group/private)
  - **Lesson Name**
  - **Capacity**
  - **Availability** (Yes/No)
  - **Day**
  - **Date**
  - **Start Time**
  - **End Time**
  - **City**
- If there are no lessons with an instructor, no output is displayed.

# Operation Contract #4: `registerForOffering(offeringID:Integer, accountId:Integer)`

## Inputs

1. **Scanner scan**: A `Scanner` object used to capture user input from the console.
   - This object is used to input the `offeringId` that the client wishes to register for.

2. **int accountId**: The ID of the account (user) attempting to register for the offering. This is used to retrieve the `clientId` associated with the account.

## Pre-Conditions
- The `accountId` provided must correspond to a valid account in the system.
- The account must be a valid client; otherwise, an error message is displayed, and the registration process is aborted.
- The `offeringId` provided must exist in the database and be available for registration (i.e., it must have a non-zero capacity).
- The offering must not already be fully booked (capacity > 0).

## Post-Conditions
- If the registration is successful:
  - A new record is inserted into the `booking` table linking the client to the offering.
  - The `capacity` for the offering is decreased by 1.
  - If the `capacity` reaches zero, the `isAvailable` field for the offering is set to `false`.
- If the registration fails:
  - No changes are made to the `booking` or `offering` tables.

# Operation Contract #5: `viewBookings(accountID:Integer)`

## Inputs

1. **int accountId**: The ID of the account (user) requesting to view their bookings. This is used to retrieve the `clientId` associated with the account.

## Pre-Conditions
- The `accountId` provided must correspond to a valid account in the system.
- The account must be a valid client, as checked by the `getClientIdFromAccount` method.
- The client must have at least one booking in the system.

## Post-Conditions
- No changes are made to the database or system state.
- If successful, the bookings for the client are displayed in the console in a tabular format.
