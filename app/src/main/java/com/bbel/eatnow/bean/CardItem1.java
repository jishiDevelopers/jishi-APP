package com.bbel.eatnow.bean;

public class CardItem1 {

    private String  mTextResource;
    private String mTitleResource;
    private String mMarkResource;

    public CardItem1(String title, String text, String mark) {
        mTitleResource = title;
        mTextResource = text;
        mMarkResource=mark;
    }

    public String getText() {
        return mTextResource;
    }
    public String getMark(){
        return mMarkResource;
    }

    public String getTitle() {
        return mTitleResource;
    }
}
