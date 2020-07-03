package com.example.sudokulab;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HighScore extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference ref;
    ArrayList arr;
    ListView lv;
    User user;
    ArrayAdapter aa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        lv=findViewById(R.id.list);
        arr = new ArrayList();
        aa = new ArrayAdapter(this,R.layout.highscorelist,R.id.hstext,arr);

        db=FirebaseDatabase.getInstance();
        ref=db.getReference("User");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    user=ds.getValue(User.class);
                    arr.add(user.getName()+": "+user.getScore());
                }
                Collections.sort(arr, new Comparator<String>() {
                    public int compare(String a, String b) {
                        String sa = "", sb = "";
                        int i = 0;
                        for (; i < a.length(); i++) {
                            if (a.charAt(i) == ':')
                                break;
                        }
                        i += 2;
                        for (; i < a.length(); i++)
                            sa += a.charAt(i);

                        i = 0;
                        for (; i < b.length(); i++) {
                            if (b.charAt(i) == ':')
                                break;
                        }
                        i += 2;
                        for (; i < b.length(); i++)
                            sb += b.charAt(i);

                        return sb.compareTo(sa);
                    }
                });
                lv.setAdapter(aa); //IMPORTANT this needs to be here!!
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
