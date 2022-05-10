package com.example.IrisREC.View.UserList.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.IrisREC.Function.FunctionImplement;
import com.example.IrisREC.Object.User;
import com.example.IrisREC.R;

import java.util.List;

public class UserList_RecycleView_Adapter extends RecyclerView.Adapter<UserList_RecycleView_Adapter.UserCard>{

    public List<User> UserData ;
    public Context context;

    public UserList_RecycleView_Adapter(List<User> userData, Context context) {
        UserData = userData;
        this.context = context;

    }

    @NonNull
    @Override
    public UserCard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View UserView = LayoutInflater
                .from(context)
                .inflate(R.layout.user_card,parent,false);
        return new UserCard(UserView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCard holder, int position) {
        User User = UserData.get(position);
        holder.Name.setText(User.getName()+"");
        holder.Email.setText(User.getEmail()+"");

        FunctionImplement.ConvertAndSetImageView Decode = new FunctionImplement.ConvertAndSetImageView(holder.ImageEye,context);
        Decode.execute(User.getImageEye());
    }

    @Override
    public int getItemCount() {
        return UserData.size();
    }


    public class UserCard extends RecyclerView.ViewHolder {
        public ImageView ImageEye;
        public TextView Name;
        public TextView Email;

        public UserCard(@NonNull View itemView) {
            super(itemView);
            itemView = itemView;
            ImageEye = itemView.findViewById(R.id.UserCard_EyeImage);
            Name = itemView.findViewById(R.id.UserCard_Name);
            Email = itemView.findViewById(R.id.UserCard_Email);
        }
    }
}
