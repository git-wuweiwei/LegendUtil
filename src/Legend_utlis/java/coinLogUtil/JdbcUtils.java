package coinLogUtil;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class JdbcUtils {

    private static String driver;
    private static String url = null;
    private static String username = null;
    private static String password = null;
    public static Connection connection = null;

    static {
        try {
            InputStream in = JdbcUtils.class.getClassLoader().getResourceAsStream("coinLogUtil/db.properties");
            System.out.println(in);
            Properties properties = new Properties();
            properties.load(in);
            driver = properties.getProperty("driver");
            url = properties.getProperty("url");
            username = properties.getProperty("username");
            password = properties.getProperty("password");
            Class.forName(driver);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static Connection getConnection() {

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return connection;
    }

    public static void main(String[] args) throws SQLException {
        Connection connection = JdbcUtils.getConnection();
        Statement statement = connection.createStatement();
        String sql = "select * from location_test;";
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            String time = resultSet.getString("time");
            String value = resultSet.getString("value");
            String geohash = resultSet.getString("geohash");
            String country = resultSet.getString("country");
            System.out.println("time " + time + "; " +
                    "value" + value + "; " +
                    "geohash " + geohash + "; " +
                    "country " + country
            );
        }
    }

    public void setName(String name) {

    }


}