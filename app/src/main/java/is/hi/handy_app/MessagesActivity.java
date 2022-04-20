package is.hi.handy_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import is.hi.handy_app.Entities.Message;
import is.hi.handy_app.Adapters.MessageAdapter;
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
    private EditText mMessageField;
    private Button mSendButton;

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
        mMessageField = findViewById(R.id.messages_edit_message);
        mSendButton = findViewById(R.id.messages_send_button);

        mMessagesHeader.setText(getIntent().getStringExtra(EXTRA_USER_FROM_NAME));

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = mMessageField.getText().toString().trim();

                if(content.isEmpty()){
                    mMessageField.setError("Please enter a message");
                    mMessageField.requestFocus();
                    return;
                }

                mMessageService.sendMessage(mUserIdSent, mUserIdReceived, content, new NetworkCallback<Message>() {
                    @Override
                    public void onSuccess(Message result) {
                        getMessages();
                        mMessageField.setText("");
                    }

                    @Override
                    public void onaFailure(String errorString) {
                        Snackbar snackbar = Snackbar.make(mMessagesRecyclerView, "Failed to send message", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
            }
        });

        getMessages();
    }

    private void getMessages() {
        mMessageService.getMessagesBetweenUsers(mUserIdSent, mUserIdReceived, new NetworkCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> result) {
                mMessages = result;
                MessageAdapter mMessageAdapter = new MessageAdapter(MessagesActivity.this, mMessages);
                LinearLayoutManager layoutManager = new LinearLayoutManager(MessagesActivity.this);
                mMessagesRecyclerView.setLayoutManager(layoutManager);
                mMessagesRecyclerView.setAdapter(mMessageAdapter);
                layoutManager.scrollToPosition(mMessages.size() - 1);
            }

            @Override
            public void onaFailure(String errorString) {

            }
        });
    }
}