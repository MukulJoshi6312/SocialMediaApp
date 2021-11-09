package com.example.socialmediaapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.socialmediaapp.R;
import com.example.socialmediaapp.adapter.NotificationAdapter;
import com.example.socialmediaapp.model.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Notification2 extends Fragment {

    RecyclerView recyclerView;
    List<Notification> list;

    FirebaseDatabase database;

    NotificationAdapter adapter;

    public Notification2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification2, container, false);

        recyclerView = view.findViewById(R.id.notificationRecyclerView);
        list = new ArrayList<>();
//        list.add(new Notification(R.drawable.sotry,"<b>Mukul Joshi</b> Liked your post","Just Now"));
//        list.add(new Notification(R.drawable.sotry,"<b>Mukul Joshi</b> Liked your post","Just Now"));
//        list.add(new Notification(R.drawable.sotry,"<b>tushar Joshi</b> Liked your post","Just Now"));
//        list.add(new Notification(R.drawable.sotry,"<b>Mukul Joshi</b> Liked your post","Just Now"));
//        list.add(new Notification(R.drawable.sotry,"<b>Mukul Joshi</b> Liked your post","Just Now"));
//        list.add(new Notification(R.drawable.sotry,"<b>Mukul Joshi</b> Liked your post","Just Now"));
//        list.add(new Notification(R.drawable.sotry,"<b>Mukul Joshi</b> Liked your post","Just Now"));
//        list.add(new Notification(R.drawable.sotry,"<b>Mukul Joshi</b> Liked your post","Just Now"));
//        list.add(new Notification(R.drawable.sotry,"<b>Mukul Joshi</b> Liked your post","Just Now"));
//        list.add(new Notification(R.drawable.sotry,"<b>Mukul Joshi</b> Liked your post","Just Now"));
//        list.add(new Notification(R.drawable.sotry,"<b>Mukul Joshi</b> Liked your post","Just Now"));


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        adapter = new NotificationAdapter(list,getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        database.getReference()
                .child("notification")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            Notification notification = dataSnapshot.getValue(Notification.class);
                            notification.setNotificationId(dataSnapshot.getKey());
                            list.add(notification);
                        }
                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        return view;
    }
}