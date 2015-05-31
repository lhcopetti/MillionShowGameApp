package com.example.luiscopetti.millionshowgameapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.games.Game;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class RankingActivity extends ActionBarActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        setTitle(getString(R.string.TITLE_RANKINGACTIVITY));

        listView = (ListView) findViewById(R.id.lst_Ranking);

        List<Score> scores = GameMenu.players;
        Collections.sort(scores);

        String nameToHighlight = getIntent().getStringExtra(getString(R.string.EXTRA_MAINGAME_NAME));
        int idxToHighlit = -1;

        if ( nameToHighlight != null )
        {
            for (int i = 0; i < scores.size(); i++)
                if ( scores.get(i).getName().equals(nameToHighlight))
                idxToHighlit = i;
        }
        RankingAdapter rAdapter = new RankingAdapter(RankingActivity.this, scores.toArray(new Score[0]), idxToHighlit);
        listView.setAdapter(rAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
