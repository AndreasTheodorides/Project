package com.unipi.atheodoridis.nfccardapp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
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
import com.unipi.atheodoridis.nfccardapp.SuccessFragment;
import com.unipi.atheodoridis.nfccardapp.R;
import com.unipi.atheodoridis.nfccardapp.ScanFragment;
import com.unipi.atheodoridis.nfccardapp.databinding.ActivityProfileBinding;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

public class HomeFragment extends DialogFragment implements NfcAdapter.CreateNdefMessageCallback{

    Button button;
    TextView textView;
    ImageView imageView;
    private FirebaseUser firebaseUser;
    private SuccessFragment successFragment;
    private ScanFragment scanFragment;
    FirebaseDatabase db;
    private ActivityProfileBinding binding;
    private NfcAdapter mNfcAdapter;
    String token ="", cardNum= "";
    private boolean isDialogDisplayed = false;
    public static final String TAG = HomeFragment.class.getSimpleName();
    private Boolean isSuccess = false;
    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe


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

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }

    public static String generateNewToken() {
        byte[] randomBytes = new byte[12];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);

    }

    boolean waitingNdefMessage = false;

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        if (!waitingNdefMessage) {
            token = generateNewToken();
            waitingNdefMessage = true;

            System.out.println("the token"+token);
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference myRef = db.getReference("users/" + userId);
            myRef.child("Token").setValue(token);
            NdefRecord ndefRecord = NdefRecord.createMime("text/plain", token.getBytes());
            NdefMessage ndefMessage = new NdefMessage(ndefRecord);
            return ndefMessage;
        }
        return null;
    }

    public void scanCard(){
        showScanFragment();
       mAdapter.setNdefPushMessageCallback(this, getActivity());
    }



    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getActivity().getIntent();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred
            String msg = new String(message.getRecords()[0].getPayload());
            if (msg.equals("Ok")){
                waitingNdefMessage = false;
                System.out.println("ola good");
                showSuccessFragment();
            }
        }
    }

    public void showSuccessFragment (){
        isSuccess = true;
        successFragment = (SuccessFragment) getActivity().getSupportFragmentManager().findFragmentByTag(SuccessFragment.TAG);
        if (successFragment == null){
            successFragment = SuccessFragment.newInstance();
        }
        successFragment.show(getActivity().getSupportFragmentManager(), SuccessFragment.TAG);
    }

    public void showScanFragment (){
        scanFragment = (ScanFragment) getActivity().getSupportFragmentManager().findFragmentByTag(ScanFragment.TAG);
        if (scanFragment == null){
            scanFragment = ScanFragment.newInstance();
        }
        scanFragment.show(getActivity().getSupportFragmentManager(),ScanFragment.TAG);
    }
}