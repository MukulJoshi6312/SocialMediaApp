package com.example.socialmediaapp.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmediaapp.R;
import com.example.socialmediaapp.databinding.CommentSampleBinding;
import com.example.socialmediaapp.model.Comment;
import com.example.socialmediaapp.model.UserModel;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    Context context;
    ArrayList<Comment> list;

    public CommentAdapter(Context context, ArrayList<Comment> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_sample,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = list.get(position);
       // holder.binding.comment.setText(comment.getCommentBody());
        String time = TimeAgo.using(comment.getCommentedAt());

        holder.binding.time.setText(time);

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(comment.getCommentedBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        Picasso.get().load(userModel.getProfile())
                                .placeholder(R.drawable.picture)
                                .into(holder.binding.userProfile);

                        holder.binding.comment.setText(Html.fromHtml("<b>"+userModel.getName()+"</b>" +" "+comment.getCommentBody()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CommentSampleBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CommentSampleBinding.bind(itemView);
        }
    }
}
