package com.example.IrisREC.View.UserList.ViewControl;

import android.content.Context;
import android.os.AsyncTask;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.IrisREC.Object.User;
import com.example.IrisREC.View.UserList.ObjectView.UserListInformation;
import com.example.IrisREC.View.UserList.View.UserList_RecycleView_Adapter;

import java.util.List;

public class UserList_ViewControl extends AsyncTask<UserListInformation, UserListInformation, Boolean> {
    public RecyclerView PublishUserData;

    @Override
    protected Boolean doInBackground(UserListInformation... userListInformations) {
        publishProgress(userListInformations[0]);
        return true;
    }

    @Override
    protected void onProgressUpdate(UserListInformation... values) {
        UserListInformation Upload = values[0];
        //Set Data

        List<User> UserData = Upload.UserData;
        UserList_RecycleView_Adapter adapter = new UserList_RecycleView_Adapter(UserData,Upload.context,Upload.RecycleViewPublishLayout);
        PublishUserData.findViewById(Upload.RecycleViewPublishLayout);
        PublishUserData.setAdapter(adapter);
        PublishUserData.setLayoutManager(new LinearLayoutManager(Upload.context));
    }
}
