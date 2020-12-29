package com.example.testretro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText mName,mEmail,mPassword,mPhone;
    Button mRegister;
    public String email,password;
    TextView mLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mName=findViewById(R.id.reg_name);
        mEmail=findViewById(R.id.reg_email);
        mPassword=findViewById(R.id.reg_password);
        mPhone=findViewById(R.id.reg_ph);
        mRegister=findViewById(R.id.regbtn);
        mLogin=findViewById(R.id.reg_login);
        mAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progress);
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        if(mAuth.getCurrentUser()!=null)
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 email=mEmail.getText().toString().trim();
                 password=mPassword.getText().toString().trim();
                if(TextUtils.isEmpty(email))
                {
                    mEmail.setError("Email is req");
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    mPassword.setError("Password is req");
                    return;
                }
                if(password.length()<6)
                {
                    mPassword.setError("Min 6 char password");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                             if(task.isSuccessful())
                             {
                                 //FirebaseUser user=mAuth.getCurrentUser();
                                 final String current_user_id=mAuth.getCurrentUser().getUid();
                                 final DatabaseReference UserRef= FirebaseDatabase.getInstance().getReference().child("Users");
                                 UserRef.addValueEventListener(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                         if(!dataSnapshot.hasChild(current_user_id))
                                         {
                                             String id =mAuth.getCurrentUser().getUid().toString();
                                             String Mail=email;
                                             String Phone=mPhone.getText().toString().trim();
                                             String Name=mName.getText().toString().trim();
                                             HashMap user1=new HashMap();
                                             user1.put("name",Name);
                                             user1.put("email",Mail);
                                             user1.put("id",id);
                                             user1.put("phone",Phone);
                                             user1.put("recent","aa");
                                             UserRef.child(id).updateChildren(user1).addOnCompleteListener(new OnCompleteListener() {
                                                 @Override
                                                 public void onComplete(@NonNull Task task) {
                                                 if(task.isSuccessful())
                                                 {
                                                     Toast.makeText(RegisterActivity.this, "Details added", Toast.LENGTH_SHORT).show();
                                                 }
                                                 }
                                             });
                                         }
                                     }

                                     @Override
                                     public void onCancelled(@NonNull DatabaseError databaseError) {

                                     }
                                 });
                                 Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                                 startActivity(new Intent(getApplicationContext(),MainActivity.class));
                             }
                             else
                             {
                                 progressBar.setVisibility(View.GONE);
                                 Toast.makeText(RegisterActivity.this, "Error "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                             }
                    }
                });


            }
        });
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });
    }
}