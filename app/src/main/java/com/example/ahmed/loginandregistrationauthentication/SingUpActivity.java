package com.example.ahmed.loginandregistrationauthentication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SingUpActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editTextEmail, editTextPassword;
    Button buttonRegister;
    TextView textViewForget, textViewLogin;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        }
        initViews();
    }

    private void initViews() {
        editTextEmail = findViewById(R.id.edit_txt_email);
        editTextPassword = findViewById(R.id.edit_txt_password);
        buttonRegister = findViewById(R.id.sign_up_button);
        textViewForget = findViewById(R.id.tv_forget);
        textViewLogin = findViewById(R.id.tv_Register);

        buttonRegister.setOnClickListener(this);
        textViewForget.setOnClickListener(this);
        textViewLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up_button:
                createNewUser();
                break;
            case R.id.tv_forget:
                startActivity(new Intent(SingUpActivity.this, ResetPasswordActivity.class));
                break;
            case R.id.tv_Register:
                startActivity(new Intent(SingUpActivity.this, LoginActivity.class));
                Toast.makeText(this, "Login Activity", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void createNewUser() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Enter email address!");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("email_not_formatted");
        } else if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Enter password!");
        } else if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
        } else {
            Utilities.showLoadingDialog(this, Color.WHITE);
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SingUpActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                   // Toast.makeText(SingUpActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                    Utilities.dismissLoadingDialog();
                    if (!task.isSuccessful()) {
                        Toast.makeText(SingUpActivity.this, "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(SingUpActivity.this, "User has been Registed", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }
            });
        }

    }

}
