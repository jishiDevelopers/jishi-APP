package com.bbel.eatnow;


public class PostMessage {
    private String token;
    private String id;
    private String idRecord;
    private String finalChoice;
    private String judge;
    public void setToken(String token){
        this.token=token;
    }

    public String getToken() {
        return token;
    }

    public String getFinalChoice() {
        return finalChoice;
    }

    public void setFinalChoice(String finalChoice) {
        this.finalChoice = finalChoice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdRecord() {
        return idRecord;
    }

    public String getJudge() {
        return judge;
    }

    public void setIdRecord(String idRecord) {
        this.idRecord = idRecord;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }
}
