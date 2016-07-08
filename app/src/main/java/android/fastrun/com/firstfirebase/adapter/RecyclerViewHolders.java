package android.fastrun.com.firstfirebase.adapter;


import android.fastrun.com.firstfirebase.R;
import android.fastrun.com.firstfirebase.model.Todo;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by admin on 2016-07-08.
 */
public class RecyclerViewHolders extends RecyclerView.ViewHolder {
    private static final String TAG = RecyclerViewHolders.class.getSimpleName();


    public TextView title;
    public TextView content;
    public TextView dateStamp;
    public TextView timeStamp;
    public ImageView deleteIcon;
    private List<Todo> todoList;

    //
    public RecyclerViewHolders(final View itemView,
                               final List<Todo> todoList) {
        super(itemView);

        this.todoList = todoList;

        title = (TextView) itemView.findViewById(R.id.txt_todo_title);
        content = (TextView) itemView.findViewById(R.id.txt_todo_content);

        dateStamp = (TextView) itemView.findViewById(R.id.txt_todo_date);
        timeStamp = (TextView) itemView.findViewById(R.id.txt_todo_time);

        deleteIcon = (ImageView) itemView.findViewById(R.id.btn_todo_delete);

        deleteIcon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Toast
                String title = todoList.get( getAdapterPosition()).getTitle();
                Log.d(TAG, "deleteIcon - Todo Title:" + title);

                // Lod.d
                DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("todo");
                Query qry = dbr.orderByChild("title").equalTo(title);

                qry.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot item : dataSnapshot.getChildren()) {
                            item.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "onCancelled", databaseError.toException());
                    }
                });

            }
        });

    }
}