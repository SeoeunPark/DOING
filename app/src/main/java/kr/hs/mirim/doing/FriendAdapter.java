package kr.hs.mirim.doing;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.protobuf.StringValue;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.CustomViewHolder> {
        private static OnItemClickListener listener;
        private int condition_num;
        private ArrayList<MyFriendList> arrayList;
        private Context context;
        private FirebaseAuth auth;
        private String user_id = null;
        private DatabaseReference dbr;
        //어댑터에서 액티비티 액션을 가져올 때 context가 필요한데 어댑터에는 context가 없다.
        //선택한 액티비티에 대한 context를 가져올 때 필요하다.

        public FriendAdapter(ArrayList<MyFriendList> arrayList, Context context) {
            this.arrayList = arrayList;
            this.context = context;
        }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myfriend_list, parent, false);
        final CustomViewHolder holder = new CustomViewHolder(view);
        Dialog myDialog = new Dialog(parent.getContext());
        myDialog.setContentView(R.layout.dialog_contact);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        user_id = user.getUid();
        dbr = FirebaseDatabase.getInstance().getReference("my_friends").child(user_id);
        Button dia_user_delete = (Button)myDialog.findViewById(R.id.dia_user_delete);



        //아라야 삭제버튼 눌렀을 때 삭제되는 거 넣어주랍!! -> 응!!
        dia_user_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("와 갑자기 안돼",arrayList.get(holder.getAdapterPosition()).getUid());
                dbr.orderByChild("code").equalTo(String.valueOf(arrayList.get(holder.getAdapterPosition()).getUid())).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsDel: dataSnapshot.getChildren()) {
                            dsDel.getRef().removeValue();
                            myDialog.dismiss();
                        }
                    }
                });
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView dia_user_img = (ImageView)myDialog.findViewById(R.id.dia_user_img);
                ImageView dia_busy_img = (ImageView)myDialog.findViewById(R.id.dia_busy_img);
                TextView dia_user_name = (TextView)myDialog.findViewById(R.id.dia_user_name);
                TextView dia_user_doing = (TextView)myDialog.findViewById(R.id.dia_user_doing);
                TextView dia_user_about = (TextView)myDialog.findViewById(R.id.dia_user_about);
                int dia_condition_num = arrayList.get(holder.getAdapterPosition()).getCondition();
                int dia_level_num = arrayList.get(holder.getAdapterPosition()).getLevel();
                if(dia_condition_num==1){
                    dia_user_img.setImageResource(R.drawable.emotion1);
                }else if(dia_condition_num==2){
                    dia_user_img.setImageResource(R.drawable.emotion2);
                }else if(dia_condition_num==3){
                    dia_user_img.setImageResource(R.drawable.emotion3);
                }else if(dia_condition_num==4){
                    dia_user_img.setImageResource(R.drawable.emotion4);
                }else if(dia_condition_num==5){
                    dia_user_img.setImageResource(R.drawable.emotion5);
                }else if(dia_condition_num==6){
                    dia_user_img.setImageResource(R.drawable.emotion6);
                }else if(dia_condition_num==7){
                    dia_user_img.setImageResource(R.drawable.emotion7);
                }else{
                    dia_user_img.setImageResource(R.drawable.emotion8);
                }

                if(dia_level_num==0){
                    dia_busy_img.setColorFilter(Color.parseColor("#62FF2A"));
                }else if(dia_level_num==1){
                    dia_busy_img.setColorFilter(Color.parseColor("#FFE32A"));
                }else{
                    dia_busy_img.setColorFilter(Color.parseColor("#FF4D2A"));
                }

                dia_user_doing.setText(arrayList.get(holder.getAdapterPosition()).getIng()+"중이에요");
                dia_user_name.setText(arrayList.get(holder.getAdapterPosition()).getName()+"님은");
                dia_user_about.setText("\""+arrayList.get(holder.getAdapterPosition()).getAbout()+"\"");
                //삭제 버튼 클릭 시
                myDialog.show();
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        int level_num = arrayList.get(position).getLevel();
        condition_num = arrayList.get(position).getCondition();
        //표정
        if(condition_num==1){
            holder.user_img.setImageResource(R.drawable.emotion1);
        }else if(condition_num==2){
            holder.user_img.setImageResource(R.drawable.emotion2);
        }else if(condition_num==3){
            holder.user_img.setImageResource(R.drawable.emotion3);
        }else if(condition_num==4){
            holder.user_img.setImageResource(R.drawable.emotion4);
        }else if(condition_num==5){
            holder.user_img.setImageResource(R.drawable.emotion5);
        }else if(condition_num==6){
            holder.user_img.setImageResource(R.drawable.emotion6);
        }else if(condition_num==7){
            holder.user_img.setImageResource(R.drawable.emotion7);
        }else{
            holder.user_img.setImageResource(R.drawable.emotion8);
        }
        //level
        if(level_num==0){
            holder.user_busy.setColorFilter(Color.parseColor("#62FF2A"));
        }else if(level_num==1){
            holder.user_busy.setColorFilter(Color.parseColor("#FFE32A"));
        }else{
            holder.user_busy.setColorFilter(Color.parseColor("#FF4D2A"));
        }
        holder.user_name.setText(arrayList.get(position).getName()+"님은");
        holder.user_ing.setText(arrayList.get(position).getIng()+"중이에요");
        }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView user_name;
        TextView user_ing;
        ImageView user_img;
        ImageView user_busy;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.user_name = itemView.findViewById(R.id.user_name);
            this.user_img = itemView.findViewById(R.id.profile_img);
            this.user_ing = itemView.findViewById(R.id.user_ing);
            this.user_busy = itemView.findViewById(R.id.user_busy);
        }

    }
    public interface OnItemClickListener {
        void onItemClick(MyFriendList MyFriendList);
    }
    public void setOnItemClickListener(OnItemClickListener listener) { this.listener = listener;  }
}
