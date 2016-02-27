package com.habitrpg.android.habitica.ui.adapter.social;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.habitrpg.android.habitica.R;
import com.habitrpg.android.habitica.databinding.ValueBarBinding;
import com.habitrpg.android.habitica.ui.AvatarWithBarsViewModel;
import com.habitrpg.android.habitica.ui.helpers.ViewHelper;
import com.habitrpg.android.habitica.userpicture.UserPicture;
import com.magicmicky.habitrpgwrapper.lib.models.Group;
import com.magicmicky.habitrpgwrapper.lib.models.HabitRPGUser;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Negue on 22.09.2015.
 */
public class QuestMemberRecyclerViewAdapter extends RecyclerView.Adapter<QuestMemberRecyclerViewAdapter.MemberViewHolder> {


    private ArrayList<HabitRPGUser> memberList;
    private Group group;

    public void setGroup(Group group){
        this.group = group;
        if(memberList==null)memberList = new ArrayList<>();
        memberList.clear();
        for(HabitRPGUser member:group.members){
            if(group.quest.members.containsKey(member.getId()))
                memberList.add(member);
        }
        this.notifyDataSetChanged();
    }


    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.party_member_quest, parent, false);

        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MemberViewHolder holder, int position) {
        holder.bind(memberList.get(position));
    }

    @Override
    public int getItemCount() {
        return memberList == null ? 0 : memberList.size();
    }

    class MemberViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.username)
        TextView userName;

        @Bind(R.id.rsvpneeded)
        TextView questResponse;

        public MemberViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }

        public void bind(HabitRPGUser user) {
            if (group.quest.leader.equals(user.getId()))
                userName.setText("* " + user.getProfile().getName());
            else
                userName.setText(user.getProfile().getName());

            Boolean questresponse = group.quest.members.get(user.getId());
            if (group.quest.active) {
                questResponse.setText("");
            } else if (questresponse == null) {
                questResponse.setText("Pending");
            } else if (questresponse.booleanValue() == true) {
                questResponse.setText("Accepted");
                questResponse.setTextColor(Color.parseColor("#2db200"));
            } else if (questresponse.booleanValue() == false) {
                questResponse.setText("Rejected");
                questResponse.setTextColor(Color.parseColor("#b30409"));
            }
        }
    }
}
