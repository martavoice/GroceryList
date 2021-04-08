package com.test.grocerylist.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.grocerylist.R;
import com.test.grocerylist.adapter.ItemsAdapter;
import com.test.grocerylist.model.ItemModel;
import com.test.grocerylist.utils.GroceryItemUtils;
import com.test.grocerylist.utils.PrefUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.test.grocerylist.model.ItemModel.ItemStatus.HAVE;
import static com.test.grocerylist.model.ItemModel.ItemStatus.RAN_OUT;

public class GroceryListActivity extends BaseActivity {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ItemsAdapter itemsAdapter;
    private List<ItemModel> itemModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        itemModels = PrefUtils.getInstance().getItems();
        if (itemModels == null) {
            itemModels = new ArrayList<>();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Collections.sort(itemModels, new ItemsComparator());
        itemsAdapter = new ItemsAdapter(itemModels, new ItemsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemModel itemModel) {
                ViewItemActivity.start(GroceryListActivity.this, itemModel);
            }

            @Override
            public void onDeleteItemClick(ItemModel itemModel) {
                new AlertDialog.Builder(GroceryListActivity.this)
                        .setTitle(R.string.are_you_sure)
                        .setMessage(R.string.are_you_sure_delete)
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                itemsAdapter.notifyItemChanged(itemModels.indexOf(itemModel));
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GroceryItemUtils.getInstance().deleteItem(itemModel);
                                itemsAdapter.updateItems();
                            }
                        })
                        .show();
            }

            @Override
            public void onChangeStatusClick(ItemModel itemModel) {
                String newStatus = itemModel.getStatus().equals(RAN_OUT.getName()) ? HAVE.getName() : RAN_OUT.getName();
                new AlertDialog.Builder(GroceryListActivity.this)
                        .setTitle(R.string.are_you_sure)
                        .setMessage(getString(R.string.are_you_sure_change_status, newStatus))
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                itemsAdapter.notifyItemChanged(itemModels.indexOf(itemModel));
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                itemModel.setStatus(newStatus);
                                GroceryItemUtils.getInstance().updateStatusOfItem(itemModel);
                                itemsAdapter.updateItems();
                            }
                        })
                        .show();
            }
        });
        recyclerView.setAdapter(itemsAdapter);
    }

    @OnClick(R.id.btn_add)
    void addClick() {
        AddItemActivity.start(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (itemsAdapter != null) {
            itemsAdapter.updateItems();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter_by_ran_out:
                itemsAdapter.filterItems(GroceryItemUtils.getInstance().getItemFilteredByStatus(RAN_OUT.getName()));
                return true;
            case R.id.action_filter_by_have:
                itemsAdapter.filterItems(GroceryItemUtils.getInstance().getItemFilteredByStatus(HAVE.getName()));
                return true;
            case R.id.action_remove_filters:
                itemsAdapter.filterItems(GroceryItemUtils.getInstance().getItemFilteredByStatus(null));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_grocery_list;
    }

    public static class ItemsComparator implements Comparator<ItemModel> {
        @Override
        public int compare(ItemModel o1, ItemModel o2) {
            if (o2.getPriority() < o1.getPriority()) {
                return 1;
            } else if (o2.getPriority() > o1.getPriority()) {
                return -1;
            } else {
                return o1.getName().compareTo(o2.getName());
            }
        }

    }

}