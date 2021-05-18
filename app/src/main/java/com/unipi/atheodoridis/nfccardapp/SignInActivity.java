package com.unipi.atheodoridis.nfccardapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unipi.atheodoridis.nfccardapp.model.UserModel;

import org.w3c.dom.Document;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignInActivity extends AppCompatActivity {
    EditText editText1,editText2,editText3,editText4,editText5;
    FirebaseFirestore db;
    FirebaseDatabase database;
    private FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        editText5 = findViewById(R.id.editText5);
        database = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }
    public void signIn(View view){
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
                            startActivity(intent);

                            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            //System.out.println(email);
                            UserModel userModel= new UserModel(am, fname, lname, email, "");
                            DatabaseReference myRef = database.getReference("users/" + userId);
                            myRef.child("AM").setValue(am);
                            myRef.child("FirstName").setValue(fname);
                            myRef.child("LastName").setValue(lname);
                            myRef.child("Email").setValue(email);
                            myRef.child("CardNumber").setValue("");
                            db.collection("users").document(userId).set(userModel);



                            Toast.makeText(getApplicationContext(),"Sign in Success!",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            System.out.println(task.getException().getMessage());
                        }
                    }
                });
    }
}