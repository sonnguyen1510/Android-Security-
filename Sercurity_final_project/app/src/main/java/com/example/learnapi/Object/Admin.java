package com.example.IrisREC.Object;

public class Admin {
    private String _id;
    private String Name;
    private String User_name;
    private String Age;
    private String Birthday;
    private boolean Gender;
    private int __v;

    public Admin(String _id, String Name, String User_name, String Age, String Birthday, boolean Gender, int __v) {
        this._id = _id;
        this.Name = Name;
        this.User_name = User_name;
        this.Age = Age;
        this.Birthday = Birthday;
        this.Gender = Gender;
        this.__v = __v;
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

    public void setName(String name) {
        Name = name;
    }

    public String getUser_name() {
        return User_name;
    }

    public void setUser_name(String user_name) {
        User_name = user_name;
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

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }
}
