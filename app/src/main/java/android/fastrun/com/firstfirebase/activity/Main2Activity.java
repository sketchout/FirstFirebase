package android.fastrun.com.firstfirebase.activity;

import android.content.Intent;
import android.fastrun.com.firstfirebase.R;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG="Main2Activity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        setButtonListen();

    }

    private void setButtonListen() {

        //findViewById(R.id.Btn_AnonyOut).setOnClickListener(this);
        findViewById(R.id.Btn_EnterChat).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch( view.getId() ) {
            //case R.id.Btn_AnonyOut:
              //  signOut();
                //break;
            case R.id.Btn_EnterChat:
                enterChat();
                break;
        }
    }

    private void indentToLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void indentToChat() {
        Toast.makeText( Main2Activity.this, "Chat now!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
        finish();
    }

    private void signOut() {
        // temporary block
        //mAuth.signOut();
        //Log.d(TAG, "signOut: onComplete --"  );
        //indentToLogin();
        Toast.makeText( Main2Activity.this, "Temporary can't Signout.", Toast.LENGTH_SHORT).show();
    }

    private void enterChat() {
        indentToChat();
    }

}
