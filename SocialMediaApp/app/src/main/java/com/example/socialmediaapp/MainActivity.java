package com.example.socialmediaapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.socialmediaapp.Fragments.AddFragment;
import com.example.socialmediaapp.Fragments.HomeFragment;
import com.example.socialmediaapp.Fragments.Notification2;
import com.example.socialmediaapp.Fragments.NotificationFragment;
import com.example.socialmediaapp.Fragments.ProfileFragment;
import com.example.socialmediaapp.Fragments.SearchFragment;
import com.example.socialmediaapp.Registration.LoginActivity;
import com.example.socialmediaapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        MainActivity.this.setTitle("My Profile");

        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,new HomeFragment());
        transaction.commit();

        auth = FirebaseAuth.getInstance();


        binding.toolbar.setVisibility(View.GONE);

        binding.readableBottomBar.setOnItemSelectListener(i -> {
            FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();

            switch (i) {
                case 0:
                    binding.toolbar.setVisibility(View.GONE);
                    transaction1.replace(R.id.container,new HomeFragment());
                    break;

                case 1:
                    binding.toolbar.setVisibility(View.GONE);
                    transaction1.replace(R.id.container,new Notification2());
                    break;
                case 2:
                    binding.toolbar.setVisibility(View.GONE);
                    transaction1.replace(R.id.container,new AddFragment());
                    break;
                case 3:
                    binding.toolbar.setVisibility(View.GONE);
                    transaction1.replace(R.id.container,new SearchFragment());
                    break;
                case 4:
                    binding.toolbar.setVisibility(View.VISIBLE);
                    transaction1.replace(R.id.container,new ProfileFragment());
                    break;
            }
            transaction1.addToBackStack(null);
            transaction1.commit();
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Logout...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}