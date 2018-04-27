package com.example.android.newsapp;

public class NewsFeed {
    //variables needed
    private String mTitle;
    private String mSection;
    private String mTimeInMilliseconds;
    private String mUrl;
    private String mAuthor;

    //constructor
    public NewsFeed(String title, String section, String timeInMilliseconds, String url, String author) {
        mTitle = title;
        mSection = section;
        mTimeInMilliseconds = timeInMilliseconds;
        mUrl = url;
        mAuthor = author;
    }

    //get title
    public String getTitle() {
        return mTitle;
    }

    //get section
    public String getSection() {
        return mSection;
    }

    //get date
    public String getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    //get url
    public String getUrl() {
        return mUrl;
    }

    //get author
    public String getAuthor() {
        return mAuthor;
    }
}
