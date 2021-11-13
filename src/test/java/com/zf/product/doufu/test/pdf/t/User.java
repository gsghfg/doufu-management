package com.zf.product.doufu.test.pdf.t;

public class User {
    private String name;    //姓名
    private int age ;       //年龄
    private int height; //身高
    private String address;  //住址
    private String sex;     //性别
    private String love;    //爱好

    public User(String name, int age, int height, String address, String sex, String love) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.address = address;
        this.sex = sex;
        this.love = love;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getLove() {
        return love;
    }

    public void setLove(String love) {
        this.love = love;
    }

    // getter和setter方法略
}