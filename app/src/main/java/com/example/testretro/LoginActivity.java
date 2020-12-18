package com.example.testretro;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity
{
    EditText mEmail,mPassword;
    Button mLogin;
    TextView mReg;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail=findViewById(R.id.log_email);
        mPassword=findViewById(R.id.log_password);
        mLogin=findViewById(R.id.logbtn);
        mReg=findViewById(R.id.log_register);
        mAuth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progress);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mEmail.getText().toString().trim();
                String password=mPassword.getText().toString().trim();
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
                //authenticate
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {

                            Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        mReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }
}
