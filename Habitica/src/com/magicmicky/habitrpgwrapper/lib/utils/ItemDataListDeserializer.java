package com.magicmicky.habitrpgwrapper.lib.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.magicmicky.habitrpgwrapper.lib.models.tasks.ItemData;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.process.ProcessModelInfo;
import com.raizlabs.android.dbflow.runtime.transaction.process.SaveModelTransaction;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemDataListDeserializer implements JsonDeserializer<List<ItemData>> {
    @Override
    public List<ItemData> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        HashMap<String, JsonElement> objects = new HashMap<>();
        if (json.isJsonObject()) {
            for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
                objects.put(entry.getKey(), entry.getValue());
            }
        } else if (json.isJsonArray()) {
            for (JsonElement itemObject : json.getAsJsonArray()) {
                objects.put(itemObject.getAsJsonObject().get("key").getAsString(), itemObject);
            }
        }

        List<ItemData> vals = new ArrayList<>();

        List<ItemData> existingItems = new Select().from(ItemData.class).queryList();

        for (ItemData item : existingItems) {
            if (objects.containsKey(item.key)) {
                JsonElement itemObject = objects.get(item.key);

                if (itemObject.isJsonObject()) {
                    ItemData parsedItem = context.deserialize(itemObject.getAsJsonObject(), ItemData.class);
                    item.text = parsedItem.text;
                    item.value = parsedItem.value;
                    item.type = parsedItem.type;
                    item.klass = parsedItem.klass;
                    item.index = parsedItem.index;
                    item.notes = parsedItem.notes;
                    item.con = parsedItem.con;
                    item.str = parsedItem.str;
                    item.per = parsedItem.per;
                    item._int = parsedItem._int;
                } else {
                    item.owned = itemObject.getAsBoolean();
                }
                vals.add(item);
                objects.remove(item.key);
            }
        }

        for (Map.Entry<String, JsonElement> entry : objects.entrySet()) {
            ItemData item;
            if (entry.getValue().isJsonObject()) {
                item = context.deserialize(entry.getValue().getAsJsonObject(), ItemData.class);
            } else {
                item = new ItemData();
                item.key = entry.getKey();
                item.owned = entry.getValue().getAsBoolean();
            }
            vals.add(item);
        }
        TransactionManager.getInstance().addTransaction(new SaveModelTransaction<>(ProcessModelInfo.withModels(vals)));

        return vals;
    }
}
