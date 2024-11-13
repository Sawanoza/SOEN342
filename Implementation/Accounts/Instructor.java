package Accounts;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Instructor {
    
    private String url = "jdbc:sqlite:Implementation/mydb.db";

    // ====================================================================================================
    // METHOD FOR INSTRUCTOR TO REGISTER TO AN OFFERING
    // ====================================================================================================
    public void assignInstructorToOffering(Scanner scan, int accountId) {
        // Step 1: Fetch instructorId from the accountId
        int instructorId = getInstructorIdFromAccount(accountId);
        
        if (instructorId == 0) {
            System.out.println("Error: Account is not an instructor.");
            return;
        }

        // Step 2: Ask for the offeringId to assign the instructor
        System.out.print("Enter the Offering ID to register for: ");
        int offeringId = Integer.parseInt(scan.nextLine());

        // Step 3: Update the offering with the instructorId
        String updateQuery = "UPDATE offering SET instructorId = ? WHERE offeringId = ? AND instructorId = 0";
        
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setInt(1, instructorId);
            stmt.setInt(2, offeringId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Successfully assigned to Offering ID " + offeringId);
            } else {
                System.out.println("Offering not found or it has already been assigned to another instructor.");
            }

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }

    // Method to get the instructorId associated with an accountId
    private int getInstructorIdFromAccount(int accountId) {
        String query = "SELECT instructorId FROM instructor WHERE accountId = ?";
        
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("instructorId");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        
        return 0;
    }
    // ====================================================================================================



    // ====================================================================================================
    // METHOD FOR INSTRUCTOR TO SEE ASSIGNED LESSONS
    // ====================================================================================================
    public void displayInstructorLessons(int accountId) {
        // Step 1: Get the instructorId from the accountId
        int instructorId = getInstructorIdFromAccount(accountId);
        
        if (instructorId == 0) {
            System.out.println("Error: Account is not an instructor.");
            return;
        }
    
        // Step 2: Query to fetch all lessons assigned to the instructor
        String query = "SELECT o.offeringId, o.lessonType, o.lessonName, o.capacity, " +
                       "o.isAvailable, t.day, t.date, t.startTime, t.endTime, l.city " +
                       "FROM offering o " +
                       "JOIN schedule s ON o.scheduleId = s.scheduleId " +
                       "JOIN timeslot t ON s.timeslotId = t.timeslotId " +
                       "JOIN location l ON s.locationId = l.locationId " +
                       "WHERE o.instructorId = ? " +
                       "ORDER BY o.offeringId";
    
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setInt(1, instructorId);
    
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.printf("%-10s %-10s %-20s %-10s %-10s %-10s %-10s %-10s %-10s %-10s%n", 
                    "ID", "Type", "Lesson Name", "Capacity", "Available", "Day", 
                    "Date", "Start Time", "End Time", "City");
                System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
    
                while (rs.next()) {
                    String availableText = rs.getString("isAvailable");
                    String isAvailable = availableText.equalsIgnoreCase("true") ? "Yes" : "No";
                    
                    System.out.printf("%-10d %-10s %-20s %-10d %-10s %-10s %-10d %-10s %-10s %-10s%n", 
                        rs.getInt("offeringId"), 
                        rs.getString("lessonType"), 
                        rs.getString("lessonName"), 
                        rs.getInt("capacity"), 
                        isAvailable, 
                        rs.getString("day"), 
                        rs.getInt("date"), 
                        rs.getString("startTime"), 
                        rs.getString("endTime"), 
                        rs.getString("city"));
                }
            }
    
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    // ====================================================================================================

}
