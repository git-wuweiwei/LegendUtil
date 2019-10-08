package coinLogUtil;

import java.util.Date;

public class Students {

    private Date createDate;
    private String name;
    private String password;
    private int year;

    @Override
    public String toString() {
        return "Students{" +
                "createDate=" + createDate +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", year=" + year +
                '}';
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
