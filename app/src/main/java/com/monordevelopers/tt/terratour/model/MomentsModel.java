package com.monordevelopers.tt.terratour.model;

public class MomentsModel {
    String momentDetails,momnentTime,imgUrl;
    int EvnId,monentId;

    public MomentsModel(String momentDetails, String momnentTime, String imgUrl, int evnId, int monentId) {
        this.momentDetails = momentDetails;
        this.momnentTime = momnentTime;
        this.imgUrl = imgUrl;
        EvnId = evnId;
        this.monentId = monentId;
    }

    public MomentsModel(String momentDetails, String momnentTime, String imgUrl, int evnId) {
        this.momentDetails = momentDetails;
        this.momnentTime = momnentTime;
        this.imgUrl = imgUrl;
        EvnId = evnId;
    }

    public String getMomentDetails() {
        return momentDetails;
    }

    public String getMomnentTime() {
        return momnentTime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public int getEvnId() {
        return EvnId;
    }

    public int getMonentId() {
        return monentId;
    }
}
