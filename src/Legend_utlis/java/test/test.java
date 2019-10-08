package test;

import coinLogUtil.JdbcUtils;
import coinLogUtil.Students;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class test {


    public List<Students> findAll() {
        List<Students> studentsList = new ArrayList<Students>();
        Connection connection = JdbcUtils.getConnection();
        String sql = "select * from webdemo1";
        ResultSet resultSet = null;
        try {
            resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()){
                Students students = new Students();
                students.setCreateDate(resultSet.getTimestamp(1));
                students.setName(resultSet.getString(2));
                students.setPassword(resultSet.getString(3));
                students.setYear(resultSet.getInt(4));
                studentsList.add(students);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return studentsList;
    }


    public List<Students> findAllStudents(){
        List<Students> studentsList = new ArrayList<>();
        Connection connection = JdbcUtils.getConnection();
        String sql = "select * from webdemo1;";
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while(resultSet.next()){
                Students students = new Students();
                students.setCreateDate(resultSet.getTimestamp(1));
                students.setName(resultSet.getString(2));
                students.setPassword(resultSet.getString(3));
                students.setYear(resultSet.getInt(4));
                studentsList.add(students);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return studentsList;
    }

    public static void main(String[] args) {

        System.out.println(new test().findAllStudents());


    }
}


