package com.example.parkwayz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//Goals: Login user to app and go to search page.
//if login successful go to search page else go to registration page



public class LoginActivity extends AppCompatActivity {


    // private variables to be used
    private EditText userName, userPass;
    private Button loginBtn, registerBtn;
    private ProgressBar loginProg;
    private FirebaseAuth mAuth;
    private Intent HomeActivity;
    private Intent RegistrationActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        // setting value to variables by grabbing from the front-end of the login page
        // by using findViewById();
        userName = findViewById(R.id.regEmail);
        userPass = findViewById(R.id.regPassword);
        loginBtn = findViewById(R.id.RegBtn);
        loginProg = findViewById(R.id.RegProgressBar);
        registerBtn = findViewById(R.id.newUser);
        mAuth = FirebaseAuth.getInstance();
        HomeActivity = new Intent(this, com.example.parkwayz.HomeActivity.class);
        RegistrationActivity = new Intent(this, com.example.parkwayz.RegistrationActivity.class);

        loginProg.setVisibility(View.INVISIBLE);

        // listens for when the loginBtn is clicked
        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


                // stores the username (taken from the login page), converts to string, and stores it in "user"
                final String user = userName.getText().toString();
                // stores the password (taken from the login page), converts to string, and stores it in "password"
                final String password = userPass.getText().toString();

                if (user.isEmpty() || password.isEmpty()) {
                    showMessage("Please fill out all fields");
                }
                else {

                    signIn(user, password);
                }


            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startActivity(RegistrationActivity);    // redirects new user to the registration page
                finish();
            }
        });


    }

    // attempts to login with the given user and password
    private void signIn(String user, String password) {
        mAuth.signInWithEmailAndPassword(user, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                   loginProg.setVisibility(View.INVISIBLE);
                   loginBtn.setVisibility(View.VISIBLE);
                   updateUI();
                }
                else {
                    showMessage(task.getException().getMessage());
                }
            }
        });
    }

    // function to change UI (switch screens)
    private void updateUI() {
        startActivity(HomeActivity);
        finish();
    }

    // displays an error message when logging in
    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            // user is already connected so we need to redirect to the home page
            updateUI();

        }
    }
}


