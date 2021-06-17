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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageReceive extends Fragment {
    private FirebaseUser currentUser;
    private FirebaseAuth auth;
    FirestoreRecyclerAdapter adapter;
    private RecyclerView receiveRv;

    public MessageReceive() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message_receive, container, false);
        receiveRv = v.findViewById(R.id.receiveRv);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = FirebaseAuth.getInstance().getUid();

        Query query = FirebaseFirestore.getInstance().collection("Post").whereEqualTo("receiver",current_uid);
        FirestoreRecyclerOptions<MyMessageList> op = new FirestoreRecyclerOptions.Builder<MyMessageList>()
                .setQuery(query, MyMessageList.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<MyMessageList, MessageReceive.MessageViewHolder>(op) {
            @NonNull
            @Override
            public MessageReceive.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mymessage_list, parent,false);
                return new MessageReceive.MessageViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MessageReceive.MessageViewHolder holder, int position, @NonNull MyMessageList model) {
                holder.list_name.setText("from. "+model.getReceiver_name());
                holder.list_gist.setText(model.getGist());
            }
        };
        receiveRv.setHasFixedSize(true);
        receiveRv.setLayoutManager(new LinearLayoutManager(getContext()));
        receiveRv.setAdapter(adapter);

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