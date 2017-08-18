package com.example.mathijs.pardonmyfrench;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.mathijs.pardonmyfrench.Objects.Word;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements WordAdapter.ListItemClickListener {
    // text to show on top
    private FirebaseUser mCurrentUser;
    private TextView mEmail;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private RecyclerView mRecyclerView;
    private WordAdapter mAdapter;

    private ArrayList<Word> mWordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Display Profile Information TODO: Add to it.
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mEmail = (TextView) findViewById(R.id.tv_profile_email);
        mEmail.setText(mCurrentUser.getEmail());

        // Initate database variables
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("words");

        // The wordlist
        mWordList = new ArrayList<>();

        // Set up the recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_words);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new WordAdapter(mWordList, this);
        mRecyclerView.setAdapter(mAdapter);

        // Retrieve data and populate wordlist
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                // add the words to the wordlist
                Word word = dataSnapshot.getValue(Word.class);

                // Only add the word to the list if it is added by the loggen in user
                if (word.getBy().equals(mCurrentUser.getEmail())) {
                    mWordList.add(word);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // TODO: update verkeerde items
//                Word word = dataSnapshot.getValue(Word.class);
//
//                int index = getItemIndex(word);
//
//                mWordList.set(index, word);
//                mAdapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // TODO: verwijdert verkeerde items
//                Word word = dataSnapshot.getValue(Word.class);
//
//                int index = getItemIndex(word);
//
//                mWordList.remove(index);
//                mAdapter.notifyItemRemoved(index);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private int getItemIndex(Word word) {
        int index = 0;

        for (int i = 0; i < mWordList.size(); i++) {
            if (mWordList.get(i).getFrench().equals(word.getFrench())) {
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
