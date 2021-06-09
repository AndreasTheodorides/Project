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

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.unipi.atheodoridis.nfccardapp.SuccessFragment;
import com.unipi.atheodoridis.nfccardapp.R;
import com.unipi.atheodoridis.nfccardapp.ScanFragment;
import com.unipi.atheodoridis.nfccardapp.databinding.ActivityProfileBinding;

public class HomeFragment extends DialogFragment implements NfcAdapter.CreateNdefMessageCallback{
//implements NfcAdapter.CreateNdefMessageCallback

    Button button;
    TextView textView;
    ImageView imageView;
    private FirebaseUser firebaseUser;
    private SuccessFragment successFragment;
    private ScanFragment scanFragment;
    FirebaseDatabase db;
    private ActivityProfileBinding binding;
    private NfcAdapter mNfcAdapter;
    String dbcardNum ="", cardNum= "";
    private boolean isDialogDisplayed = false;
    public static final String TAG = HomeFragment.class.getSimpleName();
    private Boolean isSuccess = false;

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
       // alertBoxCard();
        showScanFragment();
       mAdapter.setNdefPushMessageCallback(this, getActivity());

//        button.setOnClickListener(view -> new NdefFragment());
//        mNdefFragment = (NdefFragment) getActivity().getSupportFragmentManager().findFragmentByTag(NdefFragment.TAG);
//        if (mNdefFragment == null){
//            mNdefFragment = NdefFragment.newInstance();
//        }
//        mNdefFragment.show(getActivity().getSupportFragmentManager(),NdefFragment.TAG);
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
                System.out.println("ola good");
                showSuccessFragment();
               // alertBox();
            }
            //textView.setText(new String(message.getRecords()[0].getPayload()));

        } //else
            //textView.setText("Waiting for NDEF Message");
    }

//    public void alertBox(){
//        Dialog builder = new Dialog(getActivity());
//        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        builder.getWindow().setBackgroundDrawable(
//                new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                //nothing;
//            }
//        });
//
//        ImageView imageView = new ImageView(getActivity());
//        imageView.setImageResource(R.drawable.check);
//        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
//        builder.show();
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                builder.dismiss();
//            }
//        }, 3000);   //3 seconds
//    }
//
//    public void alertBoxCard(){
//        Dialog builder = new Dialog(getActivity());
//        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        builder.getWindow().setBackgroundDrawable(
//                new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                //nothing;
//            }
//        });
//
//        ImageView imageView = new ImageView(getActivity());
//        imageView.setImageResource(R.drawable.id_card__2_);
//        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT));
//        builder.show();
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                builder.dismiss();
//            }
//        }, 3000);
//    }

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
        if (successFragment == null){
            scanFragment = ScanFragment.newInstance();
        }
        scanFragment.show(getActivity().getSupportFragmentManager(),ScanFragment.TAG);
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