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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mFrench;
    private TextView mDutch;
    private TextView mBy;
    private TextView mVotes;
    private Button btnVote;
    private Word mWord;
    private List<String> mUsersVoted = new ArrayList<>();
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
        final FirebaseAuth FBAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        if (intent.hasExtra("word")) {
            mWord = intent.getParcelableExtra("word");

            mFrench.setText(mWord.getFrench());
            mDutch.setText(mWord.getDutch());
            mBy.setText(mWord.getBy());
            mVotes.setText(String.valueOf(mWord.getVotes()));
        }

        // get the old usersVoted list
        database = FirebaseDatabase.getInstance();
        DatabaseReference tblWords = database.getReference("words");
        final String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        tblWords.child(mWord.getFrench()).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("usersVoted").getValue() != null) {
                            GenericTypeIndicator<ArrayList<String>> a = new GenericTypeIndicator<ArrayList<String>>() {};
                            mUsersVoted = dataSnapshot.child("usersVoted").getValue(a);

                            if (mUsersVoted.contains(FBAuth.getCurrentUser().getUid())) {
                                btnVote.setEnabled(false);
                                btnVote.setText("You voted!");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
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
        // TODO: only allow 1 vote.
        String Uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (mUsersVoted.contains(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            Toast.makeText(this, "You've already voted for this word.", Toast.LENGTH_SHORT).show();
        } else {
            DatabaseReference tblWords = database.getReference("words");

            Word updated = mWord;
            updated.setVotes(mWord.getVotes() + 1);
            tblWords.child(mWord.getFrench()).setValue(updated);
            mVotes.setText(String.valueOf(updated.getVotes()));

            DatabaseReference tblWordsVotes = tblWords.child(updated.getFrench()).child("usersVoted");
            mUsersVoted.add(Uid);
            tblWordsVotes.setValue(mUsersVoted);
            btnVote.setEnabled(false);
            btnVote.setText("You voted!");

            Toast.makeText(this, "Thanks for voting", Toast.LENGTH_SHORT).show();
        }
    }
}
