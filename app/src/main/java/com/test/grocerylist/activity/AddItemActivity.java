package com.test.grocerylist.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

import com.test.grocerylist.R;
import com.test.grocerylist.model.ItemModel;
import com.test.grocerylist.model.StatusChangeModel;
import com.test.grocerylist.utils.GroceryItemUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.test.grocerylist.model.ItemModel.ItemStatus.HAVE;
import static com.test.grocerylist.model.ItemModel.ItemStatus.RAN_OUT;

public class AddItemActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.spinner_priority)
    Spinner spinnerPriority;
    @BindView(R.id.spinner_status)
    Spinner spinnerStatus;
    @BindView(R.id.input_name)
    AppCompatEditText name;

    public static void start(@NonNull Context context) {
        Intent starter = new Intent(context, AddItemActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        String[] priorityItems = {"1", "2", "3", "4", "5"};
        spinnerPriority.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, priorityItems));
        String[] statusItems = {RAN_OUT.getName(), HAVE.getName()};
        spinnerStatus.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, statusItems));

    }

    @OnClick(R.id.btn_create)
    void createClick() {
        hideKeyboard();
        boolean isNameEmpty = TextUtils.isEmpty(name.getText().toString());
        if (!isNameEmpty) {
            String status = (String) spinnerStatus.getSelectedItem();
            int priority = spinnerPriority.getSelectedItemPosition() + 1;
            StatusChangeModel statusChangeModel = new StatusChangeModel(System.currentTimeMillis(), status);
            List<StatusChangeModel> statusChangeModelList = new ArrayList<>();
            statusChangeModelList.add(statusChangeModel);
            ItemModel itemModel = new ItemModel(name.getText().toString(), status, priority, statusChangeModelList);
            GroceryItemUtils.getInstance().addItem(itemModel);
            finish();
        } else {
            Toast.makeText(this, R.string.error_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_add_item;
    }
}
