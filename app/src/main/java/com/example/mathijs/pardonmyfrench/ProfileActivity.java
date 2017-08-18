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
    private FirebaseUser mCurrentUser;
    private TextView mEmail;

    private FirebaseDatabase database;
    private DatabaseReference tblWords;

    private WordAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private ArrayList<Word> mWordList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mEmail = (TextView) findViewById(R.id.tv_profile_email);
        mEmail.setText(mCurrentUser.getEmail());

        // database
        database = FirebaseDatabase.getInstance();
        tblWords = database.getReference("words");
        tblWords.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Word word = dataSnapshot.getValue(Word.class);

                if (word.getBy().equals(mCurrentUser.getEmail())) {
                    mWordList.add(word);
                    mAdapter.notifyDataSetChanged();
                }
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

        // recycler view
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_words);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new WordAdapter(mWordList, this);

        mRecyclerView.setAdapter(mAdapter);


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
