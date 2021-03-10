package com.unipi.atheodoridis.nfccardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class SignInActivity extends AppCompatActivity {
    EditText editText1,editText2,editText3,editText4,editText5;
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
    }
    public void signIn(View view){
        Intent intent = new Intent(this,ProfileActivity.class);
        startActivity(intent);
    }
}