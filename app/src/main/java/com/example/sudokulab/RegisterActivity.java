package com.example.sudokulab;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//import android.support.annotation.NonNull;
//import android.support.design.widget.TextInputLayout;
//import android.support.v7.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText mDisplayName;
    private EditText mEmail;
    private EditText mPassword;
    private Button mCreateBtn;
    DatabaseReference reff;
    private ProgressDialog mRegProgress;
    EditText age;
    User user;

//    private Toolbar mToolBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        mRegProgress=new ProgressDialog(this);


//        mToolBar=(Toolbar)findViewById(R.id.register_toolbar);
//        setSupportActionBar(mToolBar);
//        getSupportActionBar().setTitle("Create Account");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mDisplayName=findViewById(R.id.reg_display_name);
        mEmail=findViewById(R.id.reg_email);
        mPassword=findViewById(R.id.reg_password);
        mCreateBtn=findViewById(R.id.reg_create_btn);
        age=findViewById(R.id.age);
        reff= FirebaseDatabase.getInstance().getReference().child("User");
        mCreateBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String display_name=mDisplayName.getText().toString();
                String email=mEmail.getText().toString();
                String password=mPassword.getText().toString();
                String ageint=age.getText().toString();

                if(!TextUtils.isEmpty(display_name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password )){

                    mRegProgress.setTitle("Creating Account");
                    mRegProgress.setMessage("Please wait...");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    register_user(display_name,email,password,ageint);

                }

            }
        });
    }

    private void register_user(final String display_name, final String email, String password,final String age) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            user = new User();
                            user.name=display_name;
                            user.age=age;
                            user.email=email;
                            user.score=0;

                            FirebaseUser fb=FirebaseAuth.getInstance().getCurrentUser();
                            String id = fb.getUid();
                            System.out.println(id);
                            reff.child(id).setValue(user); //IMPORTANT I ADDED THIS!! make sure fb database is in dependencies

                            mRegProgress.dismiss();

                            Intent mainIntent=new Intent(RegisterActivity.this,MainActivity.class);
                            startActivity(mainIntent);
                            finish();

                        } else {

                            mRegProgress.hide();

                            Toast.makeText(RegisterActivity.this,"Cannot sign in. Please check the form and try again.",Toast.LENGTH_LONG).show();
                        }

                        // ...
                    }
                });


    }
}
