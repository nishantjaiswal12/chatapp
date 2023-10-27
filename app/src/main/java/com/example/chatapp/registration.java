package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class registration extends AppCompatActivity {
    TextView loginbut;
    EditText rg_username,rg_email,rg_password,rg_repassword;
    Button rg_signup;

    CircleImageView  rg_profileImg;
    FirebaseAuth auth;
    Uri imageURI;
    String  imageuri;
    String emailPattern = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";
    FirebaseDatabase  database;
    FirebaseStorage storage;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);
        database= FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();


        auth = FirebaseAuth.getInstance();
        rg_username = findViewById(R.id.rgusername);
        rg_email =findViewById(R.id.rgemail);
        rg_password =findViewById(R.id.rgpassword);
        rg_repassword = findViewById(R.id.rgrepassword);
        rg_profileImg = findViewById(R.id.profilerg0);
        rg_signup = findViewById(R.id.signupbutton);





    rg_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String namee = rg_username.getText().toString();
                String emaill = rg_email.getText().toString();
                String Password= rg_password.getText().toString();
                String Cpassword= rg_repassword.getText().toString();
                String status = "Hey I'm Using This Application ";

                if(TextUtils.isEmpty(namee)||TextUtils.isEmpty(emaill)||TextUtils.isEmpty(Password)||TextUtils.isEmpty(Cpassword)) {

                    Toast.makeText(registration.this, "Please Enter valid Information", Toast.LENGTH_SHORT).show();
                } else if (!emaill.matches(emailPattern)) {
                    rg_email.setError("Type A Valid Eamil Here");
                    
                } else if (Password.length()<=7) {
                    rg_password.setError("Password Must Be 6 Characters");
                    
                } else if (!Password.equals(Cpassword)) {
                    rg_password.setError("The Password Doesn't Match ");
                    
                }else {
                    auth.createUserWithEmailAndPassword(emaill,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                String id = task.getResult().getUser().getUid();
                                DatabaseReference  reference = database.getReference().child("user").child(id);
                                StorageReference storageReference= storage.getReference().child("Upload").child(id);
                                if(imageURI!= null){
                                    storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageuri= uri.toString();
                                                        Users users = new Users(id, namee,emaill,Password,Cpassword,imageuri);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    Intent intent = new Intent(registration.this,login.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }else{
                                                                    Toast.makeText(registration.this, "Error in Creating User", Toast.LENGTH_SHORT).show();
                                                                }

                                                            }
                                                        });

                                                    }
                                                });
                                            }


                                        }
                                    });
                                }else {
                                    String status = "Hey I'm Using This Application";
                                    imageuri= "https://firebasestorage.googleapis.com/v0/b/chat-app-294b6.appspot.com/o/man.png?alt=media&token=a5aa5cce-6c09-4543-abb1-9c6a3a7253d6";
                                    Users users = new Users(id,namee,emaill,Password,imageuri,status);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Intent intent = new Intent(registration.this,MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }else{
                                                Toast.makeText(registration.this, "Error in Creating User", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });

                                }

                            }else {
                                Toast.makeText(registration.this,  task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }



                        }
                    });
                }

            }
        });





        rg_profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(data!=null){
                imageURI = data.getData();
                rg_profileImg.setImageURI(imageURI);
            }

        }
    }
}