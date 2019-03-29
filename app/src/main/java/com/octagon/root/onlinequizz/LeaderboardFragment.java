package com.octagon.root.onlinequizz;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.octagon.root.onlinequizz.Common.Common;
import com.octagon.root.onlinequizz.Inteface.ItemClickListener;
import com.octagon.root.onlinequizz.Inteface.RankingCallBack;
import com.octagon.root.onlinequizz.Model.QuestionScore;
import com.octagon.root.onlinequizz.Model.Ranking;
import com.octagon.root.onlinequizz.ViewHolder.RankingViewHolder;


public class LeaderboardFragment extends Fragment {
    View myFragment;

    RecyclerView rankingList;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<Ranking,RankingViewHolder> adapter;


    FirebaseDatabase database;
    DatabaseReference questionScore, rankingTbl;




    int sum=0;
    public static LeaderboardFragment newInstance(){
        LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
        return leaderboardFragment;
    }

    //

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        questionScore = database.getReference("Question_Score");
        rankingTbl = database.getReference("Ranking");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_leaderboard,container,false);

        rankingList = (RecyclerView)myFragment.findViewById(R.id.rankingList);
        layoutManager = new LinearLayoutManager(getActivity());
        rankingList.setHasFixedSize(true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rankingList.setLayoutManager(layoutManager);




        updateScore(Common.currentUser.getUsername(), new RankingCallBack<Ranking>() {
            @Override
            public void callBack(Ranking ranking) {

                rankingTbl.child(ranking.getUserName())
                        .setValue(ranking);
                //showRanking();

            }
        });

       adapter = new FirebaseRecyclerAdapter<Ranking, RankingViewHolder>(
               Ranking.class,
               R.layout.layout_ranking,
               RankingViewHolder.class,
               rankingTbl.orderByChild("score")

       ) {
           @Override
           protected void populateViewHolder(RankingViewHolder viewHolder, final Ranking model, int position) {

               viewHolder.txt_name.setText(model.getUserName());
               viewHolder.txt_score.setText(String.valueOf(model.getScore()));

               viewHolder.setItemClickListener(new ItemClickListener() {
                   @Override
                   public void onClick(View view, int position, boolean isLongClick) {
                       Intent scoreDetail = new Intent(getActivity(),ScoreDetail.class);
                       scoreDetail.putExtra("viewUser",model.getUserName());
                       startActivity(scoreDetail);

                   }
               });

           }
       };

       adapter.notifyDataSetChanged();
       rankingList.setAdapter(adapter);

        
        return myFragment;
    }




    private void updateScore(final String username, final RankingCallBack<Ranking> callback) {

        questionScore.orderByChild("user").equalTo(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data:dataSnapshot.getChildren())
                        {
                            QuestionScore ques = data.getValue(QuestionScore.class);
                            sum+= Integer.parseInt(ques.getScore());
                        }
                        //after summing all score, we need to process sum variable here.
                        //Firebase has async DB

                        Ranking ranking = new Ranking(username,sum);
                        callback.callBack(ranking);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }
}
