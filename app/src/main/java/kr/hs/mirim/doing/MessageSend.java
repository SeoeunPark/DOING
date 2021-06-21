package kr.hs.mirim.doing;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MessageSend extends Fragment {
    private FirebaseUser currentUser;
    private FirebaseAuth auth;
    FirestoreRecyclerAdapter adapter;
    private RecyclerView sendRv;
    private View view;


    public MessageSend() {

    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message_send, container, false);
        sendRv = v.findViewById(R.id.sendRv);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = FirebaseAuth.getInstance().getUid();

        Query query = FirebaseFirestore.getInstance().collection("Post").whereEqualTo("sender",current_uid).orderBy("time", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<MyMessageList> op = new FirestoreRecyclerOptions.Builder<MyMessageList>()
                .setQuery(query, MyMessageList.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<MyMessageList, MessageViewHolder>(op) {
            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mymessage_list, parent,false);
                return new MessageViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull MyMessageList model) {
                holder.list_name.setText("To. "+model.getReceiver_name());
                holder.list_gist.setText(model.getGist());
                holder.list_contents.setText(model.getContent());
                holder.list_time.setText(model.getTime());

                Dialog myDialog = new Dialog(getContext());
                myDialog.setContentView(R.layout.dialog_message);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView message_gist = (TextView)myDialog.findViewById(R.id.message_gist);
                        TextView message_name = (TextView)myDialog.findViewById(R.id.message_name);
                        TextView message_time = (TextView)myDialog.findViewById(R.id.message_time);
                        TextView message_contents = (TextView)myDialog.findViewById(R.id.message_contents);
                        String read = "";
                        if(model.isRead()) read = " (읽음)";
                        else read = " (읽지 않음)";
                        message_gist.setText(model.getGist());
                        message_name.setText("To. "+model.getReceiver_name()+read);
                        message_time.setText(model.getTime());
                        message_contents.setText(model.getContent());

                        myDialog.show();
                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    }
                });

            }
        };

        sendRv.setHasFixedSize(true);
        sendRv.setLayoutManager(new LinearLayoutManager(getContext()));
        sendRv.setAdapter(adapter);
        return v;
    }
    private class MessageViewHolder extends RecyclerView.ViewHolder {
        private TextView list_name;
        private TextView list_gist;
        private TextView list_contents;
        private TextView list_time;
        public MessageViewHolder(@NonNull View itemView){
            super(itemView);
            list_name = itemView.findViewById(R.id.name);
            list_gist = itemView.findViewById(R.id.gist);
            list_contents = itemView.findViewById(R.id.contents);
            list_time = itemView.findViewById(R.id.time);
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();

    }

}