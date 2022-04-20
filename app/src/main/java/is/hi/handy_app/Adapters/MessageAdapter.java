package is.hi.handy_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import is.hi.handy_app.Entities.Message;
import is.hi.handy_app.R;
import is.hi.handy_app.Services.UserService;

public class MessageAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private final List<Message> mMessageList;

    private final UserService mUserService;

    public MessageAdapter(Context context, List<Message> messageList) {
        mMessageList = messageList;
        mUserService = new UserService(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listitem_messages_from_me, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.listitem_messages_to_me, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = mMessageList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mMessageList.get(position);

        if (message.getSender().getID() == mUserService.getLoggedInUserId()) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    private static class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.messageTo_message_text);
            timeText = itemView.findViewById(R.id.messageTo_timestamp);
            nameText = itemView.findViewById(R.id.messagesTo_sender_name);
            profileImage = itemView.findViewById(R.id.image_gchat_profile_other);
        }

        void bind(Message message) {
            messageText.setText(message.getContent());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTimeString());
            nameText.setText(message.getSender().getName());

            // Insert the profile image from the URL into the ImageView.
            //Utils.displayRoundImageFromUrl(mContext, message.getSender().getProfileUrl(), profileImage);
        }
    }

    private static class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.messageFrom_message_text);
            timeText = itemView.findViewById(R.id.messageFrom_timestamp);
        }

        void bind(Message message) {
            messageText.setText(message.getContent());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getTimeString());
        }
    }
}
