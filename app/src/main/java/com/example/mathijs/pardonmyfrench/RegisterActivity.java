package com.example.mathijs.pardonmyfrench;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;

    private EditText editEmail;
    private EditText editPassword;
    private Button btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {

            // TODO: do something else!
            // go directly to overview
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        editEmail = (EditText) findViewById(R.id.editEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    public void createAccount(String email, String password) {
        // validate email and password

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // TODO rewrite

                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Exception e = task.getException();
                            Log.w(TAG, "signInWithEmail:failed", e);
                            Toast.makeText(RegisterActivity.this, e.getMessage().toString(),
                                    Toast.LENGTH_SHORT).show();
                        }

                        if (task.isSuccessful()) {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view == btnRegister) {
            String email = editEmail.getText().toString();
            String password = editPassword.getText().toString();
            createAccount(email, password);
        }

        if (view == btnLogin) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
