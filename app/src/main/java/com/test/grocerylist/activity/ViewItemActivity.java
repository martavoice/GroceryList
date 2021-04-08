package com.test.grocerylist.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.grocerylist.R;
import com.test.grocerylist.adapter.HistoryAdapter;
import com.test.grocerylist.model.ItemModel;
import com.test.grocerylist.utils.GroceryItemUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.test.grocerylist.model.ItemModel.ItemStatus.HAVE;
import static com.test.grocerylist.model.ItemModel.ItemStatus.RAN_OUT;

public class ViewItemActivity extends BaseActivity {
    private static final String KEY_ITEM = "itemModel";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.name)
    AppCompatTextView name;
    @BindView(R.id.priority)
    AppCompatTextView priority;
    @BindView(R.id.status)
    AppCompatTextView status;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    private ItemModel itemModel;
    private HistoryAdapter historyAdapter;

    public static void start(@NonNull Context context, ItemModel itemModel) {
        Intent starter = new Intent(context, ViewItemActivity.class);
        starter.putExtra(KEY_ITEM, itemModel);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            itemModel = getIntent().getExtras().getParcelable(KEY_ITEM);
            if (itemModel != null) {
                setupUI();
            } else {
                onBackPressed();
            }
        } else {
            onBackPressed();
        }
    }

    private void setupUI() {
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        name.setText(itemModel.getName());
        priority.setText(getString(R.string.priority, itemModel.getPriority()));
        status.setText(getString(R.string.status, itemModel.getStatus()));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyAdapter = new HistoryAdapter(itemModel.getChangesHistory());
        recyclerView.setAdapter(historyAdapter);
    }

    @OnClick(R.id.btn_change_status)
    void changeStatusClick() {
        String newStatus = itemModel.getStatus().equals(RAN_OUT.getName()) ? HAVE.getName() : RAN_OUT.getName();
        new AlertDialog.Builder(this)
                .setTitle(R.string.are_you_sure)
                .setMessage(getString(R.string.are_you_sure_change_status, newStatus))
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        itemModel.setStatus(newStatus);
                        itemModel = GroceryItemUtils.getInstance().updateStatusOfItem(ViewItemActivity.this.itemModel);
                        status.setText(getString(R.string.status, ViewItemActivity.this.itemModel.getStatus()));
                        if (itemModel != null && historyAdapter != null) {
                            historyAdapter.updateItems(itemModel.getChangesHistory());
                        }
                        Toast.makeText(ViewItemActivity.this, getString(R.string.status_changed, newStatus), Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    @OnClick(R.id.btn_delete)
    void deleteClick() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.are_you_sure)
                .setMessage(R.string.are_you_sure_delete)
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GroceryItemUtils.getInstance().deleteItem(itemModel);
                        finish();
                    }
                })
                .show();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_view_item;
    }
}
