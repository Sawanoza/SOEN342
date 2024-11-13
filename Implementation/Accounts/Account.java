package Accounts;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.SQLException;

public class Account {
    // SQLite database file path
    private String url = "jdbc:sqlite:Implementation/mydb.db";
        


    // ====================================================================================================
    // METHOD TO RETRIEVE ALL ACCOUNTS
    // ====================================================================================================
    public void getAccounts() {
        String query = "SELECT * FROM accounts ORDER BY accountId";
    
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
    
            System.out.printf("%-10s %-30s %-10s %-30s %-15s %-20s%n", "ID", "Name", "Age", "Email", "Phone Number", "Password");
            System.out.println("--------------------------------------------------------------------------------------------------------------");
    
            while (rs.next()) {
                System.out.printf("%-10d %-30s %-10s %-30s %-15s %-20s%n", 
                                  rs.getInt("accountId"), 
                                  rs.getString("name"), 
                                  rs.getString("age"), 
                                  rs.getString("email"), 
                                  rs.getString("phoneNumber"), 
                                  rs.getString("password"));
            }
    
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    // ====================================================================================================



    // ====================================================================================================
    // METHOD TO RETRIEVE CLIENT ACCOUNTS
    // ====================================================================================================
    public void getClients() {
        String query = "SELECT * FROM client ORDER BY clientId";
    
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
    
            System.out.printf("%-15s %-20s %-25s%n", "Client ID", "Account ID", "Guardian (Account ID)");
            System.out.println("---------------------------------------------------------------");
    
            while (rs.next()) {
                System.out.printf("%-15d %-20s %-25s%n", 
                                  rs.getInt("clientId"), 
                                  rs.getString("accountId"), 
                                  rs.getString("guardianAccountId"));
            }
    
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    // ====================================================================================================



    // ====================================================================================================
    // METHOD TO RETRIEVE INSTRUCTOR ACCOUNTS
    // ====================================================================================================
    public void getInstructors() {
        String query = "SELECT * FROM instructor ORDER BY instructorId";
    
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
    
            System.out.printf("%-15s %-20s %-20s %-20s%n", "Instructor ID", "Account ID", "Availability", "Specialty");
            System.out.println("--------------------------------------------------------------------");
    
            while (rs.next()) {
                System.out.printf("%-15d %-20s %-20s %-20s%n", 
                                  rs.getInt("instructorId"), 
                                  rs.getString("accountId"), 
                                  rs.getString("availability"), 
                                  rs.getString("speciality"));
            }
    
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    // ====================================================================================================



    // ====================================================================================================
    // METHOD TO HANDLE AUTHENTICATION
    // ====================================================================================================

    public String authenticateUser(String email, String password) {
        String query = "SELECT accountId FROM accounts WHERE email = ? AND password = ?";
    
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setString(1, email);
            stmt.setString(2, password);
    
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int accountId = rs.getInt("accountId");
    
                    // Check the associated table for the accountId
                    String tableName = checkAccountType(conn, accountId);
    
                    if (tableName != null) {
                        return tableName;  // Return the table name (role)
                    } else {
                        return "Unknown";
                    }
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            return null;
        }
    }
    
    // Helper method to check the associated table (client, instructor, or admin)
    private String checkAccountType(Connection conn, int accountId) throws SQLException {
        // Check if the accountId exists in the client table
        String clientQuery = "SELECT 1 FROM client WHERE accountId = ?";
        try (PreparedStatement clientStmt = conn.prepareStatement(clientQuery)) {
            clientStmt.setInt(1, accountId);
            try (ResultSet rs = clientStmt.executeQuery()) {
                if (rs.next()) {
                    return "client";
                }
            }
        }
    
        // Check if the accountId exists in the instructor table
        String instructorQuery = "SELECT 1 FROM instructor WHERE accountId = ?";
        try (PreparedStatement instructorStmt = conn.prepareStatement(instructorQuery)) {
            instructorStmt.setInt(1, accountId);
            try (ResultSet rs = instructorStmt.executeQuery()) {
                if (rs.next()) {
                    return "instructor";
                }
            }
        }
    
        // Check if the accountId exists in the admin table
        String adminQuery = "SELECT 1 FROM admin WHERE accountId = ?";
        try (PreparedStatement adminStmt = conn.prepareStatement(adminQuery)) {
            adminStmt.setInt(1, accountId);
            try (ResultSet rs = adminStmt.executeQuery()) {
                if (rs.next()) {
                    return "admin";
                }
            }
        }
    
        // If not found in any table, return null
        return null;
    }
    // ====================================================================================================



    // ====================================================================================================
    // METHOD TO HANDLE ACCOUNT CREATION
    // ====================================================================================================
    public boolean createAccount(Scanner scan) {
        System.out.print("Enter your name: ");
        String name = scan.nextLine();
    
        System.out.print("Enter your age: ");
        int age = scan.nextInt();
        scan.nextLine();
    
        System.out.print("Enter your email: ");
        String email = scan.nextLine();
    
        System.out.print("Enter your phone number: ");
        String phoneNumber = scan.nextLine();
    
        System.out.print("Enter your password: ");
        String password = scan.nextLine();
    
        System.out.print("Enter your role (client, instructor): ");
        String role = scan.nextLine();
    
        // Check if the age is valid
        if (age < 18) {
            System.out.println("\nError: Age must be 18 or older to create an account.");
            return false;
        }
    
        // Insert into the accounts table
        int accountId = insertIntoAccounts(name, age, email, phoneNumber, password);
        if (accountId == -1) {
            System.out.println("Error: Failed to create account in the database.");
            return false;
        }
    
        // Insert into the respective table (client or instructor)
        if (role.equalsIgnoreCase("user") || role.equalsIgnoreCase("client")) {
            return insertClient(accountId, 0);
        } else if (role.equalsIgnoreCase("instructor")) {
            System.out.print("Enter your availability (ex: Montreal, Toronto, Vancouver): ");
            String availability = scan.nextLine();

            System.out.print("Enter your speciality (ex: Yoga, Swimming, Tennis): ");
            String speciality = scan.nextLine();
            return insertInstructor(accountId, availability, speciality);
        } else {
            System.out.println("Error: Invalid role entered.");
            return false;
        }
    }
    // ====================================================================================================



    // ====================================================================================================
    // METHOD TO HANDLE INSERTION INTO CORRECT ACCOUNT TYPE
    // ====================================================================================================

    // Insert into the accounts table and return the generated accountId
    public int insertIntoAccounts(String name, int age, String email, String phoneNumber, String password) {
        String query = "INSERT INTO accounts (name, age, email, phoneNumber, password) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // Set the parameters for the query
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, email);
            stmt.setString(4, phoneNumber);
            stmt.setString(5, password);

            // Execute the query to insert the new user
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                // Retrieve the generated accountId
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);  // Return the generated accountId
                    }
                }
            }
            return -1; // Return -1 if insertion failed
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;  // Error occurred during insert
        }
    }

    // Insert into the client table
    public boolean insertClient(int accountId, int guardianAccountId) {
        String query = "INSERT INTO client (accountId, guardianAccountId) VALUES (?,?)";
        
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, accountId); 
            stmt.setInt(2, guardianAccountId);

            // Execute the query to insert the client
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Return true if insert was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Insert into the instructor table
    public boolean insertInstructor(int accountId, String availability, String speciality) {
        String query = "INSERT INTO instructor (accountId, availability, speciality) VALUES (?,?,?)";
        
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, accountId); 
            stmt.setString(2, availability);
            stmt.setString(3, speciality);

            // Execute the query to insert the instructor
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;  // Return true if insert was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ====================================================================================================



    // ====================================================================================================
    // METHOD TO DELETE ACCOUNT
    // ====================================================================================================
    public boolean deleteAccount(int accountId) {
        // dont let admin delete itself
        if (accountId == 1) {
            System.out.println("ERROR: Cannot delete Admin Account!");
            return false;
        }
        
        String deleteClientQuery = "DELETE FROM client WHERE accountId = ?";
        String deleteInstructorQuery = "DELETE FROM instructor WHERE accountId = ?";
        String deleteAccountQuery = "DELETE FROM accounts WHERE accountId = ?";

        try (Connection conn = DriverManager.getConnection(url)) {
            conn.setAutoCommit(false);
    
            // Delete from clients table (if the account has a client associated)
            try (PreparedStatement clientStmt = conn.prepareStatement(deleteClientQuery)) {
                clientStmt.setInt(1, accountId);
                clientStmt.executeUpdate();
            }
    
            // Delete from instructors table (if the account has an instructor associated)
            try (PreparedStatement instructorStmt = conn.prepareStatement(deleteInstructorQuery)) {
                instructorStmt.setInt(1, accountId);
                instructorStmt.executeUpdate();
            }
    
            // Delete the account from the accounts table
            try (PreparedStatement accountStmt = conn.prepareStatement(deleteAccountQuery)) {
                accountStmt.setInt(1, accountId);
                int rowsAffected = accountStmt.executeUpdate();
                if (rowsAffected > 0) {
                    conn.commit();
                    System.out.println("Account and associated data deleted successfully.");
                    return true;
                } else {
                    // If no rows affected, rollback the transaction and return false
                    conn.rollback();
                    System.out.println("Account not found.");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            return false;
        }
    }
    // ====================================================================================================



    // ====================================================================================================
    // METHOD TO RETRIEVE ACCOUNT ID WITH EMAIL
    // ====================================================================================================
    public int getAccountIdByEmail(String email) {
        String query = "SELECT accountId FROM accounts WHERE email = ?";
    
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setString(1, email);
    
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("accountId");  // Return the accountId
                } else {
                    System.out.println("No account found with the provided email.");
                    return -1;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            return -1;
        }
    }
    // ====================================================================================================
}