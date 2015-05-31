package com.example.luiscopetti.millionshowgameapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.copetti.core.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ChooseCards extends ActionBarActivity {

    public static final String cardHolder = "cardHolder";

    private ImageView[] images;
    private Question.Option[] options = Question.Option.values();

    private Button btn_ChooseCard;

    private int chosenCachedImageIndex = -1;

    private List<Integer> arrayCardsToTakeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_cards);
        setTitle(getString(R.string.TITLE_CHOOSECARDS));

        images = new ImageView[4];
        images[0] = (ImageView) findViewById(R.id.iV_1);
        images[1] = (ImageView) findViewById(R.id.iV_2);
        images[2] = (ImageView) findViewById(R.id.iV_3);
        images[3] = (ImageView) findViewById(R.id.iV_4);

        // Set up integer array
        Integer[] array = {0,1,2,3};
        arrayCardsToTakeOut = new ArrayList<Integer>(Arrays.asList(array));
        Collections.shuffle(arrayCardsToTakeOut);

        for (int i = 0; i < images.length; i++)
            images[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    image_Clicked(v);
                }
            });

        btn_ChooseCard = (Button) findViewById(R.id.btn_ChooseCard);

        btn_ChooseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bntChooseCard_Clicked();
            }
        });
        /*Intent intent = new Intent();
        intent.putExtra(cardHolder, "3");
        setResult(Activity.RESULT_OK, intent);
        finish();*/
    }

    private void image_Clicked(View v) {

        for (int i = 0; i < images.length; i++ )
            if ( images[i].getId() == v.getId()) {
                images[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.card_back_tick));
                chosenCachedImageIndex = i;
            }
            else
                images[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.card_back));
    }

    private void bntChooseCard_Clicked()
    {
        if ( chosenCachedImageIndex < 0) {
            Toast.makeText(this, "Escolha ao menos uma Carta!", Toast.LENGTH_SHORT).show();
            return;
        }

        final int cardsToTakeOut = arrayCardsToTakeOut.get(chosenCachedImageIndex);

        images[chosenCachedImageIndex].setBackgroundDrawable(getResources().getDrawable(getDrawableId(cardsToTakeOut)));

        for (int i = 0; i < images.length; i++)
            images[i].setEnabled(false);
        btn_ChooseCard.setEnabled(false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.putExtra(ChooseCards.cardHolder, cardsToTakeOut);
                setResult(RESULT_OK, intent);
                finish();
            }
        }, 1000);

    }

    private int getDrawableId(int cardsToTakeOut)
    {
        switch(cardsToTakeOut)
        {
            case 0:
                return R.drawable.card_king_heart;
            case 1:
                return R.drawable.card_spades_ace;
            case 2:
                return R.drawable.card_spades_two;
            case 3:
                return R.drawable.card_spades_three;
            default:
                throw new IllegalArgumentException("Cant take that many cards out!! " + cardsToTakeOut);
        }
    }

}
