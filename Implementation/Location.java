import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Location {
    
    private String url = "jdbc:sqlite:Implementation/mydb.db";

    // ====================================================================================================
    // METHOD TO GET ALL LOCATIONS
    // ====================================================================================================
    public void getAllLocations() {
        String query = "SELECT * FROM location";

        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {
            
            System.out.printf("%-10s %-20s%n", "Location ID", "City");
            System.out.println("-----------------------");

            while (rs.next()) {
                int locationId = rs.getInt("locationId");
                String city = rs.getString("city");

                System.out.printf("%-10d %-20s%n", locationId, city);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    // ====================================================================================================

}
