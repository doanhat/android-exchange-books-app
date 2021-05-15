package com.example.donpoly.api;

import com.example.donpoly.data.model.Message;
import com.example.donpoly.data.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

public class MessageHelper {

    private static final String COLLECTION_NAME = "messages";

    // --- GET ---

    public static Query getAllMessageForChat(String chat){
        return ChatHelper.getChatCollection().document(chat).collection(COLLECTION_NAME).orderBy("dateCreated").limit(1000);
    }
    //--- NEW ---
    public static Task<DocumentReference> createMessageForChat(String textMessage, String chatid, User userSender){

        // 1 - Create the Message object
        Message message = new Message(chatid,textMessage, userSender);
        //todo: reference chatid
        // 2 - Store Message to Firestore
        return ChatHelper.getChatCollection()
                .document(chatid)
                .collection(COLLECTION_NAME)
                .add(message);
    }
}