package com.habitrpg.android.habitica.ui.adapter.social;

import android.graphics.Color;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.habitrpg.android.habitica.R;
import com.habitrpg.android.habitica.databinding.ValueBarBinding;
import com.habitrpg.android.habitica.ui.AvatarWithBarsViewModel;
import com.habitrpg.android.habitica.ui.helpers.ViewHelper;
import com.habitrpg.android.habitica.userpicture.UserPicture;
import com.magicmicky.habitrpgwrapper.lib.models.Group;
import com.magicmicky.habitrpgwrapper.lib.models.HabitRPGUser;
import com.magicmicky.habitrpgwrapper.lib.models.QuestCollect;
import com.magicmicky.habitrpgwrapper.lib.models.QuestContent;
import com.magicmicky.habitrpgwrapper.lib.models.QuestProgress;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Negue on 22.09.2015.
 */
public class QuestCollectRecyclerViewAdapter extends RecyclerView.Adapter<QuestCollectRecyclerViewAdapter.QuestCollectViewHolder> {

    private ArrayList<String> collect = new ArrayList<>();

    private QuestProgress progress;
    private QuestContent quest;

    public void setQuestProgress(QuestProgress progress) {
        this.progress = progress;
        collect.clear();
        collect.addAll(progress.collect.keySet());
        this.notifyDataSetChanged();
    }

    public void setQuestContent(QuestContent quest){
        this.quest = quest;
        this.notifyDataSetChanged();
    }
    
    @Override
    public QuestCollectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_quest_collect, parent, false);

        return new QuestCollectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestCollectViewHolder holder, int position) {
        holder.bind(collect.get(position));
    }

    @Override
    public int getItemCount() {
        return collect == null ? 0 : collect.size();
    }

    class QuestCollectViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.image)
        ImageView image;

        @Bind(R.id.name)
        TextView name;

        @Bind(R.id.count)
        TextView count;

        View view;

        public QuestCollectViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.view = itemView;
        }

        public void bind(String key) {
            Picasso.with(view.getContext())
                    .load("https://habitica-assets.s3.amazonaws.com/mobileApp/images/" + "quest_" + quest.key + "_" + key + ".png")
                    .into(image);
            name.setText(quest.getCollect().get(key).text);
            count.setText(progress.collect.get(key) + " / " + quest.getCollect().get(key).count);
        }
    }
}