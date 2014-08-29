package com.project.my.inz.Model;

import java.io.Serializable;

/**
 * Created by Luki on 2014-06-30.
 */
public class QuestM implements DataModel, Serializable {

    private String id;
    private String name;
    private String state;
    private String points;
    private String desc;
    private String data;

    public QuestM(){}

    public QuestM(String name, String desc, String points ) {
        this.name = name;
        this.points = points;
        this.desc = desc;
    }
    public QuestM(String name, String desc, String points, String data, String state  ) {
        this.state = state;
        this.name = name;
        this.points = points;
        this.desc = desc;
        this.data = data;
    }
    public QuestM(String id, String name, String desc, String points, String data, String state ) { //6
        this.id=id;
        this.state = state;
        this.name = name;
        this.points = points;
        this.desc = desc;
        this.data = data;
    }

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getName() {
        return name;
    }
    public void setName(String name) {this.name = name;}

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public String getPoints(){return points;}
    public void setPoints(String points){
        this.points = points;
    }

    public String getDesc() { return desc; }
    public void setDesc(String desc) {this.desc = desc;}

    public String getData() { return data; }
    public void setData(String data) {this.data = data;}

}
