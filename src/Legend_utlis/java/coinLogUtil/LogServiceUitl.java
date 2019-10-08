package coinLogUtil;

import java.io.*;
import java.util.Properties;

public class LogServiceUitl {

    public static String endpoint = "";
    public static String access_key = "";
    public static String serect_key = "";

    static {



    }


    public static void main(String[] args) {

        Properties properties = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream("./src/legend_utlis/java/coinLogUtil/service.properties"));
            properties.load(in);
            System.out.println(properties.get("access_key"));


        } catch (Exception e) {
            e.printStackTrace();
        }

//        File file = new File(".");
//        File[] files = file.listFiles();
//        for (File file1 : files) {
//            System.out.println(file1.getName());
//        }


    }

}
