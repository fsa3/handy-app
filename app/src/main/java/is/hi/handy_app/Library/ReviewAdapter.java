package is.hi.handy_app.Library;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import is.hi.handy_app.Entities.Review;
import is.hi.handy_app.R;

public class ReviewAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private Context mContext;
    private List<Review> mReviewList;

    public ReviewAdapter(Context context, List<Review> reviewList) {
        mContext = context;
        mReviewList = reviewList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_review, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.review_author.setText(mReviewList.get(position).getAuthor().getName());
        holder.review_input.setText(mReviewList.get(position).getText());

    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }
}
