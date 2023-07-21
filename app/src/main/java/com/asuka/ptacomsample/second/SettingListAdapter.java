package com.asuka.ptacomsample.second;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asuka.ptacomsample.R;

import java.util.List;

public class SettingListAdapter extends RecyclerView.Adapter<SettingListAdapter.MyHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    List<SettingListModel> settingListModels;

    public SettingListAdapter(Context context, List<SettingListModel> settingListModels, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.settingListModels = settingListModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public SettingListAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.setting_row, parent, false);
        return new MyHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingListAdapter.MyHolder holder, int position) {
        holder.settingName.setText(settingListModels.get(position).getSettingName());
        holder.settingImage.setImageResource(settingListModels.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return settingListModels.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        ImageView settingImage;
        TextView settingName;

        public MyHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            settingName = itemView.findViewById(R.id.textView);
            settingImage = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            recyclerViewInterface. onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
