package com.tinuade.booklovers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tinuade.africaknow.Model.User;
import com.tinuade.africaknow.R;

public class Sign_Up extends AppCompatActivity {
    DatabaseReference users;
    //widgets
    private EditText mFullname, mEmailAddress, mPhonenumber, mPassword, mConfirmPassword;
    private ProgressBar loadingProgressBar;
    //Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        FirebaseDatabase mFirebaseDatabase;


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        users = mFirebaseDatabase.getReference("users");


        //findViewById for widgets
        loadingProgressBar = findViewById(R.id.loading);
        mFullname = findViewById(R.id.fullname);
        mEmailAddress = findViewById(R.id.sign_Up_email);
        mPhonenumber = findViewById(R.id.phoneNumber);
        mPassword = findViewById(R.id.password);
        mConfirmPassword = findViewById(R.id.confirm_password);
        TextView mSignInLink = findViewById(R.id.signInLink);
        Button mSignUpButton = findViewById(R.id.signup);
        loadingProgressBar.setVisibility(View.INVISIBLE);

        mSignInLink.setOnClickListener(v -> {

            Intent intent = new Intent(Sign_Up.this, Sign_In.class);
            startActivity(intent);
        });
        mSignUpButton.setOnClickListener(v -> signUpButton());

    }

    private void signUpButton() {
        if (TextUtils.isEmpty(mFullname.getText().toString()) && mFullname.getText().toString().length() < 3) {
            Toast.makeText(Sign_Up.this, "Please Enter a valid Name", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(mEmailAddress.getText().toString()) && !Patterns.EMAIL_ADDRESS.matcher(mEmailAddress.getText().toString()).matches()) {
            Toast.makeText(Sign_Up.this, "Please Enter a valid Email Address", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(mPassword.getText().toString())) {
            Toast.makeText(Sign_Up.this, "Please Enter a valid Password", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(mPassword.getText().toString()) && mEmailAddress.getText().toString().isEmpty()) {
            Toast.makeText(Sign_Up.this, "Please Enter a valid Password and Email Address", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(mConfirmPassword.getText().toString()) && !mEmailAddress.getText().toString().contains("@")) {
            Toast.makeText(Sign_Up.this, "Please Enter a valid Password", Toast.LENGTH_LONG).show();
        } else if (!mPassword.getText().toString().equalsIgnoreCase(mConfirmPassword.getText().toString())) {
            Toast.makeText(Sign_Up.this, "Passwords do not match", Toast.LENGTH_LONG).show();
            mPassword.setText("");
            mConfirmPassword.setText("");
        } else if (TextUtils.isEmpty(mPhonenumber.getText().toString()) && mPhonenumber.getText().toString().length() < 9) {
            Toast.makeText(Sign_Up.this, "Please Enter a valid Phone Number", Toast.LENGTH_LONG).show();
        } else
            //Register User


            mAuth.createUserWithEmailAndPassword(mEmailAddress.getText().toString(), mPassword.getText().toString())
                    .addOnSuccessListener(authResult -> {
                        //save user to the database
                        User user = new User();
                        user.setFullName(mFullname.getText().toString());
                        user.setEmail(mEmailAddress.getText().toString());
                        user.setPhone(mPhonenumber.getText().toString());

                        loadingProgressBar.setVisibility(View.VISIBLE);

                        //use phone as key

                        users.child(mAuth.getCurrentUser().getUid())
                                .setValue(user)
                                .addOnSuccessListener(aVoid -> Toast.makeText(Sign_Up.this, "Registration Successful", Toast.LENGTH_LONG).show())
                                .addOnFailureListener(e -> Toast.makeText(Sign_Up.this, "Registration Failed", Toast.LENGTH_LONG).show());
                        loadingProgressBar.setVisibility(View.INVISIBLE);

                        startActivity(new Intent(Sign_Up.this, Book.class));
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(Sign_Up.this, "Authentication Failed: " + e.getMessage(), Toast.LENGTH_LONG).show());

    }
}
