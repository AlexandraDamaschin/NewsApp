package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NewsFeedAdapter extends ArrayAdapter<NewsFeed> {

    //2018-04-22T18:03:48Z
    // date separator: T
    private static final String DATE_SEPARATOR = "T";

    public NewsFeedAdapter(Context context, List<NewsFeed> newsFeeds) {
        super(context, 0, newsFeeds);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_feed_list_item, parent, false);
        }
        //get position
        NewsFeed currentNewsFeed = getItem(position);

        //find title
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        titleView.setText(currentNewsFeed.getTitle());

        //find section
        TextView sectionView = (TextView) listItemView.findViewById(R.id.section_name);
        sectionView.setText(currentNewsFeed.getSection());

        //get date as 2018-04-22T18:03:48Z
        String originalDate = currentNewsFeed.getTimeInMilliseconds();
        String date;
        String time;

        // Check whether the originalLocation string contains the " of " text
        if (originalDate.contains(DATE_SEPARATOR)) {
            //split string based on of in the name
            String[] parts = originalDate.split(DATE_SEPARATOR);
            // DATE offset should be " 2018-04-22" + "T" --> "18:03:48"
            date = parts[0] + DATE_SEPARATOR;
            //time: 18:03:48
            time = parts[1];
        }

        //find date
        TextView dateLocationView = (TextView) listItemView.findViewById(R.id.date);
        dateLocationView.setText(currentNewsFeed.getTimeInMilliseconds() );
        dateLocationView.setText(date);

        //find time
        TextView timeOffsetView = (TextView) listItemView.findViewById(R.id.time);
        timeOffsetView.setText(time);

        return listItemView;
    }

    //implemented methods

    //date as
    //2018-04-22T18:03:48Z

}
