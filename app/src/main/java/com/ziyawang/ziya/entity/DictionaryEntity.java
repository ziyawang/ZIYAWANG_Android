package com.ziyawang.ziya.entity;

/**
 * Created by 牛海丰 on 2018/5/19.
 */

public class DictionaryEntity {

    private String Id ;
    private String Name ;
    private String Content ;
    private String Type ;

    public DictionaryEntity(){}

    public DictionaryEntity(String id, String name, String content, String type) {
        super();
        Id = id;
        Name = name;
        Content = content;
        Type = type;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
