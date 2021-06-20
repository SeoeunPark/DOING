package kr.hs.mirim.doing;

import java.util.TimeZone;

public class MyMessageList {
    private String content;
    private boolean read;
    private String gist;
    private String receiver;
    private String receiver_name;
    private String sender;
    private String sender_name;
    private String time;

    private MyMessageList(){

    }
    private MyMessageList(String content, boolean read, String gist, String receiver, String receiver_name, String sender, String sender_name, String time){
        this.content = content;
        this.read = read;
        this.gist = gist;
        this.receiver = receiver;
        this.sender = sender;
        this.time = time;
        this.receiver_name = receiver_name;
        this.sender_name = sender_name;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
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
