package com.example.tourmeneger;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.tourmeneger.adapter.UserAdapter;
import com.example.tourmeneger.model.Groups;
import com.example.tourmeneger.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toogle;
    CircleImageView profile_image;

    TextView username, groupName;

    FirebaseUser firebaseUser;
    DatabaseReference reference, usersreference, groupsreference;

    Button btn_new_group, btn_join_to_group;

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;
    private List<Groups> groupList;
    private String currentgroupName;

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

        groupName = findViewById(R.id.groupNameView);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                groupName.setText(user.getCurrgroup());
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

        btn_new_group = findViewById(R.id.btn_new_group);
        btn_new_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestNewGroup();
            }
        });

        btn_join_to_group = findViewById(R.id.btn_join_to_group);
        btn_join_to_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestJoin();
            }
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        groupList = new ArrayList<>();

                getCurrentGroupName(new MyCallback() {
                    @Override
                    public void onCallback(String value) {
                        currentgroupName = value;

                        groupsreference = FirebaseDatabase.getInstance().getReference("Groups").child(currentgroupName).child("members");
                        groupsreference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                groupList.clear();
                                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Groups member = snapshot.getValue(Groups.class);
                                    groupList.add(member);
                                }

                                readGroupMembers();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
    }

    private void readGroupMembers() {
        mUsers = new ArrayList<>();

        usersreference = FirebaseDatabase.getInstance().getReference("Users");
        usersreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUsers.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    for(Groups member : groupList) {
                        if(user.getId().equals(member.getId())) {
                            mUsers.add(user);
                        }
                    }
                }

                userAdapter = new UserAdapter(getApplicationContext(), mUsers, false);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void requestJoin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
        builder.setTitle("Enter group name");
        final EditText groupNameField = new EditText(MainActivity.this);
        groupNameField.setHint("SomeNameThatOnlyYouThinkIsFunny");
        builder.setView(groupNameField);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName_txt = groupNameField.getText().toString();
                if (TextUtils.isEmpty(groupName_txt)){
                    Toast.makeText(MainActivity.this, "Write group name", Toast.LENGTH_SHORT).show();
                }
                else{
                    joinToGroup(groupName_txt);
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void joinToGroup(final String nameOfGroup) {
        groupsreference = FirebaseDatabase.getInstance().getReference("Groups").child(nameOfGroup);
        groupsreference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    dataSnapshot.child("members").getChildrenCount();
                    groupsreference.child("members").child(firebaseUser.getUid()).child("id").setValue(firebaseUser.getUid());
                    usersreference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                    usersreference.child("currgroup").setValue(nameOfGroup);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public interface MyCallback {
        void onCallback(String value);
    }

    private void getCurrentGroupName(final MyCallback myCallback){
        final DatabaseReference user = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user1 = dataSnapshot.getValue(User.class);
                myCallback.onCallback(user1.getCurrgroup());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void requestNewGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
        builder.setTitle("Enter group name:");
        final EditText groupNameField = new EditText(MainActivity.this);
        groupNameField.setHint("Ostre cienie mgły");
        builder.setView(groupNameField);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupName_txt = groupNameField.getText().toString();
                if (TextUtils.isEmpty(groupName_txt)){
                    Toast.makeText(MainActivity.this, "Write group name", Toast.LENGTH_SHORT).show();
                }
                else{
                    crateNewGroup(groupName_txt);
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void crateNewGroup(String groupName_txt) {
        groupsreference = FirebaseDatabase.getInstance().getReference("Groups").child(groupName_txt);
        groupsreference.child("creator").setValue(firebaseUser.getUid());
        groupsreference = FirebaseDatabase.getInstance().getReference("Groups").child(groupName_txt).child("members");
        groupsreference.child(firebaseUser.getUid()).child("id").setValue(firebaseUser.getUid());
        usersreference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        usersreference.child("currgroup").setValue(groupName_txt);
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
            case R.id.koszty:
                Toast.makeText(this, "Wybrales obliczanie kosztow", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, Cost.class));
                break;
        }
        drawerLayout.closeDrawers();

        return false;
    }
}
