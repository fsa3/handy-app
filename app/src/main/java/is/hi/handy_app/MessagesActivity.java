package is.hi.handy_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import is.hi.handy_app.Entities.Message;
import is.hi.handy_app.Library.MessageAdapter;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Services.MessageService;
import is.hi.handy_app.Services.UserService;

public class MessagesActivity extends AppCompatActivity {
    private static final String EXTRA_USER_FROM = "is.hi.handy_app.user_message_from";
    private static final String EXTRA_USER_FROM_NAME = "is.hi.handy_app.user_message_from_name";

    private MessageService mMessageService;
    private UserService mUserService;

    private List<Message> mMessages;
    private long mUserIdSent;
    private long mUserIdReceived;

    private RecyclerView mMessagesRecyclerView;
    private TextView mMessagesHeader;

    public static Intent newIntent(Context packageContext, long userId, String userName) {
        Intent i = new Intent(packageContext, MessagesActivity.class);
        i.putExtra(EXTRA_USER_FROM, userId);
        i.putExtra(EXTRA_USER_FROM_NAME, userName);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        mMessageService = new MessageService(this);
        mUserService = new UserService(this);

        mUserIdSent = mUserService.getLoggedInUserId();
        mUserIdReceived = getIntent().getLongExtra(EXTRA_USER_FROM, 0);

        mMessagesRecyclerView = findViewById(R.id.messages_recycler_messages_list);
        mMessagesHeader = findViewById(R.id.messages_header);

        mMessagesHeader.setText(getIntent().getStringExtra(EXTRA_USER_FROM_NAME));
        getMessages();
    }

    private void getMessages() {
        mMessageService.getMessagesBetweenUsers(mUserIdSent, mUserIdReceived, new NetworkCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> result) {
                mMessages = result;
                MessageAdapter mMessageAdapter = new MessageAdapter(MessagesActivity.this, mMessages);
                mMessagesRecyclerView.setLayoutManager(new LinearLayoutManager(MessagesActivity.this));
                mMessagesRecyclerView.setAdapter(mMessageAdapter);
            }

            @Override
            public void onaFailure(String errorString) {

            }
        });
    }
}