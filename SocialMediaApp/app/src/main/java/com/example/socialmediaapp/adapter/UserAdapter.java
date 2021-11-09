package com.example.socialmediaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmediaapp.R;
import com.example.socialmediaapp.databinding.UserSampleBinding;
import com.example.socialmediaapp.model.Follow;
import com.example.socialmediaapp.model.Notification;
import com.example.socialmediaapp.model.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    Context context;
    ArrayList<UserModel> list;

    public UserAdapter(Context context, ArrayList<UserModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_sample,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserModel model = list.get(position);
        Picasso.get().load(model.getProfile()).placeholder(R.drawable.sotry).into(holder.binding.userProfile);
        holder.binding.userName.setText(model.getName());
        holder.binding.profession.setText(model.getProfession());


        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(model.getUserId())
                .child("followers")
                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    holder.binding.followButton.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.follow_act_btn));
                    holder.binding.followButton.setText("Following");
                    holder.binding.followButton.setTextColor(context.getResources().getColor(R.color.black));
                    holder.binding.followButton.setEnabled(false);
                }
                else{
                    holder.binding.followButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Follow follow = new Follow();
                            follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
                            follow.setFollowedAt(new Date().getTime());
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Users").child(model.getUserId())
                                    .child("followers").child(FirebaseAuth.getInstance().getUid())
                                    .setValue(follow).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Users").child(model.getUserId())
                                            .child("followerCount")
                                            .setValue(model.getFollowerCount() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            holder.binding.followButton.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.follow_act_btn));
                                            holder.binding.followButton.setText("Following");
                                            holder.binding.followButton.setTextColor(context.getResources().getColor(R.color.black));
                                            holder.binding.followButton.setEnabled(false);
                                            Toast.makeText(context, "You Followed "+model.getName(), Toast.LENGTH_SHORT).show();
                                            Notification notification = new Notification();
                                            notification.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                            notification.setNotificationAt(new Date().getTime());
                                            notification.setType("follow");

                                            FirebaseDatabase.getInstance().getReference()
                                                    .child("notification")
                                                    .child(model.getUserId())
                                                    .push()
                                                    .setValue(notification);
                                        }
                                    });
                                }
                            });

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



//        holder.binding.followButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Follow follow = new Follow();
//                follow.setFollowedBy(FirebaseAuth.getInstance().getUid());
//                follow.setFollowedAt(new Date().getTime());
//                FirebaseDatabase.getInstance().getReference()
//                        .child("Users").child(model.getUserId())
//                        .child("followers").child(FirebaseAuth.getInstance().getUid())
//                        .setValue(follow).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        FirebaseDatabase.getInstance().getReference()
//                                .child("Users").child(model.getUserId())
//                                .child("followerCount")
//                                .setValue(model.getFollowerCount() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                holder.binding.followButton.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.follow_act_btn));
//                                holder.binding.followButton.setText("Following");
//                                holder.binding.followButton.setTextColor(context.getResources().getColor(R.color.black));
//                                holder.binding.followButton.setEnabled(false);
//                                Toast.makeText(context, "You Followed "+model.getName(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                });
//
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        UserSampleBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = UserSampleBinding.bind(itemView);

        }
    }
}
