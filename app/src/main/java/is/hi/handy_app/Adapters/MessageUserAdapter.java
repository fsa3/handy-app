package is.hi.handy_app.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import is.hi.handy_app.Entities.User;
import is.hi.handy_app.R;

public class MessageUserAdapter extends ArrayAdapter<User> {

    private final List<User> items;
    private final LayoutInflater vi;

    public MessageUserAdapter(Context context, List<User> items) {
        super(context,0, items);
        this.items = items;
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        final User i = items.get(position);
        if (i != null) {
            v = vi.inflate(R.layout.listitem_message_user, null);
            ((TextView)v.findViewById(R.id.messageUser_name)).setText(i.getName());
        }
        return v;
    }

}