package com.example.android.newsapp;

public class NewsFeed {
    //variables needed
    private String mTitle;
    private String mSection;
    private long mTimeInMilliseconds;
    private String mUrl;

    //constructor
    public NewsFeed(String title, String section, long timeInMilliseconds, String url) {
        mTitle = title;
        mSection = section;
        mTimeInMilliseconds = timeInMilliseconds;
        mUrl = url;
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
    public long getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }

    //get url
    public String getUrl() {
        return mUrl;
    }
}
