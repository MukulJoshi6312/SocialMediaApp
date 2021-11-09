package com.example.socialmediaapp.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.socialmediaapp.R;
import com.example.socialmediaapp.databinding.FragmentAddBinding;
import com.example.socialmediaapp.model.PostModel;
import com.example.socialmediaapp.model.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class AddFragment extends Fragment {

    FragmentAddBinding binding;
    FirebaseAuth auth;
    Uri uri;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog dialog;


    public AddFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        dialog = new ProgressDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater,container,false);

        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Post Uploading");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);


        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    UserModel userModel = snapshot.getValue(UserModel.class);
                    Picasso.get()
                            .load(userModel.getProfile())
                            .placeholder(R.drawable.picture)
                            .into(binding.userProfile);
                    binding.userName.setText(userModel.getName());
                    binding.profession.setText(userModel.getProfession());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.postDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String description = binding.postDesc.getText().toString();

                if (!description.isEmpty()) {
                    binding.postButoon.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.follow_button));
                    binding.postButoon.setTextColor(getContext().getResources().getColor(R.color.white));
                    binding.postButoon.setEnabled(true);
                }else {
                    binding.postButoon.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.follow_act_btn));
                    binding.postButoon.setTextColor(getContext().getResources().getColor(R.color.black));
                    binding.postButoon.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        binding.selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,99);
            }
        });

        binding.postButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                final StorageReference reference = storage.getReference().child("posts")
                .child(FirebaseAuth.getInstance().getUid())
                .child(new Date().getTime()+"");
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                PostModel postModel = new PostModel();
                                postModel.setPostImage(uri.toString());
                                postModel.setPostedBy(FirebaseAuth.getInstance().getUid());
                                postModel.setPostDescription(binding.postDesc.getText().toString());
                                postModel.setPostedAt(new Date().getTime());

                                database.getReference().child("posts").push().setValue(postModel)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getContext(), "Posted Successfully", Toast.LENGTH_SHORT).show();
                                                FragmentTransaction fragmentTransaction = getActivity()
                                                        .getSupportFragmentManager().beginTransaction();
                                                fragmentTransaction.replace(R.id.container, new HomeFragment());
                                                fragmentTransaction.addToBackStack(fragmentTransaction.toString()).commit();
                                                dialog.dismiss();
                                            }
                                        });

                            }
                        });
                    }
                });

            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 99){
            if (data.getData()!=null){
                uri = data.getData();
                binding.postImage.setImageURI(uri);
                binding.postImage.setVisibility(View.VISIBLE);
                binding.postButoon.setBackgroundDrawable(ContextCompat.getDrawable(getContext(),R.drawable.follow_button));
                binding.postButoon.setTextColor(getContext().getResources().getColor(R.color.white));
                binding.postButoon.setEnabled(true);
            }
        }

    }
}