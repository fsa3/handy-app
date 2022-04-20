package is.hi.handy_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import is.hi.handy_app.Entities.Review;
import is.hi.handy_app.R;

public class ReviewAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    private final Context mContext;
    private final List<Review> mReviewList;
    private final boolean mShowHandyman;

    public ReviewAdapter(Context context, List<Review> reviewList, boolean showHandyman) {
        mContext = context;
        mReviewList = reviewList;
        mShowHandyman = showHandyman;
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
        holder.review_rating.setText(String.valueOf(mReviewList.get(position).getRating()));

        if (mShowHandyman) {
            holder.review_author.setText(mReviewList.get(position).getHandyman().getName());
        }
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }
}
