package Schedule;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Schedule {
    
    private String url = "jdbc:sqlite:Implementation/mydb.db";

    // ====================================================================================================
    // METHOD TO GET ALL SCHEDULES
    // ====================================================================================================
    public void getAllSchedules() {
        String query = "SELECT s.scheduleId, t.day, t.date, t.startTime, t.endTime, l.city " +
                    "FROM schedule s " +
                    "JOIN timeslot t ON s.timeslotId = t.timeslotId " +
                    "JOIN location l ON s.locationId = l.locationId";

        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {
            
            System.out.printf("%-10s %-10s %-10s %-10s %-10s %-20s%n", "Schedule ID", "Day", "Date", "Start Time", "End Time", "City");
            System.out.println("------------------------------------------------------------------");

            while (rs.next()) {
                int scheduleId = rs.getInt("scheduleId");
                String day = rs.getString("day");
                int date = rs.getInt("date");
                String startTime = rs.getString("startTime");
                String endTime = rs.getString("endTime");
                String city = rs.getString("city");

                System.out.printf("%-10d %-10s %-10d %-10s %-10s %-20s%n", scheduleId, day, date, startTime, endTime, city);
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
    }
    // ====================================================================================================

}
