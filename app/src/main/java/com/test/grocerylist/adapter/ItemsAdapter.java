package com.test.grocerylist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.grocerylist.R;
import com.test.grocerylist.model.ItemModel;
import com.test.grocerylist.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    private List<ItemModel> itemModels;
    private OnItemClickListener listener;

    public ItemsAdapter(List<ItemModel> itemModels, OnItemClickListener listener) {
        this.itemModels = itemModels != null ? itemModels : new ArrayList<>();
        this.listener = listener;
    }

    public void updateItems() {
        itemModels = PrefUtils.getInstance().getItems();
        notifyDataSetChanged();
    }

    public void filterItems(List<ItemModel> itemModels) {
        this.itemModels = itemModels;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grocery, parent, false);
        return new ItemsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(itemModels.get(position));
    }

    @Override
    public int getItemCount() {
        return itemModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        AppCompatTextView name;
        @BindView(R.id.priority)
        AppCompatTextView priority;
        @BindView(R.id.status)
        AppCompatTextView status;
        @BindView(R.id.layout_change_status)
        ViewGroup layoutChangeStatus;
        @BindView(R.id.layout_delete)
        ViewGroup layoutDelete;
        @BindView(R.id.btn_show_hide_history)
        AppCompatTextView btnShowHideHistory;
        @BindView(R.id.recyclerview)
        RecyclerView recyclerView;
        private ItemModel itemModel;
        private boolean isHistoryVisible;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(ItemModel itemModel) {
            this.itemModel = itemModel;

            name.setText(itemModel.getName());
            priority.setText(itemView.getContext().getString(R.string.priority, itemModel.getPriority()));
            status.setText(itemView.getContext().getString(R.string.status, itemModel.getStatus()));

            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            recyclerView.setAdapter(new HistoryAdapter(itemModel.getChangesHistory()));
            recyclerView.setVisibility(View.GONE);
            btnShowHideHistory.setText(R.string.show_history);
        }

        @OnClick(R.id.layout_main)
        void itemClick() {
            listener.onItemClick(itemModel);
        }

        @OnClick(R.id.btn_show_hide_history)
        void showHideHistory() {
            if (isHistoryVisible) {
                recyclerView.setVisibility(View.GONE);
                btnShowHideHistory.setText(R.string.show_history);
                isHistoryVisible = false;
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                btnShowHideHistory.setText(R.string.hide_history);
                isHistoryVisible = true;
            }
        }

        @OnClick(R.id.layout_delete)
        void deleteClick() {
            listener.onDeleteItemClick(itemModel);
        }

        @OnClick(R.id.layout_change_status)
        void changeStatusClick() {
            listener.onChangeStatusClick(itemModel);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ItemModel itemModel);

        void onDeleteItemClick(ItemModel itemModel);

        void onChangeStatusClick(ItemModel itemModel);
    }
}
