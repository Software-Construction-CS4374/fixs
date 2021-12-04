package com.yalantis.ucrop.model;
/**
 * NEF: fixed methodargumentcouldbefinal, controlstatementbraces, controlstatementbraces
 */
/**
 * Created by Oleksii Shliama [https://github.com/shliama] on 6/21/16.
 */
public class ExifInfo {

    private int mExifOrientation;
    private int mExifDegrees;
    private int mExifTranslation;

    public ExifInfo(final int exifOrientation, final int exifDegrees, final int exifTranslation) {
        mExifOrientation = exifOrientation;
        mExifDegrees = exifDegrees;
        mExifTranslation = exifTranslation;
    }

    public int getExifOrientation() {
        return mExifOrientation;
    }

    public int getExifDegrees() {
        return mExifDegrees;
    }

    public int getExifTranslation() {
        return mExifTranslation;
    }

    public void setExifOrientation(final int exifOrientation) {
        mExifOrientation = exifOrientation;
    }

    public void setExifDegrees(final int exifDegrees) {
        mExifDegrees = exifDegrees;
    }

    public void setExifTranslation(final int exifTranslation) {
        mExifTranslation = exifTranslation;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
        	return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
        	return false;
        }

        ExifInfo exifInfo = (ExifInfo) obj;

        if (mExifOrientation != exifInfo.mExifOrientation) {
        	return false;
        }
        if (mExifDegrees != exifInfo.mExifDegrees) {
        	return false;
        }
        return mExifTranslation == exifInfo.mExifTranslation;

    }

    @Override
    public int hashCode() {
        int result = mExifOrientation;
        result = 31 * result + mExifDegrees;
        result = 31 * result + mExifTranslation;
        return result;
    }

}
