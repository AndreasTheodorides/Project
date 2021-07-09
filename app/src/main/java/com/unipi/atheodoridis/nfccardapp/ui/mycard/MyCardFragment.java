package com.unipi.atheodoridis.nfccardapp.ui.mycard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.cardemulation.NfcFCardEmulation;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unipi.atheodoridis.nfccardapp.ProfileActivity;
import com.unipi.atheodoridis.nfccardapp.R;
import com.unipi.atheodoridis.nfccardapp.databinding.ActivityProfileBinding;
import com.unipi.atheodoridis.nfccardapp.ui.home.HomeFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MyCardFragment extends Fragment {
    private NfcAdapter adapter;
    Button button;
    ImageView imageView;
    TextView textView, am, fname, email, cardnum, uni, dep, year;
    private FirebaseUser firebaseUser;
    FirebaseDatabase db;
    private ActivityProfileBinding binding;
    SharedPreferences preferences;

    private MyCardViewModel myCardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myCardViewModel =
                new ViewModelProvider(this).get(MyCardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mycard, container, false);
        imageView = (ImageView) root.findViewById(R.id.imageView3);
        textView = (TextView) root.findViewById(R.id.textView);
        am = (TextView) root.findViewById(R.id.am1);
        fname = (TextView) root.findViewById(R.id.fname1);
        email = (TextView) root.findViewById(R.id.email1);
        uni = (TextView) root.findViewById(R.id.uni1);
        dep = (TextView) root.findViewById(R.id.dep1);
        year = (TextView) root.findViewById(R.id.year1);
        cardnum = (TextView) root.findViewById(R.id.cardnum1);
        db = FirebaseDatabase.getInstance();
        getInfo();
        myCardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    public void getInfo(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db.getReference("users/" + userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                   am.setText(task.getResult().child("AM").getValue().toString());
                   fname.setText(task.getResult().child("FirstName").getValue().toString());
                   email.setText(task.getResult().child("Email").getValue().toString());
                   cardnum.setText(task.getResult().child("CardNumber").getValue().toString());
                   uni.setText(task.getResult().child("University").getValue().toString());
                   dep.setText(task.getResult().child("Department").getValue().toString());
                   year.setText(task.getResult().child("Registration Date").getValue().toString());
                }
            }
        });

    }

}