package com.example.luiscopetti.millionshowgameapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Luis Copetti on 24/05/2015.
 */
public class RankingAdapter extends ArrayAdapter<Score> {

    private int indexHighlight = -1;

    public RankingAdapter(Activity context, Score[] objects) {
        this(context, objects, -1);
    }

    public RankingAdapter(Activity context, Score[] objects, int idxHighlight) {
        super(context, R.layout.ranking_list, objects);

        this.indexHighlight = idxHighlight;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();

        View row = inflater.inflate(R.layout.ranking_list, null, true);
        if ( position == indexHighlight )
            row.setBackgroundColor(Color.GREEN);

       TextView ranking = (TextView) row.findViewById(R.id.txt_Ranking);
       TextView name = (TextView) row.findViewById(R.id.txt_Name);
       TextView timeS = (TextView) row.findViewById(R.id.txt_Time);
       TextView prize = (TextView) row.findViewById(R.id.txt_Prize);

        Score s = getItem(position);

        ranking.setText((position+1) + ".");
        name.setText(s.getName());
        timeS.setText(Util.timeToString(s.getTimeS()));
        prize.setText("R$ " + Util.prizeToString(s.getPrize()));


        return row;
    }
}
