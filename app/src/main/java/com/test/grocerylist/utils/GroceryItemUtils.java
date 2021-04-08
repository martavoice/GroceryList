package com.test.grocerylist.utils;

import com.test.grocerylist.activity.GroceryListActivity;
import com.test.grocerylist.model.ItemModel;
import com.test.grocerylist.model.StatusChangeModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroceryItemUtils {
    private static GroceryItemUtils instance;

    private GroceryItemUtils() {
    }

    public static GroceryItemUtils getInstance() {
        if (instance == null) {
            instance = new GroceryItemUtils();
        }
        return instance;
    }

    public void addItem(ItemModel itemModel) {
        List<ItemModel> itemModels = PrefUtils.getInstance().getItems();
        itemModels.add(itemModel);
        Collections.sort(itemModels, new GroceryListActivity.ItemsComparator());
        PrefUtils.getInstance().saveItems(itemModels);
    }

    public void deleteItem(ItemModel itemModel) {
        List<ItemModel> itemModels = PrefUtils.getInstance().getItems();
        int positionToRemove = -1;
        for (ItemModel item : itemModels) {
            if (item.getName().equals(itemModel.getName())) {
                positionToRemove = itemModels.indexOf(item);
            }
        }

        if (positionToRemove != -1) {
            itemModels.remove(positionToRemove);
        }
        Collections.sort(itemModels, new GroceryListActivity.ItemsComparator());

        PrefUtils.getInstance().saveItems(itemModels);
    }

    public ItemModel updateStatusOfItem(ItemModel itemModel) {
        List<ItemModel> itemModels = PrefUtils.getInstance().getItems();
        ItemModel updatedItem = null;
        for (ItemModel item : itemModels) {
            if (item.getName().equals(itemModel.getName())) {
                item.setStatus(itemModel.getStatus());
                List<StatusChangeModel> statusChangeModelList = itemModel.getChangesHistory();
                StatusChangeModel statusChangeModel = new StatusChangeModel(System.currentTimeMillis(), item.getStatus());
                statusChangeModelList.add(statusChangeModel);
                item.setChangesHistory(statusChangeModelList);
                updatedItem = item;
            }
        }
        Collections.sort(itemModels, new GroceryListActivity.ItemsComparator());
        PrefUtils.getInstance().saveItems(itemModels);
        return updatedItem;
    }

    public List<ItemModel> getItemFilteredByStatus(String status) {
        List<ItemModel> filteredModels = new ArrayList<>();
        List<ItemModel> allModels = PrefUtils.getInstance().getItems();
        if (status != null) {
            for (ItemModel itemModel : allModels) {
                if (itemModel.getStatus().equals(status)) {
                    filteredModels.add(itemModel);
                }
            }
            Collections.sort(filteredModels, new GroceryListActivity.ItemsComparator());
            return filteredModels;
        } else {
            Collections.sort(allModels, new GroceryListActivity.ItemsComparator());
            return allModels;
        }
    }
}
