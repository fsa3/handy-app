package is.hi.handy_app.Networking;

public interface NetworkCallback<T> {

    void onSuccess(T result);

    void onaFailure(String errorString);
}
