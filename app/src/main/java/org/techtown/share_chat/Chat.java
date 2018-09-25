package org.techtown.share_chat;

public class Chat {
    //private String friend;

    private String chat;

    private int who;

    private int profile;

    public Chat(String chat, int who, int profile) {
        //this.friend = friend;
        this.chat = chat;
        this.who = who;
        this.profile = profile;
    }

    public int getProfile() {
        return profile;
    }

    /*public String getFriend() {
        return friend;
    }*/

    public String getChat() {
        return chat;
    }

    public int getWho() {
        return who;
    }
}