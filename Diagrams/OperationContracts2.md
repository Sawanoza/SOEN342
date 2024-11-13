### **Contract CO5: Make Booking for Client**

**Operation**: `makeBooking(clientID: String, offeringID: String, bookingDetails: Object)`

**Cross References**: Use Case: Client Booking Offering

**Preconditions**:
1. The client must be authenticated and authorized to perform this action.
2. The specified `offeringID` corresponds to an existing, valid Offering instance.
3. The `offeringID` must correspond to an offering whose status is "Assigned" (i.e., it has an assigned instructor and is ready for booking).

**Postconditions**:
1. A new Booking instance is created for the client:
   - The booking is associated with the specified `offeringID` and `clientID`.
   - The booking details (e.g., payment, participant count) are stored.
2. The offering’s status is updated to "Booked".
3. The client’s booking history is updated to include the new booking.
4. The location's schedule is updated to reflect that the offering is now fully booked, and no additional bookings can be made for this offering unless capacity allows (for group bookings, this will depend on available space).

---

### **Contract CO6: View Bookings for Client**

**Operation**: `viewBookings(clientID: String)`

**Cross References**: Use Case: Client Viewing Bookings

**Preconditions**:
1. The client must be authenticated and authorized to perform this action.
2. The `clientID` must correspond to a valid, registered client.

**Postconditions**:
1. A list of Booking instances is returned, each associated with the client:
   - Each booking corresponds to an offering that the client has booked.
   - For each booking, the details of the associated offering (e.g., location, timeslot, instructor) are included.
2. The list of bookings is sorted by timeslot or booking date, as appropriate.
3. The status of each booking\is included in the returned data.
