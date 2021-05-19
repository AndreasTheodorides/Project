package com.unipi.atheodoridis.nfccardapp.ui.mycard;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.cardemulation.NfcFCardEmulation;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyCardFragment extends Fragment {
    private NfcAdapter adapter;
    Button button;
    ImageView imageView;
    TextView textView;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase db;
    private ActivityProfileBinding binding;

    private MyCardViewModel myCardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        checkCard();
        //button = (Button) button.findViewById(R.id.button4);
        imageView = (ImageView) imageView.findViewById(R.id.imageView3);
        textView = (TextView) textView.findViewById(R.id.textView);
        // View v = inflater.inflate(R.layout.fragment_mycard, container, false);

//
        myCardViewModel =
                new ViewModelProvider(this).get(MyCardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mycard, container, false);
        button = (Button) root.findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genCard();
            }
        });


        myCardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    public void genCard() {
        final int min = 100000000;
        final int max = 999999999;
        final int random = new Random().nextInt((max - min) + 1) + min;
        String cardNum = String.valueOf(random);
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference myRef = db.getReference("users/" + userId);
        myRef.child("CardNum").setValue(cardNum);
    }

    public void checkCard(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = db.getReference("users/" + userId);
        ref.child("CardNumber").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    String cardNum = String.valueOf(task.getResult().getValue());
                    if (cardNum.equals("")){
                        textView.setVisibility(View.GONE);
                    }
                    //Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    String cardNum = String.valueOf(task.getResult().getValue());
                    if (!cardNum.equals("")){
                        button.setVisibility(View.GONE);
                        imageView.setImageResource(R.drawable.card_person);
                    }
                    //Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });
    }

}