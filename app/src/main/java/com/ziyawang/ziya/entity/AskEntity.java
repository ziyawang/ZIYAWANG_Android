package com.ziyawang.ziya.entity;

/**
 * Created by 牛海丰 on 2018/5/19.
 */

public class AskEntity {

    private String Id ;
    private String question ;
    private String Answer ;

    public AskEntity() {}

    public AskEntity(String id, String question, String answer) {
        super();
        Id = id;
        this.question = question;
        Answer = answer;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }
}
