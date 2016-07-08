package android.fastrun.com.firstfirebase.adapter;

import android.content.Context;
import android.fastrun.com.firstfirebase.R;
import android.fastrun.com.firstfirebase.activity.ChatActivity;
import android.fastrun.com.firstfirebase.model.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016-06-30.
 */
// class : MessageAdapter
public class MessageAdapter extends ArrayAdapter<Message> {

    private Context context;
    private List<Message> messages;
    private String myUserName;

    public MessageAdapter(Context context, int resourceid, ArrayList<Message> objects) {

        super(context, resourceid, objects );

        this.context = context;
        this.messages = objects;
        this.myUserName = ((ChatActivity)context).getUserName();
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        if ( row == null ) {
            LayoutInflater inflater=
                    (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.message_chat, parent, false);
        }
        LinearLayout messageContainer = (LinearLayout)row.findViewById(R.id.message_container );
        Message message = getItem( position );
        TextView msgView =(TextView)row.findViewById(R.id.message_single );

        msgView.setText( message.getMessage() );
        msgView.setBackgroundResource(message.getSender().equals( myUserName )?
                    R.drawable.bubble_right_green : R.drawable.bubble_left_gray );

        messageContainer.setGravity(message.getSender().equals( myUserName )?
                Gravity.RIGHT : Gravity.LEFT );

        return row;

        /*
        converView = super.getView(position, convertView, parent);
        Message message = getItem( position );

        TextView nameView =(TextView)convertView.findViewById(R.id.msg);

        LinearLayout.LayoutParams layoutParams=
                (LinearLayout.LayoutParams)nameView.getLayoutParams();

        int sdk = Build.VERSION.SDK_INT;

        if ( message.getSender().equals( myUserName ) ) {

            // sender
            nameView.setText( " "+ message.getMessage()+" " );
            nameView.setBackgroundResource( R.drawable.bubble_right_green );
            //if ( sdk >= Build.VERSION_CODES.JELLY_BEAN ) { // API 16
            //    nameView.setBackground( getDrawable(R.drawable.bubble_right_green));
            //} else {
            //    nameView.setBackgroundDrawable( getDrawable(R.drawable.bubble_right_green));
            //}
            layoutParams.gravity = Gravity.RIGHT;

        } else {
            // reader
            nameView.setText( " "+ message.getSender() + " : " + message.getMessage() + " " );
            nameView.setBackgroundResource( R.drawable.bubble_left_gray );

            //if ( sdk >= Build.VERSION_CODES.JELLY_BEAN ) {
                //nameView.setBackground( getDrawable(R.drawable.bubble_left_gray));
            //} else {
            //    nameView.setBackgroundDrawable( getDrawable(R.drawable.bubble_left_gray));
            //}

            layoutParams.gravity = Gravity.LEFT;
        }
        nameView.setLayoutParams( layoutParams );
        return convertView;
        */
    }

    public int getCount() {
        return this.messages.size();
    }

    public Message getItem(int index) {
        return this.messages.get(index);
    }
}