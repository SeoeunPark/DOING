package kr.hs.mirim.doing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.CustomViewHolder> {

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
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

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

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.user_name = itemView.findViewById(R.id.user_name);
            this.user_about = itemView.findViewById(R.id.user_about);
            this.user_ing = itemView.findViewById(R.id.user_ing);
            this.user_level = itemView.findViewById(R.id.user_level);
            this.user_condition = itemView.findViewById(R.id.user_condition);
        }
    }
}
