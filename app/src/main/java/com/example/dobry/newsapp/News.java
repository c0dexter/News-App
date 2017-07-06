package com.example.dobry.newsapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dobry on 04.07.17.
 */

// URL API:
//http://content.guardianapis.com/search?q=debate&tag=world/poland&order-by=newest&order-date=published&show-section=true&show-fields=headline,thumbnail&show-references=author&show-tags=contributor&page=1&page-size=20&api-key=test

public class News implements Parcelable {

    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
        @Override
        public News createFromParcel(Parcel source) {
            return new News(source);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };
    private static final String LOG_TAG = News.class.getSimpleName();
    /**
     * Title of the news
     */
    private final String mTitle;
    /**
     * Section of the news
     */
    private final String mSection;
    /**
     * Author of the news
     */
    private final String mAuthor;
    /**
     * URL address of an image thumbnail
     */
    private final String mThumbUrl;
    /**
     * Published date of news
     */
    private final String mDate;
    /**
     * Url of the site with the news
     */
    private String mNewsUrl;


    // Class constructor

    /**
     * @param title    - (String) tile of article i.e.: New famous singer in London
     * @param section  - (String) section of article i.e: Society
     * @param author   - (String) author of the news i.e: John Doe
     * @param thumbUrl - (String) url of thumbnail image
     * @param date     - (String) publication date
     * @param newsUrl  - (String) url address to the news
     */
    public News(String title, String section, String author, String thumbUrl, String date, String newsUrl) {
        this.mTitle = title;
        this.mSection = section;
        this.mAuthor = author;
        this.mThumbUrl = thumbUrl;
        this.mDate = date;
        this.mNewsUrl = newsUrl;
    }

    protected News(Parcel in) {
        this.mTitle = in.readString();
        this.mSection = in.readString();
        this.mAuthor = in.readString();
        this.mThumbUrl = in.readString();
        this.mDate = in.readString();
        this.mNewsUrl = in.readString();
    }

    // Getters
    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getThumbUrl() {
        return mThumbUrl;
    }

    public String getDate() {
        return mDate;
    }

    public String getNewsUrl() {
        return mNewsUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTitle);
        dest.writeString(this.mSection);
        dest.writeString(this.mAuthor);
        dest.writeString(this.mThumbUrl);
        dest.writeString(this.mDate);
        dest.writeString(this.mNewsUrl);
    }
}

