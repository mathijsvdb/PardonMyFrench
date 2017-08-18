package com.example.mathijs.pardonmyfrench.Objects;


import android.os.Parcel;
import android.os.Parcelable;

public class Word implements Parcelable{
    private String french;
    private String dutch;
    private String by;
    private int votes;

    public Word() {
        // needed for firebase
    }

    protected Word(Parcel in) {
        french = in.readString();
        dutch = in.readString();
        by = in.readString();
        votes = in.readInt();
    }

    public static final Creator<Word> CREATOR = new Creator<Word>() {
        @Override
        public Word createFromParcel(Parcel in) {
            return new Word(in);
        }

        @Override
        public Word[] newArray(int size) {
            return new Word[size];
        }
    };

    public String getFrench() {
        return french;
    }

    public void setFrench(String french) {
        this.french = french;
    }

    public String getDutch() {
        return dutch;
    }

    public void setDutch(String dutch) {
        this.dutch = dutch;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(french);
        parcel.writeString(dutch);
        parcel.writeString(by);
        parcel.writeInt(votes);
    }
}
