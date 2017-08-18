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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements WordAdapter.ListItemClickListener{
    private FirebaseDatabase database;
    private DatabaseReference tblWords;
    private ArrayList<Word> wordList = new ArrayList<>();
    private FirebaseUser mUser;

    // recyclerview variables
    private RecyclerView mRecyclerView;
    private WordAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUser = FirebaseAuth.getInstance().getCurrentUser();

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
                word.setBy(mUser.getEmail());
                String votes = dataSnapshot.child("votes").getValue().toString();

                word.setVotes(Integer.valueOf(votes));
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

        // RecyclerView
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_words);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new WordAdapter(wordList, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onListItemClick(Word word) {
        // Go to word detail page
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);

        // TODO: kan toch veel cleaner, nee?
        intent.putExtra("word_french", word.getFrench());
        intent.putExtra("word_dutch", word.getDutch());
        intent.putExtra("word_by", word.getBy());
        intent.putExtra("word_votes", String.valueOf(word.getVotes()));

        startActivity(intent);
    }
}
