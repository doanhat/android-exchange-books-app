package com.example.donpoly.ui.messages;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.OnClick;
import com.example.donpoly.R;
import com.example.donpoly.ui.messages.BaseActivity;
import com.example.donpoly.api.MessageHelper;
import com.example.donpoly.data.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.annotations.Nullable;

public class ChatActivity extends BaseActivity implements ChatAdapter.Listener{
    //TODO:associer chat
    // FOR DESIGN
    // 1 - Getting all views needed
    @BindView(R.id.activity_chat_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.activity_chat_text_view_recycler_view_empty) TextView textViewRecyclerViewEmpty;
    @BindView(R.id.activity_chat_message_edit_text) TextInputEditText editTextMessage;
    @BindView(R.id.activity_chat_image_chosen_preview) ImageView imageViewPreview;
    // FOR DATA
    // 2 - Declaring Adapter and data
    private ChatAdapter mentorChatAdapter;
    @Nullable private User modelCurrentUser;
    private String currentChatName;
    // --------------------
    // ACTIONS
    // --------------------

    @OnClick(R.id.activity_chat_send_button)
    public void onClickSendMessage() {
        // 1 - Check if text field is not empty and current user properly downloaded from Firestore
        if (!TextUtils.isEmpty(editTextMessage.getText()) && modelCurrentUser != null){
            // 2 - Create a new Message to Firestore
            MessageHelper.createMessageForChat(editTextMessage.getText().toString(), this.currentChatName, modelCurrentUser).addOnFailureListener(this.onFailureListener());
            // 3 - Reset text field
            this.editTextMessage.setText("");
        }
    }
}
