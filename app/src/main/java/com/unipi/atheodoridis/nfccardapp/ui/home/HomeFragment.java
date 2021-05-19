package com.unipi.atheodoridis.nfccardapp.ui.home;

import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.google.firebase.database.FirebaseDatabase;
import com.unipi.atheodoridis.nfccardapp.R;
import com.unipi.atheodoridis.nfccardapp.databinding.ActivityProfileBinding;

public class HomeFragment extends Fragment {
    private NfcAdapter adapter;
    Button button;
    TextView textView;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase db;
    private ActivityProfileBinding binding;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
       // final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }
    public void scanCard(){

//        String num = String.valueOf(random);
//        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//        NdefMessage msg = null;
//        if ((rawMsgs != null) && (rawMsgs.length > 0)) {
//            msg = (NdefMessage)rawMsgs[0];
//        }
//        if (msg != null) {
//            // do something with the received message
//        }
////        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
////        nfcAdapter.setNdefPushMessage(num, this);
    }
}