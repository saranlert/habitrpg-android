package com.magicmicky.habitrpgwrapper.lib.models;

import android.util.Log;

import com.habitrpg.android.habitica.HabitDatabase;
import com.magicmicky.habitrpgwrapper.lib.models.tasks.Task;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

@Table(databaseName = HabitDatabase.NAME)
public class QuestDrop extends BaseModel {
    @Column
    @PrimaryKey
    public String key;

    public List<QuestDropItem> items;

    @Column
    public int gp, exp;

    @Column
    public String unlock;


    @OneToMany(methods = {OneToMany.Method.SAVE, OneToMany.Method.DELETE}, variableName = "items")
    public List<QuestDropItem> getItems() {
        if (items == null || items.isEmpty()) {
            items = new Select()
                    .from(QuestDropItem.class)
                    .where(Condition.column("questKey").eq(key))
                    .queryList();
        }
        return items;
    }

    @Override
    public void save() {
        Log.i("QuestDrop", "saving quest drop "+ key + " " + gp + " " + exp + " " + unlock);
        if(items != null) {
            for (QuestDropItem i : items)
                i.questKey = key;
        }
        super.save();
    }
}

