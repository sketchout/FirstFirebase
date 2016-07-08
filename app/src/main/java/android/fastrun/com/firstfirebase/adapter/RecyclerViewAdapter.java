package android.fastrun.com.firstfirebase.adapter;

import android.content.Context;
import android.fastrun.com.firstfirebase.R;
import android.fastrun.com.firstfirebase.model.Todo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2016-07-08.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    private Context context;
    private List<Todo> todoList;

    public RecyclerViewAdapter(Context context, List<Todo> todList ) {
        this.context = context;
        this.todoList = todoList;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolders viewHolder = null;

        View layoutView = LayoutInflater.from( parent.getContext())
                .inflate(R.layout.todo_item, parent, false);
        viewHolder = new RecyclerViewHolders(layoutView, todoList);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {

        holder.title.setText( todoList.get(position).getTitle().substring(1,10) );
        holder.content.setText ( todoList.get(position).getTitle().substring(11) );

        String dateText = dfDate.format ( new Date( todoList.get(position).getTimeStamp() ) );
        holder.dateStamp.setText ( dateText );

        String timeText = dfTime.format ( new Date( todoList.get(position).getTimeStamp() ) );
        holder.timeStamp.setText ( timeText );

    }

    @Override
    public int getItemCount() {
        return this.todoList.size();
    }

    final private DateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy");
    final private DateFormat dfTime = new SimpleDateFormat("HH:mm");
}
