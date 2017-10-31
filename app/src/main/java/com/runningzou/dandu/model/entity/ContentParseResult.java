package com.runningzou.dandu.model.entity;

import android.text.SpannableStringBuilder;

/**
 * Created by runningzou on 2017/10/30.
 */

public class ContentParseResult {

    public static final int HTML5_TYPE = -1;

    private SpannableStringBuilder mStringBuilder;
    private int mType;
    private String mImgWidth;
    private String mImgHeight;
    private String mImgUrl;
    private String mHtml5Url;

    public ContentParseResult() {
    }

    public SpannableStringBuilder getStringBuilder() {
        return mStringBuilder;
    }

    public void setStringBuilder(SpannableStringBuilder stringBuilder) {
        mStringBuilder = stringBuilder;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public String getImgWidth() {
        return mImgWidth;
    }

    public void setImgWidth(String imgWidth) {
        mImgWidth = imgWidth;
    }

    public String getImgHeight() {
        return mImgHeight;
    }

    public void setImgHeight(String imgHeight) {
        mImgHeight = imgHeight;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        mImgUrl = imgUrl;
    }


    public String getHtml5Url() {
        return mHtml5Url;
    }

    public void setHtml5Url(String html5Url) {
        mHtml5Url = html5Url;
    }
}
