**Contract CO1: createOffering for Administrator**

**Operation**: `create_offerings(location,timeslot,mode,specialization)`

**Cross References**: Use Case: Process Offerings

**Preconditions**:
1. The administrator is authenticated and authorized to perform this action.
2. The `offeringDetails` contains valid parameters:
3. `location`: an existing, valid `Location` instance with adequate capacity.
4.  `timeslot`: a valid `Timeslot` instance that does not conflict with the `Location` instance’s current schedule.
5.  `lessonType`: a recognized type in the system.
6.  `mode`: either "private" or "group".

**Postconditions**:
1. A new `Offering` instance (`o`) is added to the system with details from `offeringDetails`.
2. The new offering (`o`) is uniquely assigned without conflicts:
3. No other offering exists with the same `location` and `timeslot`.
4. The offering is associated with the provided `location` and `timeslot`.
5. The `timeslot` is updated within the `Location` instance’s schedule to reflect the new offering.
6. The offering status is initialized as "Unassigned."

**Contract CO2: Get Available Offerings for Instructor**

**Operation**: `getAllOfferings(available_cities_specialization)`

**Cross References**: Use Case: Instructor Viewing Available Offerings

**Preconditions**:
1. The Instructor is logged in with registered specialization(s) and availability.
2. `cities` is a non-empty list of valid city names where the Instructor is available.
3. `specializations` is a non-empty list of valid specialization types matching the Instructor’s qualifications.

**Postconditions**:
1. Returns a list of `Offering` instances that meet the following criteria:
2. The offering’s `lessonType` aligns with one of the provided `specializations`.
3. The offering’s `location.city` is within the specified `cities`.
4. The offering’s `status` is "Unassigned" (i.e., it is available and not yet assigned to any instructor).


**Contract CO3: Assign Instructor to Offering**

**Operation**: `assignInstructorToOffering(instructorID: String, offeringID: String)`

**Cross References**: Use Case: Process Offerings, Assigning Instructor to Teach Offering

**Preconditions**:
1. The specified `offeringID` corresponds to an existing, valid `Offering` instance in the system.
2. The specified `instructorID` corresponds to an existing, valid `Instructor` instance in the system.
3. The `Offering` meets the following conditions:
   - `status` is "Unassigned."
   - The `lessonType` matches the Instructor’s specialization.
   - The `location.city` is within the Instructor’s availability cities.
4. The Instructor is not already assigned to another offering with a conflicting date and time.

**Postconditions**:
1. The `Offering` is assigned to the specified `Instructor`:
   - `offering.instructor_id` is set to the Instructor’s ID.
   - The offering’s `status` is updated to "Assigned."
2. Associations are updated:
   - The Instructor’s `assignedOfferings` list includes the new `Offering`.
   - The Instructor’s schedule is updated to include the offering’s timeslot.


**Contract CO4: Get Public Offerings**

**Operation**: `getAllOffering_with_Instructors()`

**Cross References**: Use Case: View All Lessons

**Preconditions**:
- None since anyone can perform this operation

**Postconditions**:
1. Returns a list of `Offering` instances that meet the following criteria:
   - The offering’s `status` is "Assigned."

