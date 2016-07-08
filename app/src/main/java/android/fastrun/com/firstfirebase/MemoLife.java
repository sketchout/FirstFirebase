package android.fastrun.com.firstfirebase;

import android.app.Application;
import android.os.Bundle;

import com.firebase.client.Firebase;

/**
 * Created by admin on 2016-06-30.
 *
 * The Firebase library must be initialized once with an Android context.
 * This must happen before any Firebase app reference is created or used.
 *
 * You can add the setup code to the Application or Activity onCreate method.
 * This tutorial will use the Application class option.
 *
 */
public class MemoLife extends Application {

    @Override
    public void onCreate() {

        super.onCreate();

        //Firebase library initialized once
        Firebase.setAndroidContext(this);

    }
}
