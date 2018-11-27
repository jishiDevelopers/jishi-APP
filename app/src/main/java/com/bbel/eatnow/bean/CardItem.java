package com.bbel.eatnow.bean;


public class CardItem {

    private String mTextResource;
    private String mTitleResource;

    public CardItem(String questionId, String question) {
        mTitleResource = questionId;
        mTextResource = question;
    }

    public String getText() {
        return mTextResource;
    }

    public String getTitle() {
        return mTitleResource;
    }

}
