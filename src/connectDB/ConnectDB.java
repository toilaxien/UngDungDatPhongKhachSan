package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyKhachSan;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa"; 
    private static final String PASSWORD = "sapassword";  
    private static Connection connection = null;

    // Phương thức kết nối
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi kết nối SQL Server!");
            return null;
        }
    }


    // Phương thức đóng kết nối
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("Đã đóng kết nối SQL Server.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Kiểm tra kết nối
    public static void main(String[] args) {
        Connection conn = ConnectDB.getConnection(); // Kiểm tra kết nối
        ConnectDB.closeConnection(); // Đóng kết nối sau khi kiểm tra
    }
}
