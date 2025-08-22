package com.customers.grocerystoresforcustomer.Model;

public class Chats_Table {
    String sender;
    String receiver;
    String message;
    boolean isseen;
    String sendingtime;


    public Chats_Table() {
    }

    public Chats_Table(String sender, String receiver, String message,boolean isseen, String sendingtime) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.isseen = isseen;
        this.sendingtime = sendingtime;

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public String getSendingtime() {
        return sendingtime;
    }

    public void setSendingtime(String sendingtime) {
        this.sendingtime = sendingtime;
    }
}
