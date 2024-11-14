package LessonsAndOfferings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.SQLException;

public class Offering {
    
    private String url = "jdbc:sqlite:Implementation/mydb.db";

    // ====================================================================================================
    // METHOD TO RETRIEVE ALL OFFERINGS
    // ====================================================================================================
    public void getOfferings() {
        String query = "SELECT o.offeringId, o.lessonType, o.lessonName, o.capacity, o.isAvailable, " +
                       "o.instructorId, t.day, t.date, t.startTime, t.endTime, l.city " +
                       "FROM offering o " +
                       "JOIN schedule s ON o.scheduleId = s.scheduleId " +
                       "JOIN timeslot t ON s.timeslotId = t.timeslotId " +
                       "JOIN location l ON s.locationId = l.locationId " +
                       "ORDER BY o.offeringId";
        
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
                System.out.printf("%-10s %-10s %-20s %-10s %-10s %-15s %-10s %-10s %-10s %-10s %-20s%n", 
                "ID", "Type", "Lesson Name", "Capacity", "Available", "Instructor ID", 
                "Day", "Date", "Start Time", "End Time", "City");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------");
            
            while (rs.next()) {
                String availableText = rs.getString("isAvailable");
                String isAvailable = availableText.equalsIgnoreCase("true") ? "Yes" : "No";
                
                System.out.printf("%-10d %-10s %-20s %-10d %-10s %-15d %-10s %-10d %-10s %-10s %-20s%n", 
                  rs.getInt("offeringId"), 
                  rs.getString("lessonType"), 
                  rs.getString("lessonName"), 
                  rs.getInt("capacity"), 
                  isAvailable, 
                  rs.getInt("instructorId"),
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
    // METHOD TO ADD AN OFFERING
    // ====================================================================================================
    public void addOffering(Scanner scan) {
        int capacity;
    
        System.out.print("Enter lesson type (group/private): ");
        String lessonType = scan.nextLine();
    
        System.out.print("Enter lesson name (e.g., Yoga, Swimming, Tennis, etc.): ");
        String lessonName = scan.nextLine();
    
        if (lessonType.equals("private")) {
            capacity = 1;
        } else {
            System.out.print("Enter capacity (number of participants): ");
            capacity = Integer.parseInt(scan.nextLine());
        }
    
        System.out.print("Enter the day of the lesson (e.g., Monday, Tuesday, etc.): ");
        String day = scan.nextLine();
    
        System.out.print("Enter the date of the lesson (1-31): ");
        int date = Integer.parseInt(scan.nextLine()); 
    
        System.out.print("Enter the start time of the lesson (e.g., 09:00:00): ");
        String startTime = scan.nextLine();
    
        System.out.print("Enter the end time of the lesson (e.g., 10:00:00): ");
        String endTime = scan.nextLine();
    
        System.out.print("Enter the city where the lesson will take place: ");
        String city = scan.nextLine();
    
        String checkQuery = "SELECT COUNT(*) AS count " +
                            "FROM offering o " +
                            "JOIN schedule s ON o.scheduleId = s.scheduleId " +
                            "JOIN timeslot t ON s.timeslotId = t.timeslotId " +
                            "JOIN location l ON s.locationId = l.locationId " +
                            "WHERE l.city = ? AND t.day = ? AND t.date = ? " +
                            "AND ((t.startTime <= ? AND t.endTime > ?) OR (t.startTime < ? AND t.endTime >= ?))";
    
        String getLocationQuery = "SELECT locationId FROM location WHERE city = ?";
        String insertLocationQuery = "INSERT INTO location(city) VALUES (?)";
    
        String insertTimeSlotQuery = "INSERT INTO timeslot(day, date, startTime, endTime) VALUES (?, ?, ?, ?)";
        String insertScheduleQuery = "INSERT INTO schedule(locationId, timeslotId) VALUES (?, ?)";
    
        String insertOfferingQuery = "INSERT INTO offering(lessonType, lessonName, capacity, isAvailable, instructorId, scheduleId) " +
                                     "VALUES (?, ?, ?, 'true', 0, ?)";
    
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement getLocationStmt = conn.prepareStatement(getLocationQuery);
             PreparedStatement insertLocationStmt = conn.prepareStatement(insertLocationQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement insertTimeSlotStmt = conn.prepareStatement(insertTimeSlotQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement insertScheduleStmt = conn.prepareStatement(insertScheduleQuery, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement insertOfferingStmt = conn.prepareStatement(insertOfferingQuery)) {
    
            // Check for time conflicts
            checkStmt.setString(1, city);
            checkStmt.setString(2, day);
            checkStmt.setInt(3, date);
            checkStmt.setString(4, startTime);
            checkStmt.setString(5, startTime);
            checkStmt.setString(6, endTime);
            checkStmt.setString(7, endTime);
    
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt("count") > 0) {
                System.out.println("\nTime conflict detected. Cannot add offering.");
                return;
            }
    
            // Check if location already exists
            int locationId;
            getLocationStmt.setString(1, city);
            ResultSet locationRs = getLocationStmt.executeQuery();
    
            if (locationRs.next()) {
                locationId = locationRs.getInt("locationId"); // Use existing locationId
            } else {
                // Insert city into location if it does not exist and retrieve the new locationId
                insertLocationStmt.setString(1, city);
                insertLocationStmt.executeUpdate();
                try (ResultSet locationKeys = insertLocationStmt.getGeneratedKeys()) {
                    locationKeys.next();
                    locationId = locationKeys.getInt(1);
                }
            }
    
            // Insert into timeslot and get generated timeslotId
            insertTimeSlotStmt.setString(1, day);
            insertTimeSlotStmt.setInt(2, date);
            insertTimeSlotStmt.setString(3, startTime);
            insertTimeSlotStmt.setString(4, endTime);
            insertTimeSlotStmt.executeUpdate();
    
            int timeslotId;
            try (ResultSet timeSlotKeys = insertTimeSlotStmt.getGeneratedKeys()) {
                timeSlotKeys.next();
                timeslotId = timeSlotKeys.getInt(1);
            }
    
            // Insert into schedule with locationId and timeslotId, then get scheduleId
            insertScheduleStmt.setInt(1, locationId);
            insertScheduleStmt.setInt(2, timeslotId);
            insertScheduleStmt.executeUpdate();
    
            int scheduleId;
            try (ResultSet scheduleKeys = insertScheduleStmt.getGeneratedKeys()) {
                scheduleKeys.next();
                scheduleId = scheduleKeys.getInt(1);
            }
    
            // Insert the new offering
            insertOfferingStmt.setString(1, lessonType);
            insertOfferingStmt.setString(2, lessonName);
            insertOfferingStmt.setInt(3, capacity);
            insertOfferingStmt.setInt(4, scheduleId);
            insertOfferingStmt.executeUpdate();
    
            System.out.println("Offering added successfully.");
    
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    // ====================================================================================================



    // ====================================================================================================
    // METHOD TO GET ALL LESSONS
    // ====================================================================================================
    public void getLessons() {
        String query = "SELECT o.offeringId, o.lessonType, o.lessonName, o.capacity, o.isAvailable, " +
                       "t.day, t.date, t.startTime, t.endTime, l.city " +
                       "FROM offering o " +
                       "JOIN schedule s ON o.scheduleId = s.scheduleId " +
                       "JOIN timeslot t ON s.timeslotId = t.timeslotId " +
                       "JOIN location l ON s.locationId = l.locationId " +
                       "WHERE o.instructorId != 0 " +  // Filter to only get lessons with an instructor
                       "ORDER BY o.offeringId";
        
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            System.out.printf("%-10s %-10s %-20s %-10s %-10s %-10s %-10s %-10s %-10s %-20s%n", 
                "ID", "Type", "Lesson Name", "Capacity", "Available", 
                "Day", "Date", "Start Time", "End Time", "City");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------");
            
            while (rs.next()) {
                String availableText = rs.getString("isAvailable");
                String isAvailable = availableText.equalsIgnoreCase("true") ? "Yes" : "No";
                
                System.out.printf("%-10d %-10s %-20s %-10d %-10s %-10s %-10d %-10s %-10s %-20s%n", 
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
            
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    // ====================================================================================================



    // ====================================================================================================
    // METHOD TO RETRIEVE ALL OFFERINGS (FOR INSTRUCTORS)
    // ====================================================================================================
    public void getInstructorOfferings() {
        String query = "SELECT o.offeringId, o.lessonType, o.lessonName, o.capacity, " +
                       "t.day, t.date, t.startTime, t.endTime, l.city " +
                       "FROM offering o " +
                       "JOIN schedule s ON o.scheduleId = s.scheduleId " +
                       "JOIN timeslot t ON s.timeslotId = t.timeslotId " +
                       "JOIN location l ON s.locationId = l.locationId " +
                       "WHERE o.instructorId = 0 " +  // Filter for offerings where instructorId = 0
                       "ORDER BY o.offeringId";
    
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            // Print table header (excluding instructorId and available)
            System.out.printf("%-10s %-10s %-20s %-10s %-10s %-10s %-10s %-10s %-20s%n", 
                              "ID", "Type", "Lesson Name", "Capacity", 
                              "Day", "Date", "Start Time", "End Time", "City");
            System.out.println("------------------------------------------------------------------------------------------------------------");
    
            // Iterate through result set and display offerings
            while (rs.next()) {
                System.out.printf("%-10d %-10s %-20s %-10d %-10s %-10d %-10s %-10s %-20s%n", 
                                  rs.getInt("offeringId"), 
                                  rs.getString("lessonType"), 
                                  rs.getString("lessonName"), 
                                  rs.getInt("capacity"),
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
    // METHOD TO RETRIEVE ALL LESSONS (FOR CLIENTS)
    // ====================================================================================================
    public void getAvailableLessonsForClient(int accountId) {
        // First, retrieve the clientId associated with the given accountId
        String getClientIdQuery = "SELECT clientId FROM client WHERE accountId = ?";
        int clientId = -1;
    
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(getClientIdQuery)) {
    
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
    
            if (rs.next()) {
                clientId = rs.getInt("clientId");
            } else {
                System.out.println("Client not found.");
                return;
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            return;
        }
    
        // Query to get lessons that are not fully booked, not already registered by the client, and have an assigned instructor
        String query = "SELECT o.offeringId, o.lessonType, o.lessonName, o.capacity, " +
                       "t.day, t.date, t.startTime, t.endTime, l.city " +
                       "FROM offering o " +
                       "JOIN schedule s ON o.scheduleId = s.scheduleId " +
                       "JOIN timeslot t ON s.timeslotId = t.timeslotId " +
                       "JOIN location l ON s.locationId = l.locationId " +
                       "LEFT JOIN booking b ON o.offeringId = b.offeringId AND b.clientId = ? " +
                       "WHERE o.capacity > 0 AND o.instructorId != 0 AND b.bookingId IS NULL " + // Exclude lessons already booked or without instructor
                       "ORDER BY o.offeringId";
    
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
    
            System.out.printf("%-10s %-10s %-20s %-10s %-10s %-10s %-10s %-10s%n", 
                              "ID", "Type", "Lesson Name", "Capacity", 
                              "Day", "Date", "Start Time", "End Time", "City");
            System.out.println("-------------------------------------------------------------------------------------------------------");
    
            while (rs.next()) {
                System.out.printf("%-10d %-10s %-20s %-10d %-10s %-10d %-10s %-10s %-20s%n", 
                                  rs.getInt("offeringId"), 
                                  rs.getString("lessonType"), 
                                  rs.getString("lessonName"), 
                                  rs.getInt("capacity"), 
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






}
