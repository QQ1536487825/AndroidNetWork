package com.example.androidnetwork.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidnetwork.R;
import com.example.androidnetwork.domain.GetTextItem;

import java.util.ArrayList;
import java.util.List;

public class GetresultListAdapter extends RecyclerView.Adapter<GetresultListAdapter.InnerHolder> {
    private List<GetTextItem.DataBean> data = new ArrayList<>();
    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_get_text, parent, false);


        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        View itemView = holder.itemView;
        TextView title = itemView.findViewById(R.id.item_title);
        ImageView item_iv = itemView.findViewById(R.id.item_iv);
        Glide.with(item_iv.getContext()).load("http://192.168.0.103:9102"+data.get(position).getCover()).into(item_iv);
        GetTextItem.DataBean dataBean = data.get(position);
        title.setText(dataBean.getTitle());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(GetTextItem getTextItem) {
        data.clear();
        data.addAll(getTextItem.getData());
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder{
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
