package android.fastrun.com.firstfirebase.activity;

import android.app.ProgressDialog;
import android.fastrun.com.firstfirebase.adapter.MessageAdapter;
import android.fastrun.com.firstfirebase.R;
import android.fastrun.com.firstfirebase.model.Message;
import android.fastrun.com.firstfirebase.model.MessageSource;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener ,
        MessageSource.MessagesCallbacks {

    private static final String TAG="ChatActivity";

    private static SimpleDateFormat dDateFormat = new SimpleDateFormat("yyyyMMdd");

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // UI data
    private String mRecipient;
    private ListView mListView;
    private String childKey;
    ProgressDialog progressDialog;

    // Message
    private MessageSource.MessagesListener mListener;
    private ArrayList<Message> mMessage;
    private MessageAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Firebase.setAndroidContext(this);

        // Temporarily
        setUserName();
        // Temporarily
        setRoomName();
        setMessageSource();

        makeChatRoomUI(this);
        // set activity title
        setTitle(childKey);

    }

    private void setMessageSource() {
        mListener = MessageSource.addMessageListener(childKey, (MessageSource.MessagesCallbacks)this);
    }

    private void setUserName() {

        checkFirebaseAuth();
        // getDisplayName -- from -- Firebase
        mRecipient = mAuth.getCurrentUser().getDisplayName();
        if ( mRecipient == null) {
            //r = new Random();
            //mRecipient = "Guest" + Integer.toString( r.nextInt(10000) );
            mRecipient = "Guest"+ mAuth.getCurrentUser().getUid().substring(0,5);
        }
    }
    public String getUserName() {
        return mRecipient.toString();
    }

    private void makeChatRoomUI(ChatActivity chatActivity) {

        // message list initialize
        mMessage = new ArrayList<>();
        mAdapter = new MessageAdapter( this, R.layout.activity_chat ,mMessage );

        mListView=(ListView)findViewById(R.id.message_list);
        mListView.setAdapter(mAdapter);


        if ( getSupportActionBar() != null ) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setDisplayShowHomeEnabled(true); // <-
            //getSupportActionBar().setLogo(R.mipmap.ic_launcher);   // ^.^
            //getSupportActionBar().setDisplayUseLogoEnabled(true);  //
        }
        // listen to button
        Button sendMessage = (Button)findViewById(R.id.send_message);
        sendMessage.setOnClickListener((View.OnClickListener) chatActivity);

    }

    @Override
    public void onMessageAdded(Message message) {
        mMessage.add( message );
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MessageSource.removeMessageListenr(mListener);
    }

    @Override
    public void onClick(View view) {

        EditText newMessageView = (EditText) findViewById(R.id.new_message);
        String newMessage = newMessageView.getText().toString();
        newMessageView.setText("");

        Message msg = new Message();
        msg.setDate( new Date() );
        msg.setMessage( newMessage );
        msg.setSender( mRecipient.toString() );

        // save message
        MessageSource.saveMessage( msg, childKey );
    }

    private void setRoomName() {
        /*
        String[] ids = { "Guest156", " - ", mRecipient.toString() };
        Arrays.sort(ids);
        childKey = ids[0] + ids[1] + ids[2];
        */
        Date date = new Date();
        childKey = dDateFormat.format(date);


    }

    private void checkFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if ( user != null ) {
                    // User is signed in
                    Log.d(TAG, "onauthStateChanged:signed_in as uid() :" + user.getUid() );
                    Toast.makeText( ChatActivity.this, "Signed In.", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }
    private void loadProgress() {
        progressDialog = new ProgressDialog( this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

    }
    private void waitProgress() {

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        //onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }
}
