package com.example.mathijs.pardonmyfrench;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.mathijs.pardonmyfrench.Objects.Word;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, WordAdapter.ListItemClickListener{

    private Button btnAddWord;
    private FirebaseDatabase database;
    private DatabaseReference tblWords;
    private ArrayList<Word> wordList = new ArrayList<>();;

    // recyclerview variables
    private RecyclerView mRecyclerView;
    private WordAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddWord = (Button) findViewById(R.id.btnAddWord);
        btnAddWord.setOnClickListener(this);

        // database shizzle
        database = FirebaseDatabase.getInstance();
        tblWords = database.getReference("words");

        tblWords.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("WOLLAG", "onChildAdded: " + dataSnapshot.child("french").getValue());

                // add items to the array list wordList
                Word word = new Word();
                word.setFrench(dataSnapshot.child("french").getValue().toString());
                word.setDutch(dataSnapshot.child("dutch").getValue().toString());
                word.setBy("blakkie");
                wordList.add(word);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_words);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new WordAdapter(wordList, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view == btnAddWord) {
            startActivity(new Intent(getApplicationContext(), AddWordActivity.class));
        }
    }

    @Override
    public void onListItemClick(Word word) {
        // Go to word detail page
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);


    }
}
