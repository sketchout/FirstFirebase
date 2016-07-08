package android.fastrun.com.firstfirebase.model;

import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by admin on 2016-06-28.
 */
public class MessageSource {

    private static final String TAG="MessageSource";

    // message io URL
    private static Firebase ioFirebase =
            new Firebase(Constants.FIREBASE_URL);

    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddmmss", Locale.US );

    private static String COLUMN_SENDER = "chatSender";
    private static String COLUMN_MESSAGE = "chatMessage";

    public interface MessagesCallbacks {
        public void onMessageAdded(Message message);
    }

    // method : saveMessage
    public static void saveMessage( Message message, String childKey) {

        Date date = message.getDate();

        // date
        String grandChildKey = sDateFormat.format(date);

        HashMap<String, String> hashMsg = new HashMap<>();
        hashMsg.put (COLUMN_SENDER, message.getSender() );
        hashMsg.put(COLUMN_MESSAGE, message.getMessage() );

        // setValue()
        ioFirebase.child(childKey).child(grandChildKey).setValue(hashMsg);
    }


    // method : addMessageListener
    public static MessagesListener addMessageListener (String childKey,final MessagesCallbacks
            callbacks) {
        MessagesListener listener = new MessagesListener(callbacks);
        ioFirebase.child( childKey ).addChildEventListener( listener );
        return listener;
    }

    // method : removeMessageListenr
    public static void removeMessageListenr(MessagesListener listener ) {

        ioFirebase.removeEventListener( listener );
    }

    // class : MessagesListener
    public static class MessagesListener implements ChildEventListener {

        private MessagesCallbacks callbacks;

        MessagesListener( MessagesCallbacks callbacks) {
            this.callbacks = callbacks;
        }

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            Message message = new Message();

            // key , value - dataSnapshot
            try {
                message.setDate( sDateFormat.parse( dataSnapshot.getKey() ) );
            } catch (Exception e) {
                Log.d(TAG, "Couldn't parse date :" + e);
            }

            HashMap<String, String> hashMsg = (HashMap)dataSnapshot.getValue();
            message.setSender( hashMsg.get(COLUMN_SENDER) );
            message.setMessage( hashMsg.get(COLUMN_MESSAGE) );

            if ( callbacks != null ) {
                callbacks.onMessageAdded(message);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {
        }
    }
}

