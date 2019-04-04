package com.sydehealth.sydehealth.adapters;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by docking on 16/4/25 22:35.
 */
public class RecycleBean implements Parcelable {

    private String title = null;
    private String url = null;
    private int type = 0;
    private int id = 0;
    private String count = null;
    private String tag = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeInt(this.type);
        dest.writeInt(this.id);
        dest.writeString(this.count);
        dest.writeString(this.tag);
    }

    public RecycleBean() {
    }

    protected RecycleBean(Parcel in) {
        this.title = in.readString();
        this.url = in.readString();
        this.type = in.readInt();
        this.id = in.readInt();
        this.count = in.readString();
        this.tag = in.readString();
    }

    public static final Creator<RecycleBean> CREATOR = new Creator<RecycleBean>() {
        public RecycleBean createFromParcel(Parcel source) {
            return new RecycleBean(source);
        }

        public RecycleBean[] newArray(int size) {
            return new RecycleBean[size];
        }
    };
}
