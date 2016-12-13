package com.ziyawang.ziya.entity;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 牛海丰 on 2016/11/4.
 */
public class QuestionsEntity {
    private String ID ;
    private String Question ;
    private List list ;
    private String Type ;
    private String Sort ;
    private String Paper ;
    private List list02 ;
    private String Input ;

    public QuestionsEntity(){}

    public QuestionsEntity(String ID, String question, List list, String type, String sort, String paper , List list02 , String input) {
        this.ID = ID;
        Question = question;
        this.list = list;
        Type = type;
        Sort = sort;
        Paper = paper;
        this.list02 = list02 ;
        Input = input ;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getSort() {
        return Sort;
    }

    public void setSort(String sort) {
        Sort = sort;
    }

    public String getPaper() {
        return Paper;
    }

    public void setPaper(String paper) {
        Paper = paper;
    }

    public List getList02() {
        return list02;
    }

    public void setList02(List list02) {
        this.list02 = list02;
    }

    public String getInput() {
        return Input;
    }

    public void setInput(String input) {
        Input = input;
    }
}
