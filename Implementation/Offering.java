import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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























}
