package com.example.test1;

public class User {
    private String username;
    private String birthday;
    private String id ;
    private String name;
    private String phone;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", birthday='" + birthday + '\'' +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public User(){}
    public User(String username,String birthday, String name, String id ,String phone) {
        this.username = username;
        this.id=id;
        this.phone=phone;
        this.birthday=birthday;
        this.name=name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
