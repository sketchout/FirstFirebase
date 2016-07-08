package android.fastrun.com.firstfirebase.model;

import android.net.Uri;

/**
 * Created by admin on 2016-07-04.
 */
public class UserInfo
{
    private String name;
    private String email;
    private Uri photoUrl;
    private String uid ;

    UserInfo(String name,String  email,Uri photoUrl,String  uid) {
        this.name = name;
        this.email = email;
        this.photoUrl = photoUrl;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
