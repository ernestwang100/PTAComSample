package com.asuka.ptacomsample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.MyHolder> {
    Context context;
    List<SettingModel> settingModels;

    public SettingAdapter(Context context, List<SettingModel> settingModels) {
        this.context = context;
        this.settingModels = settingModels;
    }

    @NonNull
    @Override
    public SettingAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.setting_row, parent, false);
        return new MyHolder(view);
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

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            settingName = itemView.findViewById(R.id.textView);
        }
    }
}
