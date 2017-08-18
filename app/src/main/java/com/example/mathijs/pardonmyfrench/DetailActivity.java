package com.example.mathijs.pardonmyfrench;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {
    private TextView mFrench;
    private TextView mDutch;
    private TextView mBy;
    private TextView mVotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mFrench = (TextView) findViewById(R.id.tv_french);
        mDutch = (TextView) findViewById(R.id.tv_dutch);
        mBy = (TextView) findViewById(R.id.tv_by);
        mVotes = (TextView) findViewById(R.id.tv_votes);

        Intent intent = getIntent();

        if (intent.hasExtra("word_french")) {
            mFrench.setText(intent.getStringExtra("word_french"));
        }

        if (intent.hasExtra("word_dutch")) {
            mDutch.setText(intent.getStringExtra("word_dutch"));
        }

        if (intent.hasExtra("word_by")) {
            mBy.setText(intent.getStringExtra("word_by"));
        }

        if (intent.hasExtra("word_votes")) {
            mVotes.setText(intent.getStringExtra("word_votes"));
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

        return super.onOptionsItemSelected(item);
    }
}
