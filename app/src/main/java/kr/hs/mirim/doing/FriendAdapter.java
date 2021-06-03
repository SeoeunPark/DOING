package kr.hs.mirim.doing;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.CustomViewHolder> {
        private static OnItemClickListener listener;
        private int condition_num;
        private ArrayList<MyFriendList> arrayList;
        private Context context;
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

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView dia_user_name = (TextView)myDialog.findViewById(R.id.dia_user_name);
                TextView dia_user_about = (TextView)myDialog.findViewById(R.id.dia_user_about);
                dia_user_name.setText(arrayList.get(holder.getAdapterPosition()).getName());
                dia_user_about.setText(arrayList.get(holder.getAdapterPosition()).getAbout());
                myDialog.show();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        condition_num = arrayList.get(position).getCondition();
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
        holder.user_name.setText(arrayList.get(position).getName());
        holder.user_about.setText(arrayList.get(position).getAbout());
        holder.user_ing.setText(arrayList.get(position).getIng());
        holder.user_level.setText(String.valueOf(arrayList.get(position).getLevel()));
        holder.user_condition.setText(String.valueOf(arrayList.get(position).getCondition()));
        }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView user_name;
        TextView user_about;
        TextView user_ing;
        TextView user_level;
        TextView user_condition;
        ImageView user_img;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.user_name = itemView.findViewById(R.id.user_name);
            this.user_about = itemView.findViewById(R.id.user_about);
            this.user_img = itemView.findViewById(R.id.profile_img);
            this.user_ing = itemView.findViewById(R.id.user_ing);
            this.user_level = itemView.findViewById(R.id.user_level);
            this.user_condition = itemView.findViewById(R.id.user_condition);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int position = getAdapterPosition();
//                    if (listener != null && position != RecyclerView.NO_POSITION) {
//                        listener.onItemClick(arrayList.get(position));
//                    }
//                }
//            });
        }

    }
    public interface OnItemClickListener {
        void onItemClick(MyFriendList MyFriendList);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
