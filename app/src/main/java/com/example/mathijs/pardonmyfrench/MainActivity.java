package com.example.mathijs.pardonmyfrench;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.mathijs.pardonmyfrench.Objects.Word;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements WordAdapter.ListItemClickListener {
    private ArrayList<Word> wordList;
    private RecyclerView mRecyclerView;
    private WordAdapter mAdapter;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check if registered.
        Intent intentStarted = getIntent();
        if (intentStarted.hasExtra("register_success")) {
            Toast.makeText(this, intentStarted.getStringExtra("register_success"), Toast.LENGTH_SHORT).show();
        }

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("words");

        wordList = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_words);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new WordAdapter(wordList, this);
        mRecyclerView.setAdapter(mAdapter);

        reference.orderByChild("french").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Word word = dataSnapshot.getValue(Word.class);
                wordList.add(word);
                mAdapter.notifyDataSetChanged();

                if (allowNotifations()) {
                    sendNotification(word);
                }
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

    private boolean allowNotifations() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getBoolean("allow_notifications", true);
    }

    private void sendNotification(Word word) {
        Log.i("NOTIFICATION", "sendNotification: true");

        // notification builder
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_add_black_24dp)
                .setContentTitle("New word added")
                .setContentText("The word '" + word.getFrench() + "' was added.");

        Intent resultIntent = new Intent(this, DetailActivity.class);
        resultIntent.putExtra("word", word);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
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
}
