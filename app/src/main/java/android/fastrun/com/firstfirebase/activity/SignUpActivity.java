package android.fastrun.com.firstfirebase.activity;

import android.content.Intent;
import android.fastrun.com.firstfirebase.R;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG="SignUpActivity";

    // SignUp
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signUpButton;

    // Link
    private TextView loginTextView;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = (Toolbar)findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();
        findView();
    }

    private void findView() {

        // Sign Up
        emailEditText = (EditText)findViewById(R.id.emailField);
        passwordEditText = (EditText)findViewById(R.id.passwordField);
        signUpButton = (Button)findViewById(R.id.signupButton);

        // Link
        loginTextView = (TextView)findViewById(R.id.loginText);
        loginTextView.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                Intent i = new Intent( SignUpActivity.this, LoginActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                // clears the activity stack --
//                // This prevents the user going back to the main activity
//                // when they press the Back button from the login view
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(i);
                loadLoginView();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                if ( email.isEmpty() || password.isEmpty() ) {
                    showVerificationError();
                } else {
                    try {
                        requestFirebaseCreate(email, password);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void requestFirebaseCreate(String email, String password) throws Exception {

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful() );
                        if ( !task.isSuccessful() ) {

                            Log.w(TAG, "signInWithEmail : ", task.getException() );

                            showError( task.getException().getLocalizedMessage() );

                        } else {
                            loadLoginView();
                        }
                    }
                });
    }

    private void loadLoginView() {

        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // clears the activity stack --
        // This prevents the user going back to the main activity
        // when they press the Back button from the login view
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    private void showVerificationError() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(SignUpActivity.this);
        builder.setMessage(R.string.sign_up_error_message)
                .setTitle(R.string.sign_up_error_title)
                .setPositiveButton(android.R.string.ok, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showError(String message) {

        // Authenticated failed with error firebaseError
        AlertDialog.Builder builder =
                new AlertDialog.Builder(SignUpActivity.this);
        builder.setMessage( message )
                .setTitle(R.string.login_error_title)
                .setPositiveButton(android.R.string.ok, null);

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
