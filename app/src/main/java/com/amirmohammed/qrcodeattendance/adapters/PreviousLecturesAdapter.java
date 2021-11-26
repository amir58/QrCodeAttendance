package com.amirmohammed.qrcodeattendance.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.amirmohammed.qrcodeattendance.R;
import com.amirmohammed.qrcodeattendance.databinding.ItemPreviousLecturesBinding;

import java.util.ArrayList;
import java.util.List;

public class PreviousLecturesAdapter extends RecyclerView.Adapter<PreviousLecturesAdapter.Holder> {

    private List<String> lecturesName = new ArrayList<>();

    public PreviousLecturesAdapter(List<String> lecturesName) {
        this.lecturesName = lecturesName;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                , R.layout.item_previous_lectures, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.binding.setLectureName(lecturesName.get(position));
    }

    @Override
    public int getItemCount() {
        return lecturesName.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        ItemPreviousLecturesBinding binding;
        public Holder(@NonNull ItemPreviousLecturesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
