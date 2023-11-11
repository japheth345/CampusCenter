package com.japho.campus.center.chats;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.japho.campus.center.Common.Constants;
import com.japho.campus.center.Common.Extras;
import com.japho.campus.center.Common.NodeNames;
import com.japho.campus.center.Common.Util;
import com.japho.campus.center.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {

    private Context context;
    private View view2;
    private List<ChatListModel> chatListModelList;
    private DatabaseReference mRootRef;
    private FirebaseAuth firebaseAuth;
    private String currentUserId, chatUserId;
    private DatabaseReference databaseReferenceChats;
    public ChatListAdapter(Context context, List<ChatListModel> chatListModelList) {
        this.context = context;
        this.chatListModelList = chatListModelList;
    }


    @NonNull
    @Override
    public ChatListAdapter.ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_list_layout, parent, false);
        return new ChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatListAdapter.ChatListViewHolder holder, int position) {

        final ChatListModel chatListModel = chatListModelList.get(position);

        holder.tvFullName.setText(chatListModel.getUserName());


                Glide.with(context)
                        .load(chatListModel.getPhotoName())
                        .placeholder(R.drawable.default_profile)
                        .error(R.drawable.default_profile)
                        .into(holder.ivProfile);


        String lastMessage  = chatListModel.getLastMessage();
        lastMessage = lastMessage.length()>30?lastMessage.substring(0,30):lastMessage;
        holder.tvLastMessage.setText(lastMessage);

        String lastMessageTime = chatListModel.getLastMessageTime();
        if(lastMessageTime==null) lastMessageTime="";
        if(!TextUtils.isEmpty(lastMessageTime))
            holder.tvLastMessageTime.setText(Util.getTimeAgo(Long.parseLong(lastMessageTime)));


        if(!chatListModel.getUnreadCount().equals("0"))
        {
            holder.tvUnreadCount.setVisibility(View.VISIBLE);
            holder.tvUnreadCount.setText(chatListModel.getUnreadCount());
        }
        else
            holder.tvUnreadCount.setVisibility(View.GONE);

     /*   holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
               view2=view;
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());

                // Setting Dialog Title
                alertDialog.setTitle("Confirm");

                // Setting Dialog Message
                alertDialog.setMessage("Are you user you want to delete this chat? \n Data won't be recovered.");

                // On pressing Settings button
                alertDialog.setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ProgressDialog progressDialog=new ProgressDialog(context);
                                progressDialog.setTitle("Deleting.. ");
                                progressDialog.setMessage(" please wait");
                                progressDialog.show();
                                firebaseAuth = FirebaseAuth.getInstance();
                                mRootRef = FirebaseDatabase.getInstance().getReference();
                                currentUserId = firebaseAuth.getCurrentUser().getUid();
                                databaseReferenceChats= FirebaseDatabase.getInstance().getReference().child(NodeNames.CHATS).child(currentUserId).child(chatListModel.getUserId());
                                databaseReferenceChats.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                      notifyDataSetChanged();
                                      progressDialog.cancel();
                                        Snackbar.make(context,view2,"Chat deleted Successfully",5000).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e)
                                    {
                                        progressDialog.cancel();
                                        Snackbar.make(context,view2,"Error  chat not deleted because \n "+e.getMessage(),5000).show();
                                    }
                                });
                            }
                        });

                // on pressing cancel button
                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.cancel();

                            }
                        });

                // Showing Alert Message
                alertDialog.show();


            }
            });
*/
        holder.llChatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra(Extras.USER_KEY, chatListModel.getUserId());
                intent.putExtra(Extras.USER_NAME, chatListModel.getUserName());
                intent.putExtra(Extras.PHOTO_NAME, chatListModel.getPhotoName());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return chatListModelList.size();
    }

    public class ChatListViewHolder  extends  RecyclerView.ViewHolder{

        private LinearLayout llChatList;
        private TextView tvFullName, tvLastMessage, tvLastMessageTime, tvUnreadCount;
        private ImageView ivProfile;
        //private ImageButton delete;

        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);

            llChatList = itemView.findViewById(R.id.llChatList);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            tvLastMessageTime = itemView.findViewById(R.id.tvLastMessageTime);
            tvUnreadCount= itemView.findViewById(R.id.tvUnreadCount);
            ivProfile = itemView.findViewById(R.id.ivProfile);
           /// delete=itemView.findViewById(R.id.delete);
        }
    }
}
