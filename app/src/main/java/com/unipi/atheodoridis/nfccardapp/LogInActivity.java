package com.unipi.atheodoridis.nfccardapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LogInActivity extends AppCompatActivity {
EditText editText1,editText2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
    }
    public void Login(View view){
        Intent intent = new Intent(this,ProfileActivity.class);
        startActivity(intent);
    }

    public void Signin(View view){
        Intent intent = new Intent(this,SignInActivity.class);
        startActivity(intent);
    }
}