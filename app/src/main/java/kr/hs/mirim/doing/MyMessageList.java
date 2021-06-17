package kr.hs.mirim.doing;

import java.util.TimeZone;

public class MyMessageList {
    private String content;
    private boolean read;
    private String gist;
    private String receiver;
    private String sender;
    private String time;

    private MyMessageList(){

    }
    private MyMessageList(String content, boolean read, String gist, String receiver, String sender, String time){
        this.content = content;
        this.read = read;
        this.gist = gist;
        this.receiver = receiver;
        this.sender = sender;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getGist() {
        return gist;
    }

    public void setGist(String gist) {
        this.gist = gist;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
