package com.ziyawang.ziya.entity;

/**
 * Created by 牛海丰 on 2018/5/19.
 */

public class FastEntity {

    // messageList
    private String Id ;
    private String Content ;
    private String created_at ;
    private String State ;
    private String updated_at ;
    private String time ;

    public FastEntity() {}

    public FastEntity(String id, String content, String created_at, String state, String updated_at, String time) {
        super();
        Id = id;
        Content = content;
        this.created_at = created_at;
        State = state;
        this.updated_at = updated_at;
        this.time = time;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
