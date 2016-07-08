package android.fastrun.com.firstfirebase.activity;

import android.content.Intent;
import android.fastrun.com.firstfirebase.R;
import android.fastrun.com.firstfirebase.adapter.RecyclerViewAdapter;
import android.fastrun.com.firstfirebase.model.Todo;
import android.fastrun.com.firstfirebase.model.UserInfoSource;
import android.fastrun.com.firstfirebase.model.Constants;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

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
    private ArrayList<Todo> allTodo;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference dbr;
    private RecyclerViewAdapter recyclerViewAdatper;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // getWin
        // dow().requestFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        checkFirebaseAuth();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //
        getMenuInflater().inflate(R.menu.menu_main, menu);;
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if ( id == R.id.action_logout ) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkFirebaseAuth() {

        fireDb = new Firebase( Constants.FIREBASE_URL );
        //userInfoSrc = new UserInfoSource();
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
                    try {
                        loadMainView(fUser);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


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


    private void loadMainView(FirebaseUser fUser) throws Exception {

        //getSupportActionBar().show();
        /*
        try {

            userId = fireDb.getAuth().getUid();

        } catch ( Exception e ) {
            //
            loadLoginView();
        }
        */
        userId = fUser.getUid();
        //fireDbChild = new Firebase(Constants.FIREBASE_URL+"/users/"+ userId + "/todo" );
        dbr = FirebaseDatabase.getInstance().getReference("/users/"+ userId + "/todo");

        setupListViewNew();
    }

    private void setupListViewNew() {
        allTodo = new ArrayList<Todo>();
        recyclerView = (RecyclerView)findViewById(R.id.todo_list);
        linearLayoutManager = new LinearLayoutManager(this);

        fab = (FloatingActionButton)findViewById(R.id.fab);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        //progressBar.setVisibility(View.VISIBLE);

        final Handler handler = new Handler();
        setupDbr();

        handler.postDelayed( new Runnable() {

            @Override
            public void run() {
                //load();
                progressBar.setVisibility(View.GONE);
            }
        }, 1000);

    }

    private void setupDbr() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadEditView();
            }
        });

        dbr.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded( DataSnapshot dataSnapshot, String s) {
                getAllTodo(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                getAllTodo(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                todoDeletion(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void loadEditView() {

        Intent i = new Intent(this, EditActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // clears the activity stack --
        // This prevents the user going back to the main activity
        // when they press the Back button from the login view
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

    }


    private void setupListViewOld() {
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
                //load();

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


    private void loadOld() {

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

//
//        fireDbChild.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                adapter.add( (String)dataSnapshot.child("title").getValue() );
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                adapter.remove( (String)dataSnapshot.child("title").getValue() );
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
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

    private void getAllTodo(DataSnapshot dataSnapshot) {

        Long tsLong = null;
        String sTitle = null;
        for(DataSnapshot item : dataSnapshot.getChildren() ) {

            String key = item.getKey();
            Log.d(TAG,"getKey :" + key );
            //Todo todo = item.getValue(Todo.class);
            //Todo todo = item.child(key).getValue(Todo.class);
            if ( key.equals("timeStamp") ) tsLong = item.getValue(Long.class);
            if ( key.equals("title") ) sTitle = item.getValue(String.class);
        }
        Log.d(TAG,"sTitle :" + sTitle + ", tsLong :" + tsLong);
        allTodo.add( new Todo(sTitle) );
        recyclerViewAdatper = new RecyclerViewAdapter(MainActivity.this, allTodo);
        recyclerView.setAdapter(recyclerViewAdatper);
        recyclerView.scrollToPosition(allTodo.size());
    }

    private void todoDeletion(DataSnapshot dataSnapshot) {
        for(DataSnapshot item : dataSnapshot.getChildren() ) {
            String key = item.getKey();
            Log.d(TAG,"getKey :" + key );
            if ( key.equals("title") ) {
                String title = item.getValue(String.class);
                for( int i=0; i < allTodo.size(); i++ ) {
                    if( allTodo.get(i).getTitle().equals(title) ) {
                        allTodo.remove(i);
                    }
                }
                Log.d(TAG,"delettion title :" + title );
            }
            recyclerViewAdatper.notifyDataSetChanged();
            recyclerViewAdatper = new RecyclerViewAdapter(MainActivity.this, allTodo);

            recyclerView.setAdapter(recyclerViewAdatper);
            recyclerView.scrollToPosition(allTodo.size());
        }
    }
}
