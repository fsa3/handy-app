package is.hi.handy_app.Library;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import is.hi.handy_app.R;

public class CustomViewHolder extends RecyclerView.ViewHolder{
    public TextView review_author,review_input, review_rating;
    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);
        review_author= itemView.findViewById(R.id.review_author);
        review_input= itemView.findViewById(R.id.review_input);
        review_rating = itemView.findViewById(R.id.review_rating);
    }
}
