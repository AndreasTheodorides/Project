package com.unipi.atheodoridis.nfccardapp.ui.mycard;

import android.nfc.cardemulation.NfcFCardEmulation;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unipi.atheodoridis.nfccardapp.R;
import com.unipi.atheodoridis.nfccardapp.databinding.ActivityProfileBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyCardFragment extends Fragment {
    NfcFCardEmulation cardEmulation;
    Button button;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private ActivityProfileBinding binding;

    private MyCardViewModel myCardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myCardViewModel =
                new ViewModelProvider(this).get(MyCardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mycard, container, false);

        myCardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }
    public void generateCard(){
        final int min = 100000000;
        final int max = 999999999;
        final int random = new Random().nextInt((max - min) + 1) + min;

    }
}