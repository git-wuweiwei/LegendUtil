package coinLogUtil;

import java.io.File;
import java.sql.*;

public class locatinTest implements Runnable {

    private static String username = "root";
    private static String password = "www19920912";
    private static String url = "jdbc:mysql://47.244.186.111:10306/legend_test";
    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
//        Statement statement = connection.createStatement();
//        String sql = "select * from location_test";
//        ResultSet resultSet = statement.executeQuery(sql);
//        while (resultSet.next()){
//            String time = resultSet.getString("time");
//            String geohash = resultSet.getString("geohash");
//            String value = resultSet.getString("value");
//            String country = resultSet.getString("country");
//            System.out.println("time: " + time +
//                    "; geohash: " + geohash +
//                    "; value " + value +
//                    "; country " + country);
//        }
        File file = new File(".");
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            System.out.println(files[i]);
        }
        System.out.println(file.getAbsoluteFile().getParent());
    }

    @Override
    public void run() {

    }
}