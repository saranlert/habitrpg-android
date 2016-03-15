package com.habitrpg.android.habitica.ui.fragments.social;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.habitrpg.android.habitica.APIHelper;
import com.habitrpg.android.habitica.R;
import com.habitrpg.android.habitica.databinding.FragmentGroupInfoBinding;
import com.habitrpg.android.habitica.databinding.ValueBarBinding;
import com.habitrpg.android.habitica.ui.DividerItemDecoration;
import com.habitrpg.android.habitica.ui.adapter.PartyMemberQuestRecyclerViewAdapter;
import com.habitrpg.android.habitica.ui.adapter.social.QuestCollectRecyclerViewAdapter;
import com.magicmicky.habitrpgwrapper.lib.models.Group;
import com.magicmicky.habitrpgwrapper.lib.models.HabitRPGUser;
import com.magicmicky.habitrpgwrapper.lib.models.QuestContent;
import com.magicmicky.habitrpgwrapper.lib.models.QuestDropItem;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Negue on 16.09.2015.
 */
public class GroupInformationFragment extends Fragment {


    private View view;
    FragmentGroupInfoBinding viewBinding;
    APIHelper mAPIHelper;
    @Bind(R.id.questMemberView)
    RecyclerView questMemberView;
    @Bind(R.id.collectionStats)
    RecyclerView collectionStats;
    @Bind(R.id.questDrop)
    LinearLayout questDrop;
    private Group group;
    private HabitRPGUser user;
    private QuestContent quest;
    private ValueBarBinding bossHpBar;
    private ValueBarBinding bossRageBar;

    private PartyMemberQuestRecyclerViewAdapter participantViewAdapter;
    private QuestCollectRecyclerViewAdapter questCollectViewAdapter;

    public static GroupInformationFragment newInstance(Group group, HabitRPGUser user, APIHelper mAPIHelper) {

        Bundle args = new Bundle();

        GroupInformationFragment fragment = new GroupInformationFragment();
        fragment.setArguments(args);
        fragment.group = group;
        fragment.user = user;
        fragment.mAPIHelper = mAPIHelper;
        return fragment;
    }

    public GroupInformationFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_group_info, container, false);

        viewBinding = DataBindingUtil.bind(view);
        if (user != null) {
            viewBinding.setUser(user);
        }

        if (group != null) {
            setGroup(group);
        }

        ButterKnife.bind(this, view);
        questMemberView.setLayoutManager(new LinearLayoutManager(getContext()));
        questMemberView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        participantViewAdapter = new PartyMemberQuestRecyclerViewAdapter();
        questMemberView.setAdapter(participantViewAdapter);

        collectionStats.setLayoutManager(new LinearLayoutManager(getContext()));
        questCollectViewAdapter = new QuestCollectRecyclerViewAdapter();
        collectionStats.setAdapter(questCollectViewAdapter);

        bossHpBar = DataBindingUtil.bind(view.findViewById(R.id.bossHpBar));
        bossRageBar = DataBindingUtil.bind(view.findViewById(R.id.bossRageBar));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setGroup(Group group) {
        if (viewBinding != null) {
            viewBinding.setGroup(group);
        }

        updateQuestMember(group);

        updateQuestProgress(group, quest);

        this.group = group;
    }

    public void setQuestContent(QuestContent quest) {
        if (viewBinding != null) {
            viewBinding.setQuest(quest);
        }

        updateQuestDrop(quest);

        updateQuestProgress(group, quest);

        this.quest = quest;
    }

    private void updateQuestDrop(QuestContent quest) {

        questDrop.removeAllViewsInLayout();
        if (quest.drop == null) return;

        ArrayList<String> a = new ArrayList<>();
        for (QuestDropItem i : quest.drop.getItems()) {
            a.add(i.text);
        }
        if (quest.drop.exp > 0) {
            a.add(Integer.toString(quest.drop.exp) + " Experience");
        }
        if (quest.drop.gp > 0) {
            a.add(Integer.toString(quest.drop.gp) + " Gold");
        }
        if (!quest.drop.unlock.equals("")) {
            a.add(quest.drop.unlock);
        }
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (String s : a) {
            final LinearLayout itemView = (LinearLayout) layoutInflater.inflate(R.layout.quest_drop_item, null);
            TextView textView = (TextView) itemView.findViewById(R.id.textView);
            textView.setText(s);
            questDrop.post(new Runnable() {
                @Override
                public void run() {
                    questDrop.addView(itemView);
                }
            });
        }
    }

    private void updateQuestProgress(Group group, QuestContent quest) {
        if (group == null || quest == null) {
            return;
        }
        questCollectViewAdapter.setQuestContent(quest);
        if (group.quest.getProgress() != null) {
            questCollectViewAdapter.setQuestProgress(group.quest.getProgress());
        }
        bossHpBar.valueBarLayout.setVisibility((quest.boss != null && quest.boss.hp > 0) ? View.VISIBLE : View.GONE);
        bossRageBar.valueBarLayout.setVisibility((quest.boss != null && quest.boss.rage_value > 0) ? View.VISIBLE : View.GONE);
    }

    private void updateQuestMember(Group group) {
        participantViewAdapter.setGroup(group);
    }


    @OnClick(R.id.btnQuestAccept)
    public void onQuestAccept() {
        mAPIHelper.apiService.acceptQuest(group.id, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                user.getParty().getQuest().RSVPNeeded = false;
                group.quest.members.put(user.getId(), true);
                setGroup(group);
                viewBinding.setUser(user);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    @OnClick(R.id.btnQuestReject)
    public void onQuestReject() {
        mAPIHelper.apiService.rejectQuest(group.id, new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                user.getParty().getQuest().RSVPNeeded = false;
                group.quest.members.put(user.getId(), false);
                setGroup(group);
                viewBinding.setUser(user);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }


    @OnClick(R.id.btnQuestLeave)
    public void onQuestLeave() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage("Are you sure you want to leave the active quest? All your quest progress will be lost.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAPIHelper.apiService.leaveQuest(group.id, new Callback<Void>() {
                            @Override
                            public void success(Void aVoid, Response response) {
                                group.quest.members.remove(user.getId());
                                setGroup(group);
                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    @OnClick(R.id.btnQuestBegin)
    public void onQuestBegin() {
        mAPIHelper.apiService.forceStartQuest(group.id, group, new Callback<Group>() {
            @Override
            public void success(Group group, Response response) {
                setGroup(group);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @OnClick(R.id.btnQuestCancel)
    public void onQuestCancel() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage("Are you sure you want to cancel this quest? All invitation acceptances will be lost. The quest owner will retain possession of the quest scroll.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAPIHelper.apiService.cancelQuest(group.id, new Callback<Void>() {
                            @Override
                            public void success(Void aVoid, Response response) {
                                setGroup(group);
                                setQuestContent(null);
                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

    @OnClick(R.id.btnQuestAbort)
    public void onQuestAbort() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage("Are you sure you want to abort this mission? It will abort it for everyone in your party and all progress will be lost. The quest scroll will be returned to the quest owner.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAPIHelper.apiService.abortQuest(group.id, new Callback<Group>() {
                            @Override
                            public void success(Group group, Response response) {
                                setGroup(group);
                                setQuestContent(null);
                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }

}
