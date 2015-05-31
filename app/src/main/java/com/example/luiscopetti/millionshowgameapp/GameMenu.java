package com.example.luiscopetti.millionshowgameapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

// TODO implementar tabela premios
// TODO Implementar bordas aos valores dos premios, tempo e # questoes (MainGame).
// Melhorar splash screen (ou remover)

public class GameMenu extends ActionBarActivity {


    private static final int rCode_MillionGame = 1;

    public static final String pNameHolder = "pName";
    public static final String pPrizeHolder = "pPrize";
    public static final String pTimeSHolder = "pTime";
    public static final String pQuestionsHolder = "pQuestion";

    public static final List<Score> players = new ArrayList<Score>();

    private Button btnPlay;
    private Button btnRanking;
    private Button btnPrizes;

    private Animation myAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);
        setTitle(getString(R.string.TITLE_GAMEMENU));

        players.add(new Score("can you beat me?", 1000000, 20, 5));
        players.add(new Score("Jogador anônimo", 50000, 90, 5));
        players.add(new Score("Nunca mais jogo isso", 500, 720, 5));


        btnPlay = (Button) findViewById(R.id.btn_Play);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlay_Clicked();
            }
        });

        btnRanking = (Button) findViewById(R.id.btn_Ranking);
        btnRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRanking_Clicked();
            }
        });

        /*btnPrizes = (Button) findViewById(R.id.btn_Prizes);
        btnPrizes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPrizes_Clicked();
            }
        });*/

        //myAnimation = AnimationUtils.loadAnimation(this, R.anim.animacao_top);
        //btnRanking.startAnimation(myAnimation);


    }


    public void btnPlay_Clicked()
    {
        Intent intent = new Intent();
        intent.setClass(this, MainGame.class);
        startActivityForResult(intent, rCode_MillionGame);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode)
        {
            case rCode_MillionGame:
                if (resultCode == RESULT_OK && data != null)
                {
                    String playerName = data.getStringExtra(pNameHolder);
                    int playerPrize = data.getIntExtra(pPrizeHolder, 0);
                    int timeS = data.getIntExtra(pTimeSHolder, 0);
                    int questions = data.getIntExtra(pQuestionsHolder, 0);

                    Score p = new Score(playerName, playerPrize, timeS, questions);
                    players.add(p);
                }
        }

    }

    public void btnRanking_Clicked()
    {
        Intent intent = new Intent();
        intent.setClass(this, RankingActivity.class);
        startActivity(intent);
    }

    /*public void btnPrizes_Clicked()
    {
        Toast.makeText(this, "Não pagou nada e ainda quer prêmio? Tá de brinks né!", Toast.LENGTH_LONG).show();
    }*/

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_menu, menu);
        return true;
    }*/

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
