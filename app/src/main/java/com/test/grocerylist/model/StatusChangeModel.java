package com.test.grocerylist.model;

import android.os.Parcel;
import android.os.Parcelable;

public class StatusChangeModel implements Parcelable {
    private long date;
    private String status;

    public StatusChangeModel(long date, String status) {
        this.date = date;
        this.status = status;
    }


    protected StatusChangeModel(Parcel in) {
        date = in.readLong();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(date);
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StatusChangeModel> CREATOR = new Creator<StatusChangeModel>() {
        @Override
        public StatusChangeModel createFromParcel(Parcel in) {
            return new StatusChangeModel(in);
        }

        @Override
        public StatusChangeModel[] newArray(int size) {
            return new StatusChangeModel[size];
        }
    };

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
