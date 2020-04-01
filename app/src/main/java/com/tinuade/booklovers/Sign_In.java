package com.tinuade.booklovers;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Sign_In extends AppCompatActivity {

    private EditText mEmail, mPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        //init firebase
        auth = FirebaseAuth.getInstance();

        // Check if user is already signed in
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, Book.class));
            finish();
        }

        //FindViewByID for widget
        mEmail = findViewById(R.id.sign_in_email);
        mPassword = findViewById(R.id.sign_in_password);
        TextView mSignUpLink = findViewById(R.id.signUpLink);
        Button button = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loadingProgressBar.setVisibility(View.INVISIBLE);

        mSignUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Sign_In.this, Sign_up.class);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mEmail.getText().toString()) && !Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString()).matches()) {
                    Toast.makeText(Sign_In.this, "Please Enter a valid Email Address", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(mPassword.getText().toString())) {
                    Toast.makeText(Sign_In.this, "Please Enter a valid Password", Toast.LENGTH_LONG).show();
                } else {
                    //Register User
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    auth.signInWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    startActivity(new Intent(Sign_In.this, Book.class));
                                    finish();
                                    loadingProgressBar.setVisibility(View.INVISIBLE);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Sign_In.this, "Authentication Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            })
                    ;
                }
            }
        });
    }
}
