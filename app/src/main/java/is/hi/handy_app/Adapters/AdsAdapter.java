package is.hi.handy_app.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.Base64;
import java.util.List;

import is.hi.handy_app.Entities.Ad;
import is.hi.handy_app.R;

public class AdsAdapter extends ArrayAdapter<Ad> {

    private final List<Ad> items;
    private final LayoutInflater vi;

    public AdsAdapter(Context context, List<Ad> items) {
        super(context,0, items);
        this.items = items;
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        final Ad i = items.get(position);
        if (i != null) {
            v = vi.inflate(R.layout.cardview_advertisement, null);
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

    public static void setDynamicHeight(GridView gridView) {
        ListAdapter gridViewAdapter = gridView.getAdapter();
        if (gridViewAdapter == null) {
            return;
        }

        int totalHeight;
        int items = gridViewAdapter.getCount();

        View listItem = gridViewAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight()+50;

        if( items > 2 ){
            int rows = (int) Math.ceil(items/2.0);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);
    }

}