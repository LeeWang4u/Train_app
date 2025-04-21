package com.example.train_app.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Station implements Parcelable {
    @SerializedName("stationId")
    @Expose
    private int stationId;

    @SerializedName("stationName")
    @Expose
    private String stationName;

    @SerializedName("location")
    @Expose
    private String location;

    // Constructors
    public Station() {}

    public Station(int stationId, String stationName, String location) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.location = location;
    }

    protected Station(Parcel in) {
        stationId = in.readInt();
        stationName = in.readString();
        location = in.readString();
    }

    public static final Creator<Station> CREATOR = new Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel in) {
            return new Station(in);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(stationId);
        parcel.writeString(stationName);
        parcel.writeString(location);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // Getter - Setter - toString (giữ nguyên như trước)
}
