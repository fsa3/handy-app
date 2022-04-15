package is.hi.handy_app.Services;

import android.app.DownloadManager;
import android.content.Context;

import java.util.List;

import is.hi.handy_app.Entities.Review;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Networking.NetworkManager;

public class ReviewService {
    private NetworkManager mNetworkManager;
    private Context mContext;

    public ReviewService(Context context){
        mContext = context;
        mNetworkManager = NetworkManager.getInstance(context);
    }

    public void getMyReviews(long userId, NetworkCallback<List<Review>> callback){
      //  mNetworkManager.sendRequest("/myReviews" + userId, );

    }


}
