package com.unipi.atheodoridis.nfccardapp.ui.home;

import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.unipi.atheodoridis.nfccardapp.R;
import com.unipi.atheodoridis.nfccardapp.databinding.ActivityProfileBinding;

public class HomeFragment extends Fragment {
    private NfcAdapter adapter;
    Button button;
    TextView textView;
    ImageView imageView;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase db;
    private ActivityProfileBinding binding;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        checkCard();

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        imageView = (ImageView) root.findViewById(R.id.imageView4);
        textView = (TextView) root.findViewById(R.id.textView2);
        button = (Button) root.findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                scanCard();
            }
        });
       // final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }
//    public void scanCard(){
//
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
//    }
    public void checkCard(){
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = db.getReference("users/" + userId);
        ref.child("CardNumber").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    String cardNum = String.valueOf(task.getResult().getValue());
                    if (cardNum.equals("")){
                        button.setVisibility(View.GONE);
                    }
                    //Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    String cardNum = String.valueOf(task.getResult().getValue());
                    if (!cardNum.equals("")){
                        textView.setText("Scan your card to get access to the room");
                        //button.setVisibility(View.GONE);
                        imageView.setImageResource(R.drawable.access);
                    }
                    //Log.d("firebase", String.valueOf(task.getResult().getValue()));
                }
            }
        });
    }
}