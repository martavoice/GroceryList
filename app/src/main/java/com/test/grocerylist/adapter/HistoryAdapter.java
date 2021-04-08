package com.test.grocerylist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.test.grocerylist.R;
import com.test.grocerylist.model.StatusChangeModel;
import com.test.grocerylist.utils.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<StatusChangeModel> statusChangeModels;

    public HistoryAdapter(List<StatusChangeModel> statusChangeModels) {
        this.statusChangeModels = statusChangeModels;
    }

    public void updateItems(List<StatusChangeModel> statusChangeModels){
        this.statusChangeModels = statusChangeModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(statusChangeModels.get(position));
    }

    @Override
    public int getItemCount() {
        return statusChangeModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.date)
        AppCompatTextView date;
        @BindView(R.id.status_text)
        AppCompatTextView statusText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(StatusChangeModel statusChangeModel) {
            date.setText(DateUtils.getInstance().convertDateToString(statusChangeModel.getDate()));
            statusText.setText(itemView.getContext().getString(R.string.status_changed, statusChangeModel.getStatus()));
        }
    }
}
