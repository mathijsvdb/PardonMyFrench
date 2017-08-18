package com.example.mathijs.pardonmyfrench;

import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mathijs.pardonmyfrench.Objects.Word;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mFrench;
    private TextView mDutch;
    private TextView mBy;
    private TextView mVotes;
    private Button btnVote;

    private Word mWord;

    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFrench = (TextView) findViewById(R.id.tv_french);
        mDutch = (TextView) findViewById(R.id.tv_dutch);
        mBy = (TextView) findViewById(R.id.tv_by);
        mVotes = (TextView) findViewById(R.id.tv_votes);
        btnVote = (Button) findViewById(R.id.btnVote);
        btnVote.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent.hasExtra("word")) {
            Word word = intent.getParcelableExtra("word");

            mFrench.setText(word.getFrench());
            mDutch.setText(word.getDutch());
            mBy.setText(word.getBy());
            mVotes.setText(String.valueOf(word.getVotes()));

            mWord = word;
        }

        // TODO: voting + editbtn & activity;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }

        if (id == R.id.menu_edit) {
            // send word data to activity
            Toast.makeText(this, "WIP: Edit words", Toast.LENGTH_SHORT).show();

//            startActivity(new Intent(this, EditWordActivity.class));
        }

        if (id == R.id.menu_share) {
            onClickShareWordButton();
        }

        return super.onOptionsItemSelected(item);
    }

    private void onClickShareWordButton() {
        String mimeType = "text/plain";
        String title = "Share this awesome french word!";
        String textToShare = "Weet jij wat '"  + mWord.getFrench() + "' betekent? Het is frans voor '" + mWord.getDutch() + "'!";

        ShareCompat.IntentBuilder.from(this)
                .setChooserTitle(title)
                .setType(mimeType)
                .setText(textToShare)
                .startChooser();
    }

    @Override
    public void onClick(View view) {
        if (view == btnVote) {
            upvoteWord();
        }
    }

    private void upvoteWord() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference tblWords = database.getReference("words");

        Word updated = mWord;
        updated.setVotes(mWord.getVotes() + 1);
        tblWords.child(mWord.getFrench()).setValue(updated);
        mVotes.setText(String.valueOf(updated.getVotes()));

        Toast.makeText(this, "thanks for voting", Toast.LENGTH_SHORT).show();
    }
}
