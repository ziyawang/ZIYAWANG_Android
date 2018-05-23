package com.ziyawang.ziya.entity;

/**
 * Created by 牛海丰 on 2018/5/22.
 */

public class MyAskEntity {

    private String Id ;
    private String Question ;
    private String State ;

    public MyAskEntity(){}

    public MyAskEntity(String id, String question, String State) {
        super();
        Id = id;
        Question = question;
        this.State = State;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

}
