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

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.MyHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    List<SettingModel> settingModels;

    public SettingAdapter(Context context, List<SettingModel> settingModels, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.settingModels = settingModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public SettingAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.setting_row, parent, false);
        return new MyHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingAdapter.MyHolder holder, int position) {
        holder.settingName.setText(settingModels.get(position).getSettingName());
    }

    @Override
    public int getItemCount() {
        return settingModels.size();
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        ImageView settingImage;
        TextView settingName;

        public MyHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            settingName = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
