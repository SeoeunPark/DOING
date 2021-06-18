package kr.hs.mirim.doing;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MessageSend extends Fragment {
    private FirebaseUser currentUser;
    private FirebaseAuth auth;
    FirestoreRecyclerAdapter adapter;
    private RecyclerView sendRv;


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

        Query query = FirebaseFirestore.getInstance().collection("Post").whereEqualTo("sender",current_uid);
        FirestoreRecyclerOptions<MyMessageList> op = new FirestoreRecyclerOptions.Builder<MyMessageList>()
                .setQuery(query, MyMessageList.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<MyMessageList, MessageViewHolder>(op) {
            @NonNull
            @Override
            public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mymessage_list, parent,false);
                return new MessageViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull MyMessageList model) {
                holder.list_name.setText("to. "+model.getSender_name());
                holder.list_gist.setText(model.getGist());
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
        public MessageViewHolder(@NonNull View itemView){
            super(itemView);
            list_name = itemView.findViewById(R.id.name);
            list_gist = itemView.findViewById(R.id.gist);
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