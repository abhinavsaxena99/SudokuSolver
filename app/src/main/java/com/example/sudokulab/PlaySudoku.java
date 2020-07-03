package com.example.sudokulab;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

public class PlaySudoku extends AppCompatActivity {

    int rndno=0;
    String name;
    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    Handler handler;
    int Seconds, Minutes, MilliSeconds ;
    TextView timescore;
    FirebaseUser user;
    DatabaseReference reff;
    private FirebaseAuth mAuth;
    String uid;
    Button solve;
    long prevscore;

    public Runnable runnable = new Runnable() {
        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            MilliSeconds = (int) (UpdateTime % 1000);
            timescore.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));
            handler.postDelayed(this, 0);
        }

    };

    protected boolean checkcorrect(int board[][]) {
        List<Set<Integer>> row=new ArrayList<>(),col=new ArrayList<>(),box=new ArrayList<>();
        for(int i=0;i<9;i++) {

            row.add(new HashSet<Integer>());
            col.add(new HashSet<Integer>());
            box.add(new HashSet<Integer>());

        }
        for(int x=0;x<9;x++)
        {
            for(int y=0;y<9;y++)
            {
                if(board[x][y]!=0)
                {
                    if (row.get(x).contains(board[x][y]) || col.get(y).contains(board[x][y]) || box.get(boxnumber(x, y)).contains(board[x][y]))
                        return false;
                    row.get(x).add(board[x][y]);
                    col.get(y).add(board[x][y]);
                    box.get(boxnumber(x,y)).add(board[x][y]);
                }
            }
        }
        return true;
    }

    protected int boxnumber(int x,int y)
    {
        if(x<3&&y<3)
            return 0;
        if(x<3&&y>=3&&y<6)
            return 1;
        if(x<3&&y>=6)
            return 2;
        if(x>=3&&x<6&&y<3)
            return 3;
        if(x>=3&&x<6&&y>=3&&y<6)
            return 4;
        if(x>=3&&x<6&&y>=6)
            return 5;
        if(x>=6&&y<3)
            return 6;
        if(x>=6&&y>=3&&y<6)
            return 7;
        if(x>=6&&y>=6)
            return 8;
        else return 9;
    }

    protected boolean solveit(int x,int y, int board[][], List<Set<Integer>> row,List<Set<Integer>> col, List<Set<Integer>> box)
    {
        if(x==9&y==0)    //comes after 8,8
        {
            //Solution Found!
            //Need a way to get out of the stack. Whenever a subroutine call returns true, it means exit the stack.
            System.out.println("SOLVED!!!");
            return true;
        }
        if(board[x][y]!=0)
        {
            boolean temp=false;
            if(y<8)
                temp=solveit(x,y+1,board,row,col,box);
            else
                temp=solveit(x+1,0,board,row,col,box);
            if(temp)
                return temp;        //subroutine returns true => exit the stack
        }
        else {
            for (int i = 1; i < 10; i++)    //IMPORTANT 1to 9
            {
                if (!row.get(x).contains(i) && !col.get(y).contains(i) && !box.get(boxnumber(x, y)).contains(i)) {
                    row.get(x).add(i);
                    col.get(y).add(i);
                    box.get(boxnumber(x, y)).add(i);
                    board[x][y] = i;

                    boolean temp = false;
                    if (y < 8)
                        temp = solveit(x, y + 1, board, row, col, box);
                    else
                        temp = solveit(x + 1, 0, board, row, col, box);
                    if (temp)
                        return temp;

                    row.get(x).remove(i);
                    col.get(y).remove(i);
                    box.get(boxnumber(x, y)).remove(i);
                    board[x][y] = 0;
                }
            }
            return false;    //if no solution is found (no way to go forward)
        }
        return false;
    }

    protected boolean solveSudoku(int [][] board) {
        List<Set<Integer>> row=new ArrayList<>(),col=new ArrayList<>(),box=new ArrayList<>();
        System.out.println("Okay");
        for(int i=0;i<9;i++) {

            row.add(new HashSet<Integer>());
            col.add(new HashSet<Integer>());
            box.add(new HashSet<Integer>());

        }
        for(int x=0;x<9;x++)
        {
            for(int y=0;y<9;y++)
            {
                if(board[x][y]!=0)
                {
                    row.get(x).add(board[x][y]);
                    col.get(y).add(board[x][y]);
                    box.get(boxnumber(x,y)).add(board[x][y]);
                }
            }
        }
        System.out.println("Done till now!");
        return solveit(0,0,board,row,col,box);
    }

    // currently hard coded, can change later
    int samples[][][]= {
            {
                    {6, 1, 8, 5, 0, 3, 4, 2, 0},
                    {0, 5, 0, 0, 1, 7, 0, 6, 3},
                    {4, 7, 3, 2, 6, 0, 0, 9, 5},
                    {8, 0, 1, 0, 5, 0, 7, 4, 2},
                    {5, 0, 0, 6, 0, 1, 3, 0, 9},
                    {0, 9, 7, 8, 2, 0, 5, 0, 6},
                    {2, 8, 6, 0, 4, 5, 0, 3, 0},
                    {1, 0, 5, 9, 0, 2, 6, 0, 4},
                    {0, 4, 0, 1, 0, 6, 2, 5, 8}
            },
            {
                    {5,3,0,0,7,0,0,0,0},
                    {6,0,0,1,9,5,0,0,0},
                    {0,9,8,0,0,0,0,6,0},
                    {8,0,0,0,6,0,0,0,3},
                    {4,0,0,8,0,3,0,0,1},
                    {7,0,0,0,2,0,0,0,6},
                    {0,6,0,0,0,0,2,8,0},
                    {0,0,0,4,1,9,0,0,5},
                    {0,0,0,0,8,0,0,7,9}
            },
            {
                    {8,2,7,1,5,4,3,9,6},
                    {9,6,5,3,2,7,1,4,8},
                    {0,4,1,6,8,9,7,5,2},
                    {5,9,3,4,6,8,2,7,1},
                    {4,7,2,5,1,3,6,8,9},
                    {6,1,8,9,7,2,4,3,5},
                    {7,8,6,2,3,5,9,1,4},
                    {1,5,4,7,9,6,8,2,3},
                    {0,3,9,8,4,1,5,6,7}
            },
            {
                    {0,0,0,0,0,4,0,9,0},
                    {8,0,2,9,7,0,0,0,0},
                    {9,0,1,2,0,0,3,0,0},
                    {0,0,0,0,4,9,1,5,7},
                    {0,1,3,0,5,0,9,2,0},
                    {5,7,9,1,2,0,0,0,0},
                    {0,0,7,0,0,2,6,0,3},
                    {0,0,0,0,3,8,2,0,5},
                    {0,2,0,5,0,0,0,0,0}
            },
            {
                    {0,3,7,4,8,1,6,0,9},
                    {0,9,0,0,2,7,0,3,8},
                    {8,0,0,3,0,9,0,0,0},
                    {0,1,9,8,7,3,0,6,0},
                    {7,8,0,0,0,2,0,9,3},
                    {0,0,0,9,0,4,8,7,0},
                    {0,0,0,2,9,5,0,8,6},
                    {0,0,8,1,3,6,9,0,0},
                    {9,6,2,7,0,0,3,1,5}
            },
    };
    int sample[][];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sudoku_play);

        mAuth = FirebaseAuth.getInstance();
        reff= FirebaseDatabase.getInstance().getReference();

        handler=new Handler();
        StartTime= SystemClock.uptimeMillis();
        handler.postDelayed(runnable,1000);

        timescore=(TextView)findViewById(R.id.time);

        Intent in=getIntent();
        Bundle b = in.getExtras();

        if(b!=null)
        {
            rndno = (int)b.get("rndno");
        }

        sample=samples[rndno];

        //making 2d array of edit text
        final EditText edit[][]=new EditText[9][9];
        int l=0;
        int m=0;
        GridLayout GL = findViewById(R.id.grid);
        for(int i = 0; i < GL.getChildCount(); i++) {
            View v = GL.getChildAt(i);
            if (v instanceof EditText) {
                edit[l][m] = ((EditText) v);
                m+=1;
                if(m>8)
                {
                    m=0;
                    l+=1;
                }
            }
        }


        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(sample[i][j]!=0) {
                    edit[i][j].setText("" + sample[i][j]);
                    edit[i][j].setFocusable(false); //User can't change them
                }
                    else {
                    edit[i][j].setText("");
                    edit[i][j].setTextColor(Color.GRAY);
                }
            }
        }

        //clear button
        Button clear=(Button) findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                            if(sample[i][j]==0) //if it was part of the question
                                edit[i][j].setText("");
                    }
                }
                StartTime=SystemClock.uptimeMillis();
                solve.setEnabled(true);
                handler.postDelayed(runnable,1000);
            }
        });

        //check button
        solve=(Button) findViewById(R.id.check);
        solve.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean isFilled=true;

                int sudoku[][]=new int[9][9];
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (edit[i][j].getText().toString().equals("")) {
                            sudoku[i][j] = 0;
                            isFilled=false;
                        }
                        else
                            sudoku[i][j]=Integer.parseInt(edit[i][j].getText().toString());
                    }
                }
                System.out.println(isFilled);

                int original[][]=new int[9][9];
                for(int i=0;i<9;i++)
                {
                    for(int j=0;j<9;j++)
                    {
                        original[i][j]=sudoku[i][j];
                    }
                }
                if(!isFilled) {
                    Toast.makeText(getApplicationContext(),"Please fill all the blank boxes",Toast.LENGTH_SHORT).show();
                }
                else if(!checkcorrect(sudoku)) {
                    Toast.makeText(getApplicationContext(),"Sudoku is invalid",Toast.LENGTH_SHORT).show();
                }
                else if(!solveSudoku(sudoku)) {
                    Toast.makeText(getApplicationContext(),"Sudoku is invalid",Toast.LENGTH_SHORT).show();
                }
                else {
                    long totaltime=SystemClock.uptimeMillis()-StartTime;
                    long sec=(long)(totaltime/1000);
                    Toast.makeText(getApplicationContext(),"You Win! Good Job!",Toast.LENGTH_LONG).show();
                    handler.removeCallbacks(runnable);
                    final long curscore=10000-sec*sec;


                    // update highscore
                    user= FirebaseAuth.getInstance().getCurrentUser();
                    StartTime= SystemClock.uptimeMillis();
                    uid=user.getUid();
                    System.out.println(uid);

                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            prevscore=dataSnapshot.child("User").child(uid).child("score").getValue(Long.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(),"Network Error",Toast.LENGTH_LONG);
                        }
                    });

                    if(prevscore<curscore)
                    {
                        Map<String,Object> taskMap = new HashMap<String,Object>();
                        taskMap.put("score", curscore);
                        reff.child("User").child(uid).updateChildren(taskMap);
                    }
                    solve.setEnabled(false);

                }
            }
        });

        Button newboard=(Button)findViewById(R.id.newboard);
        newboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = getIntent();
                intent.putExtra("rndno",(rndno+1)%5);
                finish();   //so if i press back the prev activity doesnt open (here, the prev act is the previous instance of the same activity)
                startActivity(intent);
            }
        });

    }
}
