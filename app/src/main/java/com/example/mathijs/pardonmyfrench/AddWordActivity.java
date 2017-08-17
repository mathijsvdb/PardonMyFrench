package com.example.mathijs.pardonmyfrench;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mathijs.pardonmyfrench.Objects.Word;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddWordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText wFrench;
    private EditText wDutch;
    private Button btnAddWord;

    // TODO: get the current user
    private FirebaseUser currentUser;
    private DatabaseReference dbWords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

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

        dbWords = FirebaseDatabase.getInstance().getReference("words");
        dbWords.child(french).setValue(word);

        Toast.makeText(this, "Word has been added.", Toast.LENGTH_SHORT).show();

        // TODO: return to your words list.

    }

    @Override
    public void onClick(View view) {
        if (view == btnAddWord) {
            addWord();
        }
    }
}
