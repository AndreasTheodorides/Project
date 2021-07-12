package com.unipi.atheodoridis.nfccardapp.ui.mycard;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.cardemulation.NfcFCardEmulation;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MyCardFragment extends Fragment {
    private NfcAdapter adapter;
    FloatingActionButton fab;
    Button button;
    ImageView imageView;
    TextView textView, am, fname, email, cardnum, uni, dep, year, exp_year;
    private FirebaseUser firebaseUser;
    FirebaseDatabase db;
    private ActivityProfileBinding binding;
    SharedPreferences preferences;
    String imageB64;

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
        exp_year = (TextView) root.findViewById(R.id.exp_year1);
        cardnum = (TextView) root.findViewById(R.id.cardnum1);
        fab = root.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(l -> {
            Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent, 6969);
        });
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
                    fname.setText(task.getResult().child("FullName").getValue().toString());
                    email.setText(task.getResult().child("Email").getValue().toString());
                    cardnum.setText(task.getResult().child("CardNumber").getValue().toString());
                    uni.setText(task.getResult().child("University").getValue().toString());
                    dep.setText(task.getResult().child("Department").getValue().toString());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
                    YearMonth date;
                    String dateStr = task.getResult().child("Registration Date").getValue(String.class);
                    date = YearMonth.parse(dateStr, formatter);
                    year.setText(date.toString());
                    exp_year.setText(date.plusYears(6).toString());
                    imageB64 = task.getResult().child("ProfileImage").getValue(String.class);
                    if (imageB64 != null) {
                        byte[] decodedString = Base64.decode(imageB64, Base64.URL_SAFE);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imageView.setImageBitmap(decodedByte);
                    }
                }
            }
        });
    }

    InputStream in;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { // Όταν γίνει επιλογή εικόνας από το τηλέφωνο
        super.onActivityResult( requestCode, resultCode, data);
        if (requestCode == 6969) {
            if(resultCode == Activity.RESULT_OK) {
                Uri contentURI = Uri.parse(data.getDataString());
                ContentResolver cr = getActivity().getContentResolver();
                try {
                    in = cr.openInputStream(contentURI);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize=16;
                Bitmap thumb = BitmapFactory.decodeStream(in,null,options);
                getImageData(thumb);
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference myRef = db.getReference("users/" + userId);
                if (imageB64 != null) {
                    myRef.child("ProfileImage").setValue(imageB64);
                }
                imageView.setImageBitmap(thumb);
            }
        }
    }

    public void getImageData(Bitmap bmp) {

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 30, bao); // bmp is bitmap from user image file
        byte[] byteArray = bao.toByteArray();
        imageB64 = Base64.encodeToString(byteArray, Base64.URL_SAFE);
        //  store & retrieve this string which is URL safe(can be used to store in FBDB) to firebase
        // Use either Realtime Database or Firestore
    }

}