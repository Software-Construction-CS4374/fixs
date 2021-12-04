package com.yalantis.ucrop.model;

import android.graphics.Bitmap;
/**
 * NEF: fixed methodargumentcouldbefinal, immutablefield
 */
/**
 * Created by Oleksii Shliama [https://github.com/shliama] on 6/21/16.
 */
public class CropParameters {

    private final int mMaxResultImageSizeX, mMaxResultImageSizeY;

    private final Bitmap.CompressFormat mCompressFormat;
    private final int mCompressQuality;
    private final String mImageInputPath, mImageOutputPath;
    private final ExifInfo mExifInfo;


    public CropParameters(final int maxResultImageSizeX, final int maxResultImageSizeY,
                          final Bitmap.CompressFormat compressFormat,final int compressQuality,
                          final String imageInputPath,final String imageOutputPath,final  ExifInfo exifInfo) {
        mMaxResultImageSizeX = maxResultImageSizeX;
        mMaxResultImageSizeY = maxResultImageSizeY;
        mCompressFormat = compressFormat;
        mCompressQuality = compressQuality;
        mImageInputPath = imageInputPath;
        mImageOutputPath = imageOutputPath;
        mExifInfo = exifInfo;
    }

    public int getMaxResultImageSizeX() {
        return mMaxResultImageSizeX;
    }

    public int getMaxResultImageSizeY() {
        return mMaxResultImageSizeY;
    }

    public Bitmap.CompressFormat getCompressFormat() {
        return mCompressFormat;
    }

    public int getCompressQuality() {
        return mCompressQuality;
    }

    public String getImageInputPath() {
        return mImageInputPath;
    }

    public String getImageOutputPath() {
        return mImageOutputPath;
    }

    public ExifInfo getExifInfo() {
        return mExifInfo;
    }

}
