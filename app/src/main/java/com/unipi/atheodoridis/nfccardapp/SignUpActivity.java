package com.unipi.atheodoridis.nfccardapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    EditText editText1,editText2,editText3,editText4,editText5,editText6,editText7;

    FirebaseDatabase db;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    ImageView imageView;
    private static final int RC_SIGN_IN = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);
        editText6 = findViewById(R.id.editText6);
        editText7 = findViewById(R.id.editText7);
        db = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }
    public void signUp(View view){
        Intent intent = new Intent(this,ProfileActivity.class);
        String am = editText1.getText().toString();
        String fullname = editText2.getText().toString();
        String email = editText3.getText().toString();
        String Password = editText4.getText().toString();
        String uni = editText5.getText().toString();
        String dep = editText6.getText().toString();
        String date = editText7.getText().toString();
        //dimiourgia neou xristi stin vasi firebase
        mAuth.createUserWithEmailAndPassword(email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference myRef = db.getReference("users/" + userId);
                            myRef.child("AM").setValue(am);
                            myRef.child("FullName").setValue(fullname);
                            myRef.child("Email").setValue(email);
                            myRef.child("University").setValue(uni);
                            myRef.child("Department").setValue(dep);
                            myRef.child("Registration Date").setValue(date);
                            String key =  myRef.child("CardNumber").push().getKey();
                            myRef.child("CardNumber").setValue(key);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),"Sign up Success!",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            System.out.println(task.getException().getMessage());
                        }
                    }
                });
    }
}