package com.example.mathijs.pardonmyfrench;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mathijs.pardonmyfrench.Objects.Word;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddWordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText wFrench;
    private EditText wDutch;
    private Button btnAddWord;

    private FirebaseUser currentUser;
    private DatabaseReference dbWords;
    private Boolean exists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        wFrench = (EditText) findViewById(R.id.wFrench);
        wDutch = (EditText) findViewById(R.id.wDutch);
        btnAddWord = (Button) findViewById(R.id.btnAddWord);
        btnAddWord.setOnClickListener(this);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void addWord() {
        String french = wFrench.getText().toString();
        String dutch = wDutch.getText().toString();
        String email = currentUser.getEmail();

        Word word = new Word();
        word.setBy(email);
        word.setFrench(french);
        word.setDutch(dutch);
        word.setVotes(0);

        dbWords = FirebaseDatabase.getInstance().getReference("words");

        checkIfWordExists(french);

        if (exists) {
            Toast.makeText(
                    getApplicationContext(),
                    "Word already exists",
                    Toast.LENGTH_SHORT)
                    .show();
        } else {
            dbWords.child(french).setValue(word);

            Toast.makeText(this, "Word has been added", Toast.LENGTH_SHORT).show();

            // TODO: add to child added on main activity?
            if (allowNotifations()) {
                sendNotification(word);
            }

            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

    }

    private void checkIfWordExists(String wordToCheck)
    {
        dbWords.child(wordToCheck).limitToFirst(1).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            wordExists();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    private void wordExists() {
        exists = true;
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

    @Override
    public void onClick(View view) {
        if (view == btnAddWord) {
            addWord();
        }
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
