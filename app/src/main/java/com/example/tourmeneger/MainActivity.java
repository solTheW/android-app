package com.example.tourmeneger;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.tourmeneger.model.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle toogle;
    CircleImageView profile_image;
    TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    CustomCalendarView customCalendarView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Main");

        drawerLayout=findViewById(R.id.drawer);
        navigationView=findViewById(R.id.navigationView);

        View headerView = navigationView.getHeaderView(0);
        username = headerView.findViewById(R.id.username);
        profile_image = headerView.findViewById(R.id.profile_image);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if(user.getImageURl().equals("default")){
                    profile_image.setImageResource(R.drawable.grumpy_cat);
                } else {
                    Glide.with(MainActivity.this).load(user.getImageURl()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        toogle=new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.chat:
                startActivity(new Intent(MainActivity.this, ChatActivity.class));
                break;
            case R.id.schedule:
                Toast.makeText(this, "klikłeś schedule", Toast.LENGTH_LONG).show();
                break;
            case R.id.map:
                startActivity(new Intent(MainActivity.this, MapActivity.class));
                break;
            case R.id.profile:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                return true;
            case R.id.kalendarz:
                Toast.makeText(this, "Wybrales Kalendarz", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, KalendarzActivity.class));
                break;
        }
        drawerLayout.closeDrawers();

        return false;
    }

}
