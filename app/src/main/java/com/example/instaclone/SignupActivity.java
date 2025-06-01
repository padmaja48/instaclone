//SignupActivity.java
package com.example.instaclone;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    EditText emailEt, passwordEt;
    Button signupBtn;
    TextView loginRedirectText;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Firebase
        mAuth = FirebaseAuth.getInstance();

        // Views
        emailEt = findViewById(R.id.email);
        passwordEt = findViewById(R.id.password);
        signupBtn = findViewById(R.id.signupBtn);
        loginRedirectText = findViewById(R.id.loginRedirectText); // 👈 This is the "Already have an account?" TextView

        // Signup button logic
        signupBtn.setOnClickListener(v -> {
            String email = emailEt.getText().toString().trim();
            String pass = passwordEt.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, UploadActivity.class));
                            finish();
                        } else {
                            Toast.makeText(this, "Signup Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Redirect to LoginActivity when user clicks "Already have an account?"
        loginRedirectText.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }
}
