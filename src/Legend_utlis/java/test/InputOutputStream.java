package test;

import java.io.*;

public class InputOutputStream {

    public static void main(String[] args) throws Exception {

//        try {
//            FileInputStream fileInputStream = new FileInputStream("./src/Legend_utlis/java/test/LogTest.java");
//            byte[] buff = new byte[1024];
//            int hasRead = 0;
//            FileOutputStream fileOutputStream = new FileOutputStream("./src/Legend_utlis/java/test/newFile.txt");
//
//            while ((hasRead = fileInputStream.read(buff)) > 0){
//                System.out.print(new String(buff,0,hasRead));
//                fileOutputStream.write(buff,0,hasRead);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        InputStream in = Object.class.getClassLoader().getResourceAsStream("db.properties");
        System.out.println(in);


    }

}
