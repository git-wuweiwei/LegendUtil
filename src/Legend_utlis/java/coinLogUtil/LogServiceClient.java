package coinLogUtil;

import com.aliyun.openservices.log.Client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class LogServiceClient {


    private static String access_key ;
    private static String serect_key ;
    private static String endpoint;
    private static String project ;
    private static String logstore;
    private static Client client = null;

    static {
        try {
            Properties properties = readpro();
            access_key = properties.getProperty("access_key");
            serect_key = properties.getProperty("secret_key");
            endpoint = properties.getProperty("endpoint");
            project = properties.getProperty("project");
            logstore = properties.getProperty("logstore");
            client = new Client(endpoint, access_key, serect_key);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static Client getLogClient(){

        return client;
    }

    public static Properties readpro() throws FileNotFoundException {
        Properties properties = new Properties();
//        InputStream inputStream = Object.class.getResourceAsStream("service.properties");
        FileInputStream fileInputStream = new FileInputStream("D:\\java\\IdeaProject\\aliyun_LogService\\src\\Legend_utlis\\java\\coinLogUtil\\service.properties");

        try {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
//        System.out.println(properties.getProperty("project"));
    }



}
