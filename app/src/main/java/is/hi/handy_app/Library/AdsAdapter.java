package is.hi.handy_app.Library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Base64;
import java.util.List;

import is.hi.handy_app.Entities.Ad;
import is.hi.handy_app.Entities.HandyUser;
import is.hi.handy_app.R;

public class AdsAdapter extends ArrayAdapter<Ad> {

    private Context context;
    private List<Ad> items;
    private LayoutInflater vi;

    public AdsAdapter(Context context, List<Ad> items) {
        super(context,0, items);
        this.context = context;
        this.items = items;
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        final Ad i = items.get(position);
        if (i != null) {
            v = vi.inflate(R.layout.cardview_advertisement, null); //custom xml for desired view
            //do what ever you need to
            ((TextView)v.findViewById(R.id.adcard_title)).setText(i.getTitle());
            byte[] decodedImage = Base64.getDecoder().decode(i.getStringImage());
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
            ((ImageView)v.findViewById(R.id.adcard_imageview)).setImageBitmap(decodedByte);
            ((TextView)v.findViewById(R.id.adcard_trade)).setText(i.getTrade().toString());
            ((TextView)v.findViewById(R.id.adcard_location)).setText(i.getLocation());
            ((TextView)v.findViewById(R.id.adcard_date)).setText(i.getFormattedDate());

        }
        return v;
    }

}