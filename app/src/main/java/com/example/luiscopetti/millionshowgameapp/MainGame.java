package com.example.luiscopetti.millionshowgameapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.copetti.core.MillionGame;
import com.copetti.core.MillionShowGame;
import com.copetti.core.Question;
import com.copetti.core.QuestionPool;
import com.copetti.core.dao.QuestionXMLDao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class MainGame extends ActionBarActivity {


    private MillionShowGame millionGame;

    // UI ELEMENTS
    private TextView txtQuestion;
    private RadioGroup radioGroup;
    private RadioButton[] rbOptions;

    private Map<Question.Option, Integer> mapQId;

    private Button btnAnswer;
    private ImageButton btnSkip;
    private ImageButton btnCards;
    private Button btnStop;

    private TextView txtPrizeError;
    private TextView txtPrizeStop;
    private TextView txtPrizeScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        Map<Question.Option, Integer> maps = new HashMap<Question.Option, Integer>();
        maps.put(Question.Option.A, R.id.rB_Option1);
        maps.put(Question.Option.B, R.id.rB_Option2);
        maps.put(Question.Option.C, R.id.rB_Option3);
        maps.put(Question.Option.D, R.id.rB_Option4);

        mapQId = Collections.unmodifiableMap(maps);

        try {
            InputStream is = this.getAssets().open("questions.xml");


            //QuestionXMLDao questions = QuestionXMLDao.newInstance(file.getAbsolutePath());
            QuestionXMLDao questions = new QuestionXMLDao(is);
            QuestionPool questionPool = new QuestionPool(questions, 8);
            millionGame = new MillionGame(questionPool);
        }
        catch(IOException e)
        {
            Toast.makeText(this, "Questions file not found!!!" + e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        txtQuestion = (TextView) findViewById(R.id.txt_Question);

        radioGroup = (RadioGroup) findViewById(R.id.rdGroup);
        rbOptions = new RadioButton[4];
        rbOptions[0] = (RadioButton)findViewById(R.id.rB_Option1);
        rbOptions[1] = (RadioButton)findViewById(R.id.rB_Option2);
        rbOptions[2] = (RadioButton)findViewById(R.id.rB_Option3);
        rbOptions[3] = (RadioButton)findViewById(R.id.rB_Option4);

        btnAnswer = (Button)findViewById(R.id.btn_Answer);
        btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnAnswer_Clicked();
            }
        });

        btnSkip = (ImageButton)findViewById(R.id.btn_Skip);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSkip_Clicked();
            }
        });

        btnCards = (ImageButton)findViewById(R.id.btn_Cards);

        btnCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCards_Clicked();
            }
        });


        txtPrizeError = (TextView) findViewById(R.id.txt_PrizeWrong);
        txtPrizeStop = (TextView) findViewById(R.id.txt_PrizeStop);
        txtPrizeScore = (TextView) findViewById(R.id.txt_PrizeScore);

        updateUI();

    }

    public void buttonParar_Clicked()
    {

    }

    public void btnSkip_Clicked()
    {
        millionGame.skip();
        updateUI();
    }

    public void btnCards_Clicked()
    {
        Set<Question.Option> options = millionGame.useCards();

        for 

    }

    public void btnAnswer_Clicked()
    {
        Question.Option option = getOption();

        if (millionGame.answer(option))
        {
            // answer correct
            updateUI();
        }
        else
        {
            // answer incorrect
        }
    }

    public Question.Option getOption()
    {
        int radioButtonId = radioGroup.getCheckedRadioButtonId();

        int radioButtonSelected = radioGroup.getCheckedRadioButtonId();

        for (Map.Entry e : mapQId.entrySet())
            if ( e.getValue() == radioButtonSelected)
                return (Question.Option)e.getKey();
        throw new AssertionError("Option not existent");
    }

    public int getButtonId()
    {

    }


    public void updateUI()
    {
        Question question = millionGame.getCurrentQuestion();

        txtQuestion.setText(question.getQuestion());

        Map<Question.Option, String> options = question.getOptions();
        Question.Option[] ops = Question.Option.values();
        for (int i = 0; i < options.size(); i++ )
        {
            rbOptions[i].setText(options.get(ops[i]));
        }

        Map<MillionShowGame.PrizeValue, Integer> prizes = millionGame.getPrizes();

        String prizeError = String.valueOf(prizes.get(MillionShowGame.PrizeValue.ERROR));
        String prizeStop = String.valueOf(prizes.get(MillionShowGame.PrizeValue.STOP));
        String prizeScore = String.valueOf(prizes.get(MillionShowGame.PrizeValue.SCORE));

        txtPrizeError.setText(prizeError);
        txtPrizeStop.setText(prizeStop);
        txtPrizeScore.setText(prizeScore);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_game, menu);
        return true;
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
