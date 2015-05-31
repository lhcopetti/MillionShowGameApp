package com.example.luiscopetti.millionshowgameapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Timer;
import java.util.TimerTask;


public class MainGame extends ActionBarActivity {


    private MillionShowGame millionGame;
    private long initGameTime = -1;
    private int questionsAnsweredRight = 0;

    private static final int rCode_UseCards = 1;

    // UI ELEMENTS
    private TextView txtQuestion;
    private RadioGroup radioGroup;
    private RadioButton[] rbOptions;
    //private RadioButton rbInvisible;

    private Map<Question.Option, Integer> mapQId;

    private Button btnAnswer;
    private ImageButton btnSkip;
    private ImageButton btnCards;
    private Button btnStop;

    private TextView txtPrizeError;
    private TextView txtPrizeStop;
    private TextView txtPrizeScore;

    private TextView txtQuestionsAnswered;
    private TextView txtTimeElapsed;

    private final TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtTimeElapsed.setText(Util.timeToString((int)((System.nanoTime() - initGameTime )/ (1000*1000*1000))));
                }
            });
        }
    };
    private final Timer gameTimer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);

        setTitle(getString(R.string.TITLE_MAINGAME));

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

        //rbInvisible = (RadioButton) findViewById(R.id.rb_Invisible);

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

        btnStop = (Button) findViewById(R.id.btn_Stop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStop_Clicked();
            }
        });



        txtPrizeError = (TextView) findViewById(R.id.txt_PrizeWrong);
        txtPrizeStop = (TextView) findViewById(R.id.txt_PrizeStop);
        txtPrizeScore = (TextView) findViewById(R.id.txt_PrizeScore);

        txtQuestionsAnswered = (TextView) findViewById(R.id.txt_QuestionsAnswered);
        txtTimeElapsed = (TextView) findViewById(R.id.txt_TimeElapsed);

        updateUI();
        initGameTime = System.nanoTime();

        gameTimer.schedule(task, 0, 1000);

    }

    public void btnStop_Clicked()
    {
        millionGame.quit();
        new EndGameHandler(this, millionGame.getGameOverPrize(), initGameTime, questionsAnsweredRight);
    }

    public void btnSkip_Clicked()
    {
        if ( millionGame.can(MillionShowGame.GameAction.SKIP) > 0) {

            final ProgressDialog pd = ProgressDialog.show(this, "Pulando questão", "Aguarde enquanto buscamos outra questão!", true, false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pd.dismiss();
                }
            }, 1000);
            millionGame.skip();
            updateUI();

            if ( millionGame.can(MillionShowGame.GameAction.SKIP) <= 0 )
            {
                btnSkip.setBackgroundDrawable(getResources().getDrawable(R.drawable.pulo_milha_x));
            }
        }
        else
        {

            Toast.makeText(this, "Você já usou todos os seus Pulos!", Toast.LENGTH_SHORT).show();
        }
    }

    public void btnCards_Clicked()
    {
        if (millionGame.can(MillionShowGame.GameAction.USECARDS) > 0 )
        {
            Intent intentCards = new Intent();
            intentCards.setClass(this, ChooseCards.class);
            startActivityForResult(intentCards, rCode_UseCards);
        }
        else
        {
            Toast.makeText(this, "Você já usou Cartas demais!!", Toast.LENGTH_SHORT).show();
        }
    }

    public MillionShowGame.MillionCards cardFromInt(int card)
    {
        switch(card)
        {
            case 0:
                return MillionShowGame.MillionCards.KING;
            case 1:
                return MillionShowGame.MillionCards.ACE;
            case 2:
                return MillionShowGame.MillionCards.TWO;
            case 3:
                return MillionShowGame.MillionCards.THREE;
        }
        throw new IllegalArgumentException("There is no such card: " + card);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( requestCode == rCode_UseCards )
        {
            if ( resultCode == RESULT_OK )
            {
                int chosenCard = -1;
                if ( data != null && (chosenCard = data.getIntExtra(ChooseCards.cardHolder, -1)) != -1)
                {
                    MillionShowGame.MillionCards card = cardFromInt(chosenCard);
                    Set<Question.Option> remainingOptions = millionGame.useCards(card);


                    if ( millionGame.can(MillionShowGame.GameAction.USECARDS) <= 0)
                    {
                        btnCards.setBackgroundDrawable(getResources().getDrawable(R.drawable.cards_milha_x));
                    }

                    final ProgressDialog pd = ProgressDialog.show(this, "Aguarde!", "Removendo opções...", true, false);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                        }
                    }, 1000);

                    for (Question.Option op : Question.Option.values())
                    {
                        if (!remainingOptions.contains(op))
                        {
                            RadioButton rb = (RadioButton)findViewById(mapQId.get(op));
                            rb.setVisibility(View.INVISIBLE);
                            //rb.setEnabled(false);
                            //rb.setBackgroundColor(Color.BLACK);
                        }
                    }
                }
            }
            else
            {
                // Do nothing, the user canceled the cards.
                Toast.makeText(this, "Sabia que você não precisaria de ajuda!!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void btnAnswer_Clicked()
    {
        Question.Option option = getOption();

        // If there is no option selected. Ignore!
        if ( option == null ) {
            Toast.makeText(this, "Escolha ao menos uma opção!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (millionGame.answer(option))
        {
            ++questionsAnsweredRight;
            // simply update UI
            if ( millionGame.isGameOver() )
            {
                // The game is over. Corra pro abraço. YOU WON!
                new EndGameHandler(this, millionGame.getGameOverPrize(), initGameTime, questionsAnsweredRight);
                return;
            }

            updateUI();
        }
        else
        {
            // wrong answer
            RadioButton rightAnswer = (RadioButton)findViewById(mapQId.get(millionGame.getCorrectAnswer()));
            RadioButton wrongAnswer = (RadioButton)findViewById(mapQId.get(option));

            rightAnswer.setTypeface(null, Typeface.BOLD);
            rightAnswer.setTextColor(Color.GREEN);
            wrongAnswer.setTypeface(null, Typeface.BOLD);
            wrongAnswer.setTextColor(Color.RED);


            btnAnswer.setEnabled(false);
            btnSkip.setEnabled(false);
            btnCards.setEnabled(false);
            btnStop.setEnabled(false);

            Toast.makeText(this, "Resposta errada", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new EndGameHandler(MainGame.this, millionGame.getGameOverPrize(), initGameTime, questionsAnsweredRight);
                }
            }, 1500);

        }
    }

    private class EndGameHandler
    {
        private Context context;
        private EditText editText;
        private AlertDialog alert;

        private int prize;
        private int timeS;
        private int numberOfQuestions;

        public EndGameHandler(Context context, int prize, long beginTimeS, int numberOfQuestions)
        {
            this.context = context;

            this.prize = prize;
            gameTimer.cancel();
            this.timeS = (int)((System.nanoTime() - beginTimeS ) / (1000 * 1000 * 1000));
            this.numberOfQuestions = numberOfQuestions;

            editText = new EditText(context);
            editText.setText("");
            editText.setHint("Seu nome...");

            final StringBuilder alertMessage = new StringBuilder();
            alertMessage.append("Prêmio: R$" + Util.prizeToString(prize)+ "\n");
            alertMessage.append("Tempo: " + Util.timeToString(timeS) + "\n");
            alertMessage.append("#Questões: " + questionsAnsweredRight + "\n");
            alertMessage.append("Digite seu nome para tentar uma vaga no podium: ");

            alert = new AlertDialog.Builder(context).
                    setTitle(getString(R.string.MainGame_AlertDialogTitle)).
                    setMessage(alertMessage.toString()).
                    setPositiveButton(getString(R.string.MainGame_AlertDialogPosButton), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing, custom listener will validate input
                        }
                    }).
                    setNegativeButton(getString(R.string.MainGame_AlertDialogNegButton), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            negativeClicked();
                        }
                    }).setView(editText).
                    setCancelable(false).
                    show();
            Button positiveButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    positiveClicked();
                }
            });
        }

        public void cancelClicked()
        {
            Toast.makeText(context, "Escolha uma opção ao menos!", Toast.LENGTH_LONG).show();
        }

        public void positiveClicked()
        {
            String value = editText.getText().toString();
            if ( null == value || value.length() < 3)
                Toast.makeText(context, "Seu nome deve ter no mínimo três caracteres.", Toast.LENGTH_LONG).show();
            else if (GameMenu.players.contains(value))
                Toast.makeText(context, "Já existe alguém com esse nome! Seja mais específico!", Toast.LENGTH_LONG).show();
            else
            {
                String name = editText.getText().toString();

                // Close the dialog
                alert.dismiss();

                // Add the new player to the ranking list
                Score score = new Score(value, prize, timeS, numberOfQuestions);
                GameMenu.players.add(score);

                // Open the ranking activity, and finish this one.
                Intent intent = new Intent();

                // Pass name as parameter so that this entry is highlighted
                intent.putExtra(getString(R.string.EXTRA_MAINGAME_NAME), score.getName());

                intent.setClass(context, RankingActivity.class);
                startActivity(intent);
                finish();
            }
        }

        public void negativeClicked()
        {
            ((Activity)context).setResult(RESULT_CANCELED);
            ((Activity)context).finish();
        }
    }


    public Question.Option getOption()
    {
        int radioButtonId = radioGroup.getCheckedRadioButtonId();

        if ( radioButtonId < 0 )
            return null;

        View radioButton = findViewById(radioButtonId);

        int idx = radioGroup.indexOfChild(radioButton);

        return Question.Option.values()[idx];
    }

    public void updateUI()
    {
        Question question = millionGame.getCurrentQuestion();

        txtQuestion.setText(question.getQuestion());

        Map<Question.Option, String> options = question.getOptions();
        Question.Option[] ops = Question.Option.values();

        Drawable draw = getResources().getDrawable(R.drawable.bar_alternativas);

        radioGroup.clearCheck();

        for (int i = 0; i < options.size(); i++ )
        {
            rbOptions[i].setText(options.get(ops[i]));
            rbOptions[i].setVisibility(View.VISIBLE);
        }

        //rbInvisible.setChecked(true);

        Map<MillionShowGame.PrizeValue, Integer> prizes = millionGame.getPrizes();

        String prizeError = String.valueOf(prizes.get(MillionShowGame.PrizeValue.ERROR));
        String prizeStop = String.valueOf(prizes.get(MillionShowGame.PrizeValue.STOP));
        String prizeScore = String.valueOf(prizes.get(MillionShowGame.PrizeValue.SCORE));

        txtPrizeError.setText("Errar: R$" + Util.prizeToString(prizes.get(MillionShowGame.PrizeValue.ERROR)));
        txtPrizeStop.setText("Parar: R$" + Util.prizeToString(prizes.get(MillionShowGame.PrizeValue.STOP)));
        txtPrizeScore.setText("Acertar: R$" + Util.prizeToString(prizes.get(MillionShowGame.PrizeValue.SCORE)));

        txtQuestionsAnswered.setText("# Questões: " + questionsAnsweredRight);
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

    public void sleep(int ms)
    {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
