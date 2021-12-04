package com.yalantis.ucrop.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

/**
 * Created by Oleksii Shliama [https://github.com/shliama] on 6/24/16.
 */
/**
 * NEF: fixed all MethodArgumentCouldbeFinals and shortvariables
 * @author nicolefavela
 *
 */
public class AspectRatio implements Parcelable {

    @Nullable
    private final String mAspectRatioTitle;
    private final float mAspectRatioX;
    private final float mAspectRatioY;

    public AspectRatio(final @Nullable String aspectRatioTitle, final float aspectRatioX, final float aspectRatioY) {
        mAspectRatioTitle = aspectRatioTitle;
        mAspectRatioX = aspectRatioX;
        mAspectRatioY = aspectRatioY;
    }

    protected AspectRatio(final Parcel input) {
        mAspectRatioTitle = input.readString();
        mAspectRatioX = input.readFloat();
        mAspectRatioY = input.readFloat();
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
        dest.writeString(mAspectRatioTitle);
        dest.writeFloat(mAspectRatioX);
        dest.writeFloat(mAspectRatioY);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AspectRatio> CREATOR = new Creator<AspectRatio>() {
        @Override
        public AspectRatio createFromParcel(final Parcel input) {
            return new AspectRatio(input);
        }

        @Override
        public AspectRatio[] newArray(final int size) {
            return new AspectRatio[size];
        }
    };

    @Nullable
    public String getAspectRatioTitle() {
        return mAspectRatioTitle;
    }

    public float getAspectRatioX() {
        return mAspectRatioX;
    }

    public float getAspectRatioY() {
        return mAspectRatioY;
    }

}
