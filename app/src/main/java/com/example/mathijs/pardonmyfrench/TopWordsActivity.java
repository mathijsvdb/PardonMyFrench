package com.example.mathijs.pardonmyfrench;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.mathijs.pardonmyfrench.Objects.Word;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

public class TopWordsActivity extends AppCompatActivity implements WordAdapter.ListItemClickListener {
    private ArrayList<Word> wordList;
    private RecyclerView mRecyclerView;
    private WordAdapter mAdapter;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_words);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("words");

        wordList = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_words);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new WordAdapter(wordList, this);
        mRecyclerView.setAdapter(mAdapter);

        reference.orderByChild("votes").limitToLast(200).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                wordList.add(dataSnapshot.getValue(Word.class));
                Collections.reverse(wordList);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Word word = dataSnapshot.getValue(Word.class);

                int index = getItemIndex(word);

                wordList.set(index, word);
                mAdapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Word word = dataSnapshot.getValue(Word.class);

                int index = getItemIndex(word);

                wordList.remove(index);
                mAdapter.notifyItemRemoved(index);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //
            }
        });
    }

    private int getItemIndex(Word word) {
        // get the index of the word that thas been removed
        int index = 0;

        for (int i = 0; i < wordList.size(); i++) {
            if (wordList.get(i).getFrench().equals(word.getFrench())) {
                index = i;
                break;
            }
        }

        return index;
    }

    @Override
    public void onListItemClick(Word word) {
        // Go to word detail page
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        intent.putExtra("word", word);

        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
