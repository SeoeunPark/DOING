package kr.hs.mirim.doing;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.database.FirebaseDatabase;
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
    private ArrayList<MyMessageList> arrayList;
    private Context context;

    public MessageReceive(ArrayList<MyMessageList> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }
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
                final MessageReceive.MessageViewHolder holder = new MessageReceive.MessageViewHolder(view);
                auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                Dialog myDialog = new Dialog(parent.getContext());
                myDialog.setContentView(R.layout.dialog_message);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView message_gist = (TextView)myDialog.findViewById(R.id.message_gist);
                        TextView message_name = (TextView)myDialog.findViewById(R.id.message_name);
                        TextView message_time = (TextView)myDialog.findViewById(R.id.message_time);
                        TextView message_contents = (TextView)myDialog.findViewById(R.id.message_contents);

                        message_gist.setText(arrayList.get(holder.getAdapterPosition()).getGist());
                        message_name.setText("from. "+arrayList.get(holder.getAdapterPosition()).getSender_name());
                        message_time.setText(arrayList.get(holder.getAdapterPosition()).getTime());
                        message_contents.setText(arrayList.get(holder.getAdapterPosition()).getContent());

                        myDialog.show();
                        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    }
                });


                return new MessageReceive.MessageViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MessageReceive.MessageViewHolder holder, int position, @NonNull MyMessageList model) {
                holder.list_name.setText("from. "+model.getReceiver_name());
                holder.list_gist.setText(model.getGist());
                if(!model.isRead()){
                    holder.background.setBackgroundColor(getContext().getResources().getColor(R.color.message));
                }
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
        private ConstraintLayout background;
        public MessageViewHolder(@NonNull View itemView){
            super(itemView);

            list_name = itemView.findViewById(R.id.name);
            list_gist = itemView.findViewById(R.id.gist);
            background = itemView.findViewById(R.id.back);
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