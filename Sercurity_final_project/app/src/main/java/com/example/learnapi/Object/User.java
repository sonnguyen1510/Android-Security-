package com.example.learnapi.Object;

public class User {
    private String _id;
    private String User_name;
    private String Age;
    private String Birthday;
    private boolean Gender;
    private String ImageEye;

    public User(String _id, String user_name, String age, String birthday, boolean gender, String imageEye) {
        this._id = _id;
        User_name = user_name;
        Age = age;
        Birthday = birthday;
        Gender = gender;
        ImageEye = imageEye;
    }

    public String getImageEye() {

        return ImageEye;
    }

    public void setImageEye(String imageEye) {
        ImageEye = imageEye;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser_Name() {
        return User_name;
    }

    public void setUser_Name(String user_Name) {
        User_name = user_Name;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getBirthday() {
        return Birthday;
    }

    public void setBirthday(String birthday) {
        Birthday = birthday;
    }

    public boolean isGender() {
        return Gender;
    }

    public void setGender(boolean gender) {
        Gender = gender;
    }





}
