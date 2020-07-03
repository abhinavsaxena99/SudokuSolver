package com.example.sudokulab;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SolveSudoku extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sudoku_solve);

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

        //clear button
        Button clear=(Button) findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        edit[i][j].setTextColor(Color.BLACK);
                        edit[i][j].setText("");
                    }
                }
            }
        });

        //solve button
        Button solve=(Button) findViewById(R.id.solve);
        solve.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int sudoku[][]=new int[9][9];
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (edit[i][j].getText().toString().equals(""))
                            sudoku[i][j]=0;
                        else
                            sudoku[i][j]=Integer.parseInt(edit[i][j].getText().toString());
                    }
                }


                int original[][]=new int[9][9];
                for(int i=0;i<9;i++)
                {
                    for(int j=0;j<9;j++)
                    {
                        original[i][j]=sudoku[i][j];
                    }
                }
                if(!checkcorrect(sudoku)) {
                    Toast.makeText(getApplicationContext(),"Sudoku is invalid",Toast.LENGTH_SHORT).show();
                }
                else if(!solveSudoku(sudoku)) {
                    Toast.makeText(getApplicationContext(),"Sudoku is invalid",Toast.LENGTH_SHORT).show();
                }
                else {

                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            edit[i][j].setText(""+sudoku[i][j]);
                            if(original[i][j]!=sudoku[i][j])
                            {
                                edit[i][j].setTextColor(Color.GRAY);
                            }
                        }
                    }

                }
            }
        });
    }
}
