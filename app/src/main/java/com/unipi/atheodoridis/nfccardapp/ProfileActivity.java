package com.unipi.atheodoridis.nfccardapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unipi.atheodoridis.nfccardapp.databinding.ActivityProfileBinding;
import com.unipi.atheodoridis.nfccardapp.model.UserModel;
import com.unipi.atheodoridis.nfccardapp.ui.home.HomeFragment;
import com.unipi.atheodoridis.nfccardapp.ui.mycard.MyCardFragment;
import com.unipi.atheodoridis.nfccardapp.ui.settings.SettingsFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseUser firebaseUser;
   // private FirebaseFirestore db;
    private FirebaseDatabase database;
    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) this);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_mycard, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        binding.navView.setNavigationItemSelectedListener(this);
        binding.navView.setCheckedItem(R.id.nav_home);
        init();
        updateUI();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
         Handle action bar item clicks here. The action bar will
         automatically handle clicks on the Home/Up button, so long
         as you specify a parent activity in AndroidManifest.xml.
        */
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
            setFragment(fragment, "Home_Fragment");
//            Intent intent = new Intent(this,HomeFragment.class);
//            startActivity(intent);
        } else if (id == R.id.nav_mycard) {
            fragment = new MyCardFragment();
            setFragment(fragment, "My_card_fragment");
//            Intent intent = new Intent(this,MyCardFragment.class);
//            startActivity(intent);
        }
        else if (id == R.id.nav_settings) {
            fragment = new SettingsFragment();
            setFragment(fragment, "Settings_Fragment");
//            Intent intent = new Intent(this,SettingsFragment.class);
//            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void setFragment(Fragment fragment, String tagName)
    {
        //get current fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();

        //get fragment transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //set new fragment in fragment_container (FrameLayout)
        fragmentTransaction.replace(R.id.fragment_container_view_tag, fragment, tagName);
        fragmentTransaction.commit();

    }

    public void init() {
        database = FirebaseDatabase.getInstance();
        //db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public void updateUI(){
        if (firebaseUser!=null){
            String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            DatabaseReference reference = database.getReference("users/" + userId);
            System.out.println("Andreas------------ " + userId);
//                reference.child("AM").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DataSnapshot> task) {
//                        if (!task.isSuccessful()) {
//                            System.out.println("Andreas------------ " + userId);
//                        }
//                        else {
//                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                        }
//                    }
//                });
//                    @Override
//                    public void onComplete(@NonNull Task<DataSnapshot> task) {
//                        System.out.println("Andreas------------ " + userId);
//                        String am = String.valueOf(Objects.requireNonNull(task.getResult()).getValue());
//                        View headerView = binding.navView.getHeaderView(0);
//                        TextView textViewName = headerView.findViewById(R.id.textViewNavBar_AM);
//                        textViewName.setText(Objects.requireNonNull(am));
//                        System.out.println(am);
//                    }
//                });
//                reference.child("FirstName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DataSnapshot> task) {
//                        String fname = String.valueOf(Objects.requireNonNull(task.getResult()).getValue());
//                        View headerView = binding.navView.getHeaderView(0);
//                        TextView textViewName = headerView.findViewById(R.id.textViewNavBar_Name);
//                        textViewName.setText(Objects.requireNonNull(fname));
//                        System.out.println(fname);
//                    }
//
//                });
        }
    }
}