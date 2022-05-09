package com.example.learnapi.Object;

public class User {


    private String _id;
    private String Name;
    private String Age;
    private String Birthday;
    private String Email;
    private boolean Gender;
    private String ImageEye;
    private int __v;

    public User(String _id, String name, String age, String birthday, String email, boolean gender, String imageEye, int __v) {
        this._id = _id;
        Name = name;
        Age = age;
        Birthday = birthday;
        Email = email;
        Gender = gender;
        ImageEye = imageEye;
        this.__v = __v;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImageEye() {

        return ImageEye;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
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

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        Name = Name;
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
