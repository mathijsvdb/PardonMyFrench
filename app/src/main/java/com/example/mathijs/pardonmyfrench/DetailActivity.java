package com.example.mathijs.pardonmyfrench;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private TextView mFrench;
    private TextView mDutch;
    private TextView mBy;
    private TextView mVotes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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
}
