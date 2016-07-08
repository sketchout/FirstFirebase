package android.fastrun.com.firstfirebase.model;

import android.fastrun.com.firstfirebase.model.Constants;
import android.fastrun.com.firstfirebase.model.UserInfo;
import android.support.annotation.NonNull;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016-07-04.
 */
public class UserInfoSource {

    private static final String TAG ="UserInfoSource";

    final Firebase ioFirebase;
    private UserInfo userInfo;
    private FirebaseAuth auth;


    UserInfoSource() {
        ioFirebase = new Firebase( Constants.FIREBASE_URL );
        //auth = FirebaseAuth.getInstance();
    }

    public Firebase getIoFirebase() {
        return ioFirebase;
    }

    public void setAuth(FirebaseAuth auth) {
        this.auth = auth;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }


    // local save
    public void saveUserInfo(FirebaseUser fUser) {
        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userInfo = new UserInfo( fUser.getDisplayName(),
                fUser.getEmail(), fUser.getPhotoUrl(), fUser.getUid() );
    }
    // users ...
    public void saveEmail(String emailAddress) throws  Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("email", emailAddress);
        ioFirebase.child("users")
                .child( auth.getCurrentUser().getUid() )
                .setValue(map);
    }

    // https://firebase.google.com/docs/auth/android/manage-users
    // user
    public void updateEmail(String emailAddress) throws  Exception{
        auth.getCurrentUser().updateEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful() ) {
                            Log.d(TAG,"User email address updated");
                        }
                    }
                });
    }
    // user
    public void updatePassword(String password) throws Exception {
        auth.getCurrentUser().updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if ( task.isSuccessful() ) {
                            Log.d(TAG, "User password updated.");
                        }
                    }
                });
    }

    // auth
    public void sendPasswordResetEmail(String emailAddress) throws  Exception {

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if ( task.isSuccessful() ) {
                            Log.d(TAG, "Email Sent.");
                        }
                    }
                });
    }

    // user
    public void deleteUser() {

        auth.getCurrentUser().delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if ( task.isSuccessful() ) {
                            Log.d(TAG, "user account deleted");
                        }
                    }
                });
    }

}
