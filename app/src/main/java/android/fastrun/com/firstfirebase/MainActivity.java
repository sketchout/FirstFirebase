package android.fastrun.com.firstfirebase;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="MainActivity";
    private UserInfoSource userInfoSrc;
    private Firebase fireDb, fireDbChild;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    private ListView listView;
    private ProgressBar progressBar;

    private ArrayAdapter<String> adapter;
    // add
    private String userId;
    private String itemUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main);

        fireDb = new Firebase( Constants.FIREBASE_URL );
        //userInfoSrc = new UserInfoSource();
        checkFirebaseAuth();

    }


    private void checkFirebaseAuth() {

        auth = FirebaseAuth.getInstance();
        //userInfoSrc.setAuth(auth);
       /*
       // after signed-in

        FirebaseUser fUser = userInfoSrc.getAuth().getCurrentUser();
        if ( fUser != null ) {
            // User is signed in
            userInfoSrc.saveUserInfo(fUser);
        } else {
            // No user is signed in
            loadLoginView();
        }
        */
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fUser = firebaseAuth.getCurrentUser();
                if ( fUser != null ) {
                    // User is signed in
                    Log.d(TAG, "onauthStateChanged:signed_in as uid() :" + fUser.getUid() );
                    //newIntent();
                    //userInfo = new UserInfo( user.getDisplayName(),user.getEmail(),
                    // user.getPhotoUrl(), user.getUid() );
                    //userInfoSrc.setUser(userInfo);
                    //userInfoSrc.saveUserInfo(fUser);
                    loadMainView(fUser);


                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    loadLoginView();
                }
            }
        };

        //findViewById(R.id.btn_guestLogin).setOnClickListener(this);
    }


    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }
    public void onStop() {
        super.onStop();
        if ( authListener != null ) {
            auth.removeAuthStateListener(authListener);
        }
    }

    private void showVerificationError() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.add_error_message)
                .setTitle(R.string.add_error_title)
                .setPositiveButton(android.R.string.ok, null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loadLoginView() {
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // clears the activity stack --
        // This prevents the user going back to the main activity
        // when they press the Back button from the login view
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }


    private void loadMainView(FirebaseUser fUser) {
        /*
        try {

            userId = fireDb.getAuth().getUid();

        } catch ( Exception e ) {
            //
            loadLoginView();
        }
        */
        userId = fUser.getUid();
        fireDbChild = new Firebase(Constants.FIREBASE_URL+"/users/"+ userId + "/items" );
        setupListView();
    }

    private void setupListView() {
        // setup listview
        listView = (ListView)findViewById(R.id.listView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,android.R.id.text1 );
        listView.setAdapter(adapter);

        //listView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        final Handler handler = new Handler();
        handler.postDelayed( new Runnable() {

            @Override
            public void run() {
                load();

            }
        }, 1000);

        final EditText todoText =(EditText)findViewById(R.id.todoText);
        todoText.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                return false;
            }
        });

        final Button addButton = (Button)findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String message = todoText.getText().toString().trim();
                if ( message.isEmpty()  ) {
                    showVerificationError();
                } else {
                    saveTodoText(message);
                    todoText.setText("");
                }
            }
        });
    }


    private void load() {

        setFileDbEventListen();

        //listView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

    }


    // users ...
    public void saveTodoText(String message) {

        // users
        //Map<String, Object> map = new HashMap<String, Object>();
        //map.put("title", message);

        fireDbChild.push().child( "title" ).setValue(message);

    }

    private void setFileDbEventListen() {


        fireDbChild.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                adapter.add( (String)dataSnapshot.child("title").getValue() );
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                adapter.remove( (String)dataSnapshot.child("title").getValue() );
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }



    private void newIntent() {

        Intent intent = new Intent( this, Main2Activity.class );
        startActivity(intent);
        finish();
    }

    public boolean onCreateOptionMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_main, menu );
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if ( id == R.id.action_logout ) {
            return true;
        }
        return false;
        // return super.onOptionsItemSelected(item);
    }

    /*
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
    */
}
