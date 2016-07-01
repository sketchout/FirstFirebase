package android.fastrun.com.firstfirebase;

import android.app.Application;
import android.os.Bundle;

import com.firebase.client.Firebase;

/**
 * Created by admin on 2016-06-30.
 */
public class ChatLife extends Application {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate();
        Firebase.setAndroidContext(this);

    }
}
