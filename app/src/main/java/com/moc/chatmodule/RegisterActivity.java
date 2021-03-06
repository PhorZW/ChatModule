package com.moc.chatmodule;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout username, email, password;
    TextInputEditText inputUsername,inputEmail,inputPassword;
    Button btnAddMR;

    FirebaseAuth auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolBar = findViewById(R.id.toolBar_reg);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = (TextInputLayout) findViewById(R.id.username);
        password = (TextInputLayout) findViewById(R.id.password);
        email = (TextInputLayout) findViewById(R.id.email);
        inputUsername = (TextInputEditText) findViewById(R.id.inputUsername);
        inputEmail = (TextInputEditText) findViewById(R.id.inputEmail);
        inputPassword = (TextInputEditText) findViewById(R.id.inputPassword);

        btnAddMR = findViewById(R.id.btnAddMR);

        auth = FirebaseAuth.getInstance();

        btnAddMR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username = inputUsername.getText().toString();
                String txt_email = inputEmail.getText().toString();
                String txt_password = inputPassword.getText().toString();
                //String txt_ = username.getText().toString();

                if (TextUtils.isEmpty(txt_username)  || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {

                    Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else if (txt_password.length() < 8) {

                    Toast.makeText(RegisterActivity.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
                }
                else {

                    addMR(txt_username,txt_email,txt_password);
                }

            }
        });
    }

    private void addMR(String username, String email, String password){

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String readerid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Meter Readers").child(readerid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", readerid);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                        Intent intent = new Intent(RegisterActivity.this, StartActivity.class );
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });


                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Invalid email address or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}