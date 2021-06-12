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
    EditText editText1,editText2,editText3,editText4,editText5;

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
        db = FirebaseDatabase.getInstance();
       // db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }
    public void signUp(View view){
        Intent intent = new Intent(this,ProfileActivity.class);
        String am = editText1.getText().toString();
        String fname = editText2.getText().toString();
        String lname = editText3.getText().toString();
        String email = editText4.getText().toString();
       // String email = editText4.toString();
        String Password = editText5.getText().toString();
        //dimiourgia neou xristi stin vasi firebase
        mAuth.createUserWithEmailAndPassword(email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            DatabaseReference myRef = db.getReference("users/" + userId);
                            myRef.child("AM").setValue(am);
                            myRef.child("FirstName").setValue(fname);
                            myRef.child("LastName").setValue(lname);
                            myRef.child("Email").setValue(email);
                            myRef.child("CardNumber").push().getKey();
//                            String cardNum = String.valueOf(key);
//                            myRef.child("CardNumber").setValue(cardNum);
//                            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPref.edit();
//                            editor.putString("CardNumber", cardNum);
//                            editor.apply();
                            startActivity(intent);
                            //db.collection("users").document(userId).set(userModel);



                            Toast.makeText(getApplicationContext(),"Sign in Success!",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            System.out.println(task.getException().getMessage());
                        }
                    }
                });
    }
}