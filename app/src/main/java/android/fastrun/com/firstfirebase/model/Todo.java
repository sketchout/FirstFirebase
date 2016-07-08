package android.fastrun.com.firstfirebase.model;

/**
 * Created by admin on 2016-07-08.
 */
public class Todo {

    public Todo() {

        this.timeStamp = System.currentTimeMillis() ;

    }

    public Todo(String title) {

        this.timeStamp = System.currentTimeMillis() ;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    private String title;
    private Long timeStamp;

}
