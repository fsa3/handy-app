package is.hi.handy_app.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import is.hi.handy_app.Entities.HandyUser;
import is.hi.handy_app.R;

public class HandyUserAdapter extends ArrayAdapter<HandyUser> {

    private final List<HandyUser> items;
    private final LayoutInflater vi;

    public HandyUserAdapter(Context context, List<HandyUser> items) {
        super(context,0, items);
        this.items = items;
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        final HandyUser i = items.get(position);
        if (i != null) {
            v = vi.inflate(R.layout.listitem_handyuser, null); //custom xml for desired view
            //do what ever you need to
            ((TextView)v.findViewById(R.id.handylist_text1)).setText(i.getName());
            ((TextView)v.findViewById(R.id.handylist_text2)).setText(i.getTrade().toString());
            ((TextView)v.findViewById(R.id.handylist_rating)).setText(String.valueOf(round(i.getAverageRating(), 1)));
            ((TextView)v.findViewById(R.id.handylist_rate)).setText(String.valueOf(i.getHourlyRate()));

        }
        return v;
    }

    public static double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

}