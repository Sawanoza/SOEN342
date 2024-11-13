import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TimeSlot {
    
    private String url = "jdbc:sqlite:Implementation/mydb.db";

    // ====================================================================================================
    // METHOD TO GET ALL TIMESLOT
    // ====================================================================================================
    public void getAllTimeslots() {
        String query = "SELECT t.timeslotId, t.day, t.date, t.startTime, t.endTime " +
                    "FROM timeslot t";

        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {
            
            // Print table header
            System.out.printf("%-15s %-10s %-10s %-10s %-10s%n", "Timeslot ID", "Day", "Date", "Start Time", "End Time");
            System.out.println("------------------------------------------------------------");

            // Iterate through the result set and print each timeslot
            while (rs.next()) {
                int timeslotId = rs.getInt("timeslotId");
                String day = rs.getString("day");
                int date = rs.getInt("date");
                String startTime = rs.getString("startTime");
                String endTime = rs.getString("endTime");

                // Print timeslot data in formatted columns
                System.out.printf("%-15d %-10s %-10d %-10s %-10s%n", timeslotId, day, date, startTime, endTime);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    // ====================================================================================================

}
