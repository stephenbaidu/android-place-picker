package com.github.stephenbaidu.placepicker;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by stephen on 3/1/15.
 */
public class PlaceDetail implements Parcelable {
    public String placeId;
    public String description;
    public Double latitude;
    public Double longitude;

    public PlaceDetail(String placeId, String description, Double latitude, Double longitude) {
        this.placeId = placeId;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String toString() {
        return placeId + ": <" + description + "> (" + Double.toString(latitude) + ", " + Double.toString(longitude) + ")";
    }

    protected PlaceDetail(Parcel in) {
        placeId = in.readString();
        description = in.readString();
        latitude = in.readByte() == 0x00 ? null : in.readDouble();
        longitude = in.readByte() == 0x00 ? null : in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(placeId);
        dest.writeString(description);
        if (latitude == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(longitude);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<PlaceDetail> CREATOR = new Parcelable.Creator<PlaceDetail>() {
        @Override
        public PlaceDetail createFromParcel(Parcel in) {
            return new PlaceDetail(in);
        }

        @Override
        public PlaceDetail[] newArray(int size) {
            return new PlaceDetail[size];
        }
    };
}