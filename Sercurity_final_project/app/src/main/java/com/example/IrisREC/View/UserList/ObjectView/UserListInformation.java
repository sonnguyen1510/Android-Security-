package com.example.IrisREC.View.UserList.ObjectView;

import android.content.Context;

import com.example.IrisREC.Object.User;

import java.util.List;

public class UserListInformation {
    public List<User> UserData;
    public Context context;
    public int RecycleViewPublishLayout;

    public UserListInformation(List<User> userData, Context context ,int RecycleViewPublishLayout) {
        UserData = userData;
        this.context = context;
        this.RecycleViewPublishLayout =RecycleViewPublishLayout;
    }
}
