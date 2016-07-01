package android.fastrun.com.firstfirebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG="MainActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkFirebaseAuth();
    }

    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void onStop() {
        super.onStop();
        if ( mAuthListener != null ) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    private void newIntent() {

        Intent intent = new Intent( this, Main2Activity.class );
        startActivity(intent);
        finish();
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
                    newIntent();

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        findViewById(R.id.btn_guestLogin).setOnClickListener(this);
    }

    private void doSingIn() {
        //showProgressDialog();
        mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "signInAnonymously: onComplete:" + task.isSuccessful() );
                if ( !task.isSuccessful() ) {
                    Log.w(TAG, "signInAnonymously", task.getException() ) ;
                    Toast.makeText( MainActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch( v.getId() ) {
            case R.id.btn_guestLogin:
                doSingIn();
                break;
        }
    }
}
