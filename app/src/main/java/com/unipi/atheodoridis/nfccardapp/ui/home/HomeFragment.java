package com.unipi.atheodoridis.nfccardapp.ui.home;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.unipi.atheodoridis.nfccardapp.ProfileActivity;
import com.unipi.atheodoridis.nfccardapp.R;
import com.unipi.atheodoridis.nfccardapp.databinding.ActivityProfileBinding;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;

public class HomeFragment extends DialogFragment implements NfcAdapter.CreateNdefMessageCallback{

    private NfcAdapter adapter;
    Button button;
    TextView textView;
    ImageView imageView;
    private FirebaseUser firebaseUser;
    FirebaseDatabase db;
    private ActivityProfileBinding binding;
    private NfcAdapter mNfcAdapter;
    String dbcardNum ="", cardNum= "";
    private boolean isDialogDisplayed = false;
    public static final String TAG = HomeFragment.class.getSimpleName();

    public static HomeFragment newInstance() {

        return new HomeFragment();
    }



    private HomeViewModel homeViewModel;
    NfcAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        db = FirebaseDatabase.getInstance();
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        cardNum = sharedPref.getString("CardNumber","");
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        imageView = (ImageView) root.findViewById(R.id.imageView4);
        textView = (TextView) root.findViewById(R.id.textView2);
        button = (Button) root.findViewById(R.id.button5);
        mAdapter = NfcAdapter.getDefaultAdapter(getActivity());
        //checkCard();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanCard();
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

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String message = sharedPref.getString("CardNumber","");
        NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
        NdefMessage ndefMessage = new NdefMessage(ndefRecord);
        return ndefMessage;
    }

    public void scanCard(){
        mAdapter.setNdefPushMessageCallback(this, getActivity());
    }




//    public void checkCard(){
//        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
//        System.out.println(userId);
//        DatabaseReference ref = db.getReference("users").child(userId);
//        ref.child("CardNumber").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                DataSnapshot snapshot = task.getResult();
//                dbcardNum = snapshot.getValue().toString();
//                if (task.isSuccessful()) {
//                    if (dbcardNum.equals("")){
//                        button.setVisibility(View.GONE);
//                    }
//                    //Log.e("firebase", "Error getting data", task.getException());
//                }
//                else {
//                    if (!dbcardNum.equals("")){
//                        textView.setText("Scan your card to get access to the room");
//                        imageView.setImageResource(R.drawable.access);
//                        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sharedPref.edit();
//                        editor.putString("CardNumber", dbcardNum);
//                        editor.apply();
//                    }
//                    //Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                }
//            }
//        });
//    }
}