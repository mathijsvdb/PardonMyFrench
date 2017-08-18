package com.example.mathijs.pardonmyfrench;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.mathijs.pardonmyfrench.Objects.Word;
import java.util.ArrayList;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {
    // de data die gaat gebruikt worden voor de recyclerview
    private ArrayList<Word> mWords;
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(Word word);
    }

    public WordAdapter(ArrayList<Word> mWords, ListItemClickListener listener) {
        this.mWords = mWords;
        mOnClickListener = listener;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.word_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        WordViewHolder viewHolder = new WordViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        Word word = mWords.get(position);
        holder.bind(word);
    }

    @Override
    public int getItemCount() {
        return mWords.size();
    }

    public class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView listItemWordView;
        private TextView listItemTranslation;
        private Word mWord;

        public WordViewHolder(View itemView) {
            super(itemView);

            listItemWordView = (TextView) itemView.findViewById(R.id.tv_item_word);
            listItemTranslation = (TextView) itemView.findViewById(R.id.tv_item_translation);
            itemView.setOnClickListener(this);
        }

        public void bind(Word word) {
            mWord = word;
            listItemWordView.setText(mWord.getFrench());
            listItemTranslation.setText(mWord.getDutch());
        }

        @Override
        public void onClick(View view) {
//            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(mWord);
        }
    }
}
