package android.fastrun.com.firstfirebase.model;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by admin on 2016-07-04.
 */
public class Constants {
    public static final String FIREBASE_URL="https://project-8504355771509178481.firebaseio.com/";

    public void ShowToast(Context context, String message, boolean isLong ) {
        Toast.makeText( context, message, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT ).show();
    }
}
