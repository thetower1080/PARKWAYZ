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
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegistrationActivity extends AppCompatActivity {
    // private variables to be used
    private EditText userName, userPass, userEmail, userPass2;
    private ProgressBar loadingProg;
    private Button regBtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // setting value to variables by grabbing from the front-end of the login page
        // by using findViewById();
        userEmail = findViewById(R.id.regEmail);
        userPass = findViewById(R.id.regPassword);
        userPass2 = findViewById(R.id.regPasswordConfirm);
        userName = findViewById(R.id.regName);
        loadingProg = findViewById(R.id.RegProgressBar);
        regBtn = findViewById(R.id.RegBtn);

        loadingProg.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();

        regBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                regBtn.setVisibility(View.INVISIBLE);
                loadingProg.setVisibility(View.VISIBLE);
                final String email = userEmail.getText().toString();
                final String password = userPass.getText().toString();
                final String password2 = userPass2.getText().toString();
                final String name = userName.getText().toString();

                // display error message if not all fields are filled out the passwords do not match
                if (email.isEmpty() || name.isEmpty() || password.isEmpty() || password2.isEmpty() || !password.equals(password2))  {
                    showMessage("Please fill out all fields!");
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProg.setVisibility(View.INVISIBLE);
                }
                else {
                    // create a new user in Firebase
                    CreateUserAccount(email, password);
                }
            }
        });
    }

    // function to create a new user in Firebase
    private void CreateUserAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // user account successfully created
                    showMessage("Account created.");
                    updateUserInfo(userName.toString(), mAuth.getCurrentUser());
                }
                else {
                    showMessage("Failed to create account." + task.getException().getMessage());
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProg.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void updateUserInfo(String name, FirebaseUser currentUser){
        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // successfully updated user info
                    showMessage("Account registered!");
                    updateUI();
                }
            }
        });
    }

    private void updateUI() {
        Intent HomeActivity = new Intent(getApplicationContext(), com.example.parkwayz.HomeActivity.class);
        startActivity(HomeActivity);
        finish();
    }

    // simple method to toast (display) message
    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

}