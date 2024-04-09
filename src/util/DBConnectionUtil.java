package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static util.ConnectionConst.*;

public class DBConnectionUtil {
    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
