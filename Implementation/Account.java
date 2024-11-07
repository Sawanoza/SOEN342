import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class Account {
    // SQLite database file path
    private String url = "jdbc:sqlite:mydb.db";
        



    public void getAccounts() {

        String query = "SELECT * FROM accounts ORDER BY id";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id") + "\t| Name: " + rs.getString("name") + "\t| Email: " + rs.getString("email") + "\t| " + "Phone Number: " + rs.getInt("phoneNumber") + " \t| Password: " + rs.getString("password"));
                }

            } catch (SQLException e) {
                System.out.println("SQL Error: " + e.getMessage());
            }
    }


    //method to check if the user exists with the provided email and password
    public boolean authenticateUser(String email, String password) {
        String query = "SELECT * FROM accounts WHERE email = ? AND password = ?";
        
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
            //set parameters for the query
            stmt.setString(1, email);
            stmt.setString(2, password);

            //execute query and check if result is found
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return true;  //user found with matching credentials
                } else {
                    return false; //no match found
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            return false;
        }
    }


    //method to create a new account
    public boolean createAccount(String name, String email, String phoneNumber, String password) {
        String query = "INSERT INTO accounts (name, email, phoneNumber, password) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(query)) {
             
           //set the parameters for the query
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phoneNumber);
            stmt.setString(4, password);
            
            //execute the query to insert the new user
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; //return true if the insertion was successful
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            return false; //return false if an error occurred during insertion
        }
    }



}