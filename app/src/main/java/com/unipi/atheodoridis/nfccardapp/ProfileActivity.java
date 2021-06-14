package com.unipi.atheodoridis.nfccardapp;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.unipi.atheodoridis.nfccardapp.databinding.ActivityProfileBinding;
import com.unipi.atheodoridis.nfccardapp.ui.home.HomeFragment;
import com.unipi.atheodoridis.nfccardapp.ui.mycard.MyCardFragment;
import com.unipi.atheodoridis.nfccardapp.ui.help.HelpFragment;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Listener {
    private FirebaseUser firebaseUser;
    private boolean isDialogDisplayed = false;
    private FirebaseDatabase db;
    private ActivityProfileBinding binding;
    TextView textView;
    private Boolean isSuccess = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_profile);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        init();
        updateUI(); // Update UI with user's info.
        textView = findViewById(R.id.textViewToolbar);

        //setContentView(R.layout.activity_profile);
        setSupportActionBar(binding.toolbar.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.toolbar_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(
                        this, binding.root, binding.toolbar.getRoot(),
                        R.string.navigation_drawer_close,
                        R.string.navigation_drawer_close);
        binding.root.addDrawerListener(toggle);
        // Change drawer arrow icon
        toggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, R.color.design_default_color_on_secondary));
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);
        binding.navView.setCheckedItem(R.id.nav_home);

        // Add the home fragment to show it in the frame layout of main activity.
        HomeFragment fragmentDefault = new HomeFragment();
        setFragment(fragmentDefault, "FRAGMENT_HOME");
        textView.setText("Home");

    }
    @Override
    public void onBackPressed() {
        if (binding.root.isDrawerOpen(GravityCompat.START)) {
            binding.root.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        if (id == R.id.nav_home)
        {
            // Products Fragment
            fragment = new HomeFragment();
            setFragment(fragment, "FRAGMENT_HOME");

            textView.setText("Home");
        }
        else if (id == R.id.nav_mycard)
        {
            fragment = new MyCardFragment();
            setFragment(fragment, "FRAGMENT_MY_CARD");
            textView.setText("My Card");
        }
        else if (id == R.id.nav_help)
        {
            fragment = new HelpFragment();
            setFragment(fragment, "FRAGMENT_HELP");
            textView.setText("Help");
        }
        else if (id == R.id.nav_exit)
        {

            this.finish();
            FirebaseAuth.getInstance().signOut();
            //System.exit(0);
            return false;
        }

        if (fragment == null) {
            fragment = new HomeFragment();
            setFragment(fragment, "FRAGMENT_HOME");
        }

        DrawerLayout drawer = findViewById(R.id.root);
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
        fragmentTransaction.replace(R.id.fragment_container, fragment, tagName);
        fragmentTransaction.commit();

    }

    public void init() {
        db = FirebaseDatabase.getInstance();
        //db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public void updateUI(){
        if (firebaseUser!=null){
            String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            DatabaseReference reference = db.getReference("users/" + userId);
            System.out.println("Andreas------------ " + userId);
                reference.child("AM").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (task.isSuccessful()) {
                            String am =String.valueOf(Objects.requireNonNull(task.getResult()).getValue());
                            View headerView = binding.navView.getHeaderView(0);
                            TextView textViewName = headerView.findViewById(R.id.textViewNavBar_AM);
                            textViewName.setText(Objects.requireNonNull(am));
                            System.out.println("Andreas------------ " + userId);
                        }
                        else {
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                        }
                    }
                });
                reference.child("FirstName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        String fname = String.valueOf(Objects.requireNonNull(task.getResult()).getValue());
                        reference.child("LastName").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                String lname = String.valueOf(Objects.requireNonNull(task.getResult()).getValue());
                                View headerView = binding.navView.getHeaderView(0);
                                TextView textViewName = headerView.findViewById(R.id.textViewNavBar_Name);
                                textViewName.setText(Objects.requireNonNull(fname+" "+lname));
                                System.out.println(fname);
                            }
                        });


                    }

                });
        }
    }

    @Override
    public void onDialogDisplayed() {
        isDialogDisplayed = true;
    }

    @Override
    public void onDialogDismissed() {
        isDialogDisplayed = false;
        isSuccess = false;
    }
}