package com.bbel.eatnow.bean;

public class RankItem {
    private String canteenName;
    private String restName;
    private String dishName;
    private String count;
    private int imageId;

    public RankItem(String canteenName, String restName, String dishName, String count, int imageId) {
        this.canteenName = canteenName;
        this.restName = restName;
        this.dishName = dishName;
        this.count = count;
        this.imageId = imageId;
    }

    public String getCanteenName() {
        return canteenName;
    }

    public void setCanteenName(String canteenName) {
        this.canteenName = canteenName;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDishName() {

        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getRestName() {

        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }
}
