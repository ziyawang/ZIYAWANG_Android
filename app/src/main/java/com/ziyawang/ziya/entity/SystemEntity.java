package com.ziyawang.ziya.entity;

/**
 * Created by 牛海丰 on 2016/8/20.
 */
public class SystemEntity {

    private String TextID ;
    private String Title ;
    private String Text ;
    private String Time ;
    private String Status ;

    private String type ;
    private String ProjectId ;

    public SystemEntity(){}

    public SystemEntity(String textID, String title, String text, String time, String status , String type , String ProjectId ) {
        super();
        TextID = textID;
        Title = title;
        Text = text;
        Time = time;
        Status = status;
        this.type = type ;
        this.ProjectId = ProjectId ;
    }

    public String getTextID() {
        return TextID;
    }

    public void setTextID(String textID) {
        TextID = textID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProjectId() {
        return ProjectId;
    }

    public void setProjectId(String projectId) {
        ProjectId = projectId;
    }
}
