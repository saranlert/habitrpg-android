package com.habitrpg.android.habitica.ui.adapter.social;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.habitrpg.android.habitica.R;
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

    public void setGroup(Group group) {
        this.group = group;
        if (memberList == null) memberList = new ArrayList<>();
        memberList.clear();
        if (group.quest != null && group.quest.members != null) {
            for (HabitRPGUser member : group.members) {
                if (group.quest.members.containsKey(member.getId()))
                    memberList.add(member);
            }
            this.notifyDataSetChanged();
        }
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

            Boolean questResponse = group.quest.members.get(user.getId());
            if (group.quest.active) {
                this.questResponse.setText("");
            } else if (questResponse == null) {
                this.questResponse.setText("Pending");
            } else if (questResponse) {
                this.questResponse.setText("Accepted");
                this.questResponse.setTextColor(Color.parseColor("#2db200"));
            } else {
                this.questResponse.setText("Rejected");
                this.questResponse.setTextColor(Color.parseColor("#b30409"));
            }
        }
    }
}
