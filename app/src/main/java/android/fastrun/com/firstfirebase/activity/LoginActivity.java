package android.fastrun.com.firstfirebase.activity;

import android.content.Intent;
import android.fastrun.com.firstfirebase.R;
import android.fastrun.com.firstfirebase.model.Constants;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG="LoginActivity";

    // Login
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;

    // Link
    private TextView signUpTextView;

    //private UserInfoSource userInfoSrc;
    private FirebaseAuth fireAuth;
    //private DatabaseReference dbRef;
    private Firebase fireDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar)findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        fireDb = new Firebase ( Constants.FIREBASE_URL );
        fireAuth = FirebaseAuth.getInstance();
        findViewAndListen();
    }


    private void findViewAndListen() {

        // Login
        emailEditText = (EditText)findViewById(R.id.emailField);
        passwordEditText = (EditText)findViewById(R.id.passwordField);
        loginButton = (Button)findViewById(R.id.loginButton);

        // link
        signUpTextView = (TextView)findViewById(R.id.signUpText);
        signUpTextView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
//                Intent i = new Intent(LoginActivity.this,SignUpActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                // clears the activity stack --
//                // This prevents the user going back to the main activity
//                // when they press the Back button from the login view
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(i);
                loadSignUpView();
            }

        });

        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                if ( email.isEmpty() || password.isEmpty() ) {
                    showVerificationError();
                } else {
                    try {
                        requestFirebaseAuth(email, password);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void showVerificationError() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(R.string.login_error_message)
                .setTitle(R.string.login_error_title)
                .setPositiveButton(android.R.string.ok, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showError(String message) {

        // Authenticated failed with error firebaseError
        AlertDialog.Builder builder =
                new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage( message )
                .setTitle(R.string.login_error_title)
                .setPositiveButton(android.R.string.ok, null);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void loadMainView() {

        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // clears the activity stack --
        // This prevents the user going back to the main activity
        // when they press the Back button from the login view
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void loadSignUpView() {

        Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // clears the activity stack --
        // This prevents the user going back to the main activity
        // when they press the Back button from the login view
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    // users ...
    public void saveUsersEmail(String emailAddress) throws Exception {


        // users
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("email", emailAddress);

        fireDb.child("users").child( fireAuth.getCurrentUser().getUid() )
                .setValue(map);
    }

    private void requestFirebaseAuth(String email, String password) throws Exception {

        final String emailAddress = email;
        //userInfoSrc = new UserInfoSource();
        // login with an email/password combination
        fireAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful() );
                        if ( !task.isSuccessful() ) {
                            Log.w(TAG, "signInWithEmail : ", task.getException() );

                            showError( task.getException().getLocalizedMessage() );
                        } else {

                            try {
                                saveUsersEmail(emailAddress);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            loadMainView();
                        }
                    }
                });
                /*
                new Firebase.AuthResultHandler() {

                    @Override
                    public void onAuthenticated(AuthData authData) {
                        userInfoSrc.saveUsersEmail(emailAddress);
                        loadMainView();
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        // Authenticated failed with error firebaseError
                        showError( firebaseError.getMessage() );
                    }
                });
                */
        /*
        userInfoSrc.getIoFirebase().authWithPassword(email, password,
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG,"createUserWithEmailAndPassword isSuccessful() :"
                                + task.isSuccessful( )) ;

                        // If sign in fails, display a message to the user.
                        // If sign in succeeds the fireAuth state listener will be notified
                        // and login to handle the signed in user can be handled in the listener
                        if ( !task.isSuccessful() ) {

                            showCreateUserError( task.getException().getMessage() );

                        } else {

                            // Authenticated successfully with payload authData

                            userInfoSrc.saveUsersEmail(emailAddress);

                            loadMainView();

                        }
                    }
                });
*/
    }
}
