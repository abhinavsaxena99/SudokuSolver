package com.example.sudokulab;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        //play button
        Button play=(Button) findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),PlaySudoku.class);
                Random rand=new Random();
                intent.putExtra("rndno",rand.nextInt(5));
                startActivity(intent);
            }
        });

        //solve button
        Button solve=(Button) findViewById(R.id.solve);
        solve.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),SolveSudoku.class);
                startActivity(intent);
            }
        });

        //highscore button
        Button highscore=(Button) findViewById(R.id.highscrore);
        highscore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),HighScore.class);
                startActivity(intent);
            }
        });

        //how to play button
        Button howtoplay=(Button)findViewById(R.id.howtoplay);
        howtoplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),HowToPlay.class);
                startActivity(intent);
            }
        });


        //logout button
        ImageButton logout=(ImageButton)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent startIntent= new Intent(MainActivity.this, StartActivity.class);
                startActivity(startIntent);
                finish();
            }
        });


    }
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser==null){
            Intent startIntent=new Intent(MainActivity.this, StartActivity.class);
            startActivity(startIntent);
            finish();
        }

    }
}
