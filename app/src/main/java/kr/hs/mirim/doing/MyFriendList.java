package kr.hs.mirim.doing;

import java.util.ArrayList;

public class MyFriendList{
    private String about;
    private int condition;
    private String ing;
    private int level;
    private String name;
    private boolean postOnOff;
    private String uid;

    public void MyFriendList(String about, int condition, String ing, int level, String name, String uid){
        this.about = about;
        this.condition = condition;
        this.ing = ing;
        this.level = level;
        this.name = name;
        this.uid = uid;
    }
    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    public String getIng() {
        return ing;
    }

    public void setIng(String ing) {
        this.ing = ing;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getOnoff() {
        return postOnOff;
    }

    public void setOnoff(boolean postOnOff) {
        this.postOnOff = postOnOff;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) { this.uid = uid; }

}
