package Bookings;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.SQLException;

public class Booking {

    private String url = "jdbc:sqlite:Implementation/mydb.db";

    // ====================================================================================================
    // METHOD TO RETRIEVE ALL BOOKINGS
    // ====================================================================================================
    public void getBookings() {
        String query = "SELECT b.bookingId, c.clientId, a.name AS clientName, " +
                       "o.lessonName, t.day, t.date, t.startTime, t.endTime, l.city " +
                       "FROM booking b " +
                       "JOIN client c ON b.clientId = c.clientId " + 
                       "JOIN accounts a ON c.accountId = a.accountId " +
                       "JOIN offering o ON b.offeringId = o.offeringId " +
                       "JOIN schedule s ON o.scheduleId = s.scheduleId " +
                       "JOIN timeslot t ON s.timeslotId = t.timeslotId " +
                       "JOIN location l ON s.locationId = l.locationId " +
                       "ORDER BY b.bookingId";
        
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            // Printing the header
            System.out.printf("%-10s %-15s %-20s %-20s %-10s %-10s %-10s %-10s %-10s%n", 
                "Booking ID", "Client ID", "Client Name", "Lesson Name", 
                "Day", "Date", "Start Time", "End Time", "City");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------");
            
            // Fetch and display each booking
            while (rs.next()) {
                System.out.printf("%-10d %-15d %-20s %-20s %-10s %-10d %-10s %-10s %-10s%n", 
                    rs.getInt("bookingId"), 
                    rs.getInt("clientId"), 
                    rs.getString("clientName"), 
                    rs.getString("lessonName"), 
                    rs.getString("day"), 
                    rs.getInt("date"), 
                    rs.getString("startTime"), 
                    rs.getString("endTime"), 
                    rs.getString("city"));
            }
            
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    // ====================================================================================================



    // ====================================================================================================
    // METHOD TO CANCEL A BOOKING
    // ====================================================================================================
    public boolean cancelBooking(Scanner scan) {
        System.out.print("Enter booking ID to cancel: ");
        int bookingId = Integer.parseInt(scan.nextLine());
    
        // Query to get the offeringId associated with the booking
        String selectQuery = "SELECT offeringId FROM booking WHERE bookingId = ?";
        String deleteQuery = "DELETE FROM booking WHERE bookingId = ?";
        String updateCapacityQuery = "UPDATE offering SET capacity = capacity + 1 WHERE offeringId = ?";
    
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
             PreparedStatement updateCapacityStmt = conn.prepareStatement(updateCapacityQuery)) {
    
            // Step 1: Retrieve the offeringId associated with the booking
            selectStmt.setInt(1, bookingId);
            ResultSet rs = selectStmt.executeQuery();
    
            if (rs.next()) {
                int offeringId = rs.getInt("offeringId");
    
                // Step 2: Delete the booking
                deleteStmt.setInt(1, bookingId);
                int rowsAffected = deleteStmt.executeUpdate();
    
                if (rowsAffected > 0) {
                    // Step 3: Increment the capacity of the offering by 1
                    updateCapacityStmt.setInt(1, offeringId);
                    updateCapacityStmt.executeUpdate();
    
                    System.out.println("Booking canceled successfully, and capacity updated.");
                    return true;
                } else {
                    System.out.println("No booking found with the provided bookingId.");
                    return false;
                }
            } else {
                System.out.println("No booking found with the provided bookingId.");
                return false;
            }
    
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            return false;
        }
    }
    // ====================================================================================================
}