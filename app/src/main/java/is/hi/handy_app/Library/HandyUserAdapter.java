package is.hi.handy_app.Library;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import is.hi.handy_app.Entities.HandyUser;
import is.hi.handy_app.R;

public class HandyUserAdapter extends ArrayAdapter<HandyUser> {

    private Context context;
    private List<HandyUser> items;
    private LayoutInflater vi;

    public HandyUserAdapter(Context context, List<HandyUser> items) {
        super(context,0, items);
        this.context = context;
        this.items = items;
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        final HandyUser i = items.get(position);
        if (i != null) {
            v = vi.inflate(R.layout.listitem_handyuser, null); //custom xml for desired view
            //do what ever you need to
            ((TextView)v.findViewById(R.id.handylist_text1)).setText(i.getName());
            ((TextView)v.findViewById(R.id.handylist_text2)).setText(i.getTrade().toString());
            ((TextView)v.findViewById(R.id.handylist_rating)).setText(String.valueOf(i.getAverageRating()));
            ((TextView)v.findViewById(R.id.handylist_rate)).setText(String.valueOf(i.getHourlyRate()));

        }
        return v;
    }

}