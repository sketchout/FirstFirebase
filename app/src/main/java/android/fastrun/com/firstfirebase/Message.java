package android.fastrun.com.firstfirebase;

import java.util.Date;

/**
 * Created by admin on 2016-06-28.
 */
public class Message {
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private Date date;
    private String sender;
    private String message;

}
