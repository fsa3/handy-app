package is.hi.handy_app.Library;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.Base64;
import java.util.List;

import is.hi.handy_app.Entities.PortfolioItem;
import is.hi.handy_app.R;

public class PortfolioItemAdapter extends ArrayAdapter<PortfolioItem> {

    private Context context;
    private List<PortfolioItem> items;
    private LayoutInflater vi;

    public PortfolioItemAdapter(Context context, List<PortfolioItem> items) {
        super(context,0, items);
        this.context = context;
        this.items = items;
        vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        final PortfolioItem i = items.get(position);
        if (i != null) {
            v = vi.inflate(R.layout.listitem_portfolio_item, null);

            ((TextView)v.findViewById(R.id.portfolioItem_title)).setText(i.getTitle());
            byte[] decodedImage = Base64.getDecoder().decode(i.getStringImage());
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
            ((ImageView)v.findViewById(R.id.portfolioItem_imageview)).setImageBitmap(decodedByte);
            ((TextView)v.findViewById(R.id.portfolioItem_description)).setText(i.getDescription());
            ((TextView)v.findViewById(R.id.portfolioItem_location)).setText(i.getLocation());
        }
        return v;
    }

    public static void setDynamicHeight(ListView listView) {
        ListAdapter gridViewAdapter = listView.getAdapter();
        if (gridViewAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int items = gridViewAdapter.getCount();

        View listItem = gridViewAdapter.getView(0, null, listView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight()+50;

        totalHeight *= items;

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
    }

}