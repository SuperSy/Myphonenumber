package model;

/**
 * Created by Sy on 2016/2/25.
 */
public class People {
    String name;
    String num;
    String email;

    public People(){}

    public People(String name, String num, String email) {
        this.name = name;
        this.num = num;
        this.email = email;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
