package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    Button button;
    EditText Email,password;
    FirebaseAuth auth;
    String emailPattern="a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logbutton);
        Email = findViewById(R.id.editTextLogEmailAddress);
        password = findViewById(R.id.editTextLogPassword);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email.getText().toString();
                String pass = password.getText().toString();
                if ((TextUtils.isEmpty(email))){
                    Toast.makeText(login.this, "Enter the Email", Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(login.this, "Enter the Password", Toast.LENGTH_SHORT).show();

                } else if (!email.matches(emailPattern)) {
                    Email.setError("Give Proper Email Address ");

                } else if (password.length()<6) {
                       password.setError("More then Six Characters");
                    Toast.makeText(login.this, "Password Needs to be Longer then Six character", Toast.LENGTH_SHORT).show();
                }
//                else {
//
//                    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
                                try {
                                    Intent intent = new Intent(login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } catch (Exception e) {
                                    Toast.makeText(login.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                           }
/*
                            else {
                                Toast.makeText(login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
*/


                    });


                }
            }

