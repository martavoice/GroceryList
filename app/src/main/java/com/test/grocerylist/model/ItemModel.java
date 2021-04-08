package com.test.grocerylist.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ItemModel implements Parcelable {
    private String name;
    private String status;
    private int priority;
    private List<StatusChangeModel> changesHistory;

    public ItemModel(String name, String status, int priority, List<StatusChangeModel> changesHistory) {
        this.name = name;
        this.status = status;
        this.priority = priority;
        this.changesHistory = changesHistory;
    }

    protected ItemModel(Parcel in) {
        name = in.readString();
        status = in.readString();
        priority = in.readInt();
        changesHistory = in.createTypedArrayList(StatusChangeModel.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(status);
        dest.writeInt(priority);
        dest.writeTypedList(changesHistory);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemModel> CREATOR = new Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel in) {
            return new ItemModel(in);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public List<StatusChangeModel> getChangesHistory() {
        return changesHistory;
    }

    public void setChangesHistory(List<StatusChangeModel> changesHistory) {
        this.changesHistory = changesHistory;
    }


    public enum ItemStatus {
        RAN_OUT("Ran out"), HAVE("Have");
        private final String name;

        ItemStatus(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
