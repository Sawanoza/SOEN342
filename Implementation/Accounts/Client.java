package Accounts;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Client extends Account {

    private Account account;

    public Client() {
        this.account = new Account();
    }
    
    private String url = "jdbc:sqlite:Implementation/mydb.db";
    
    // ====================================================================================================
    // METHOD TO HANDLE GUARDIANED ACCOUNT CREATION
    // ====================================================================================================
    public boolean createUnderageAccount(Scanner scan) {
        // Take inputs from the user
        System.out.print("Enter your email: ");
        int guardianID = account.getAccountIdByEmail(scan.nextLine());
        
        System.out.print("Enter ward's name: ");
        String name = scan.nextLine();
    
        System.out.print("Enter ward's age: ");
        int age = scan.nextInt();
        scan.nextLine();
    
        System.out.print("Enter ward's email: ");
        String email = scan.nextLine();
    
        System.out.print("Enter ward's phone number: ");
        String phoneNumber = scan.nextLine();
    
        System.out.print("Enter ward's password: ");
        String password = scan.nextLine();
    
        // Insert into the accounts table
        int accountId = account.insertIntoAccounts(name, age, email, phoneNumber, password);
        if (accountId == -1) {
            System.out.println("Error: Failed to create account in the database.");
            return false;
        }
    
        return account.insertClient(accountId, guardianID);
    }
    // ====================================================================================================



    // ====================================================================================================
    // METHOD TO RETRIEVE ALL LESSONS (FOR CLIENTS)
    // ====================================================================================================
    public void displayClientLessons(int accountId) {
        // Step 1: Get the clientId from the accountId
        int clientId = getClientIdFromAccount(accountId);
        
        if (clientId == 0) {
            System.out.println("Error: Account is not a client.");
            return;
        }
    
        // Step 2: Query to fetch all lessons the client is registered for, including bookingId
        String query = "SELECT b.bookingId, o.lessonType, o.lessonName, o.capacity, t.day, t.date, " +
                       "t.startTime, t.endTime, l.city " +
                       "FROM booking b " +
                       "JOIN offering o ON b.offeringId = o.offeringId " +
                       "JOIN schedule s ON o.scheduleId = s.scheduleId " +
                       "JOIN timeslot t ON s.timeslotId = t.timeslotId " +
                       "JOIN location l ON s.locationId = l.locationId " +
                       "WHERE b.clientId = ? " +
                       "ORDER BY b.bookingId";
    
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setInt(1, clientId); // Set the clientId in the query
    
            try (ResultSet rs = stmt.executeQuery()) {
                // Print header
                System.out.printf("%-10s %-10s %-20s %-10s %-10s %-10s %-10s %-10s %-10s%n", 
                    "Booking ID", "Type", "Lesson Name", "Capacity", "Day", 
                    "Date", "Start Time", "End Time", "City");
                System.out.println("----------------------------------------------------------------------------------------------------------");
    
                // Print the lessons the client is registered for
                while (rs.next()) {
                    System.out.printf("%-10d %-10s %-20s %-10d %-10s %-10d %-10s %-10s %-10s%n", 
                        rs.getInt("bookingId"), 
                        rs.getString("lessonType"), 
                        rs.getString("lessonName"), 
                        rs.getInt("capacity"), 
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


    // Method to get the instructorId associated with an accountId
    private int getClientIdFromAccount(int accountId) {
        String query = "SELECT clientId FROM client WHERE accountId = ?";
        
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("clientId");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        
        return 0;
    }
    // ====================================================================================================



    // ====================================================================================================
    // METHOD TO REGISTER TO A LESSON
    // ====================================================================================================
    public boolean registerForLesson(Scanner scan, int accountId) {
        // Step 1: Get the clientId from the accountId
    int clientId = getClientIdFromAccount(accountId);
    
    if (clientId == 0) {
        System.out.println("Error: Account is not a client.");
        return false;
    }

    // Step 2: Ask for the offeringId from the user
    System.out.print("Enter the Offering ID to register for: ");
    int offeringId = Integer.parseInt(scan.nextLine());

    // Step 3: Register the client for the lesson by inserting into the booking table
    String registerQuery = "INSERT INTO booking (clientId, offeringId) VALUES (?, ?)";
    
    try (Connection conn = DriverManager.getConnection(url);
         PreparedStatement insertStmt = conn.prepareStatement(registerQuery)) {
        
        insertStmt.setInt(1, clientId);
        insertStmt.setInt(2, offeringId);
        int rowsAffected = insertStmt.executeUpdate();

        if (rowsAffected > 0) {
            // Step 4: Decrease the capacity by 1
            String updateCapacityQuery = "UPDATE offering SET capacity = capacity - 1 WHERE offeringId = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateCapacityQuery)) {
                updateStmt.setInt(1, offeringId);
                updateStmt.executeUpdate();
            }

            // Step 5: Check if the capacity reached 0 and set isAvailable to false
            String checkCapacityQuery = "SELECT capacity FROM offering WHERE offeringId = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkCapacityQuery)) {
                checkStmt.setInt(1, offeringId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt("capacity") == 0) {
                        String updateAvailabilityQuery = "UPDATE offering SET isAvailable = 'false' WHERE offeringId = ?";
                        try (PreparedStatement availabilityStmt = conn.prepareStatement(updateAvailabilityQuery)) {
                            availabilityStmt.setInt(1, offeringId);
                            availabilityStmt.executeUpdate();
                        }
                    }
                }
            }

            System.out.println("Successfully registered for the lesson.");
            return true;
        } else {
            System.out.println("Error: Registration failed.");
            return false;
        }
    } catch (SQLException e) {
        System.out.println("SQL Error: " + e.getMessage());
        return false;
    }
}





}
