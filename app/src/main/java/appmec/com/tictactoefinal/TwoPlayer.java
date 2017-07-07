package appmec.com.tictactoefinal;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class TwoPlayer extends AppCompatActivity {

    int[] unicode = {0x1F60A,0x1F64C,0x1F603,0x1F604,0x1F60E,0x1F61B};
    static int index=0;
    private int size;
    TableLayout mainBoard;
    TextView tv_turn;
    char[][] board;
    //rr
    char[][] dumyBoard;
    char turn;

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_player);

        size = Integer.parseInt(getString(R.string.size_of_board));
        board = new char[size][size];
        //rr
        dumyBoard = new char[size][size];



        mainBoard = (TableLayout) findViewById(R.id.mainBoard);
        tv_turn = (TextView) findViewById(R.id.turn);

        resetBoard();
        tv_turn.setText("Turn: " + turn);

        for (int i = 0; i < mainBoard.getChildCount(); i++) {
            TableRow row = (TableRow) mainBoard.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView tv = (TextView) row.getChildAt(j);
                tv.setText(R.string.none);
                tv.setOnClickListener(Move(i, j, tv));
            }
        }

        Button rstbtn = (Button) findViewById(R.id.reset);
        rstbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent current = getIntent();
                finish();
                startActivity(current);
            }
        });

        MobileAds.initialize(getApplicationContext(),
                "ca-app-pub-7860341576927713~8020931580");
        mAdView = (AdView) findViewById(R.id.adView3);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    protected void resetBoard() {
        turn = 'X';
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = ' ';
            }
        }
    }

    protected int gameStatus() {

        //0 Continue
        //1 X Wins
        //2 O Wins
        //-1 Draw

        int rowX = 0, colX = 0, rowO = 0, colO = 0;
        for (int i = 0; i < size; i++) {
            if (check_Row_Equality(i, 'X'))
                return 1;
            if (check_Column_Equality(i, 'X'))
                return 1;
            if (check_Row_Equality(i, 'O'))
                return 2;
            if (check_Column_Equality(i, 'O'))
                return 2;
            if (check_Diagonal('X'))
                return 1;
            if (check_Diagonal('O'))
                return 2;
        }

        boolean boardFull = true;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == ' ')
                    boardFull = false;
            }
        }
        if (boardFull)
            return -1;
        else return 0;
    }

    protected boolean check_Diagonal(char player) {
        int count_Equal1 = 0, count_Equal2 = 0;
        for (int i = 0; i < size; i++)
            if (board[i][i] == player)
                count_Equal1++;
        for (int i = 0; i < size; i++)
            if (board[i][size - 1 - i] == player)
                count_Equal2++;
        if (count_Equal1 == size || count_Equal2 == size) {
            //rr
            if(count_Equal1==size) {
                for (int i = 0; i < size; i++)
                    if (board[i][i] == player)
                        dumyBoard[i][i] = 'R';
            }else{
                for (int i = 0; i < size; i++)
                    if (board[i][size - 1 - i] == player)
                        dumyBoard[i][size - 1 - i] = 'R';
            }
            return true;
        }
        else return false;
    }

    protected boolean check_Row_Equality(int r, char player) {
        int count_Equal = 0;
        for (int i = 0; i < size; i++) {
            if (board[r][i] == player)
                count_Equal++;
        }

        if (count_Equal == size) {
            //rr
            for (int i = 0; i < size; i++) {
                if (board[r][i] == player)
                    dumyBoard[r][i]='R';
            }
            return true;
        }
        else
            return false;
    }

    protected boolean check_Column_Equality(int c, char player) {
        int count_Equal = 0;
        for (int i = 0; i < size; i++) {
            if (board[i][c] == player)
                count_Equal++;
        }
        if (count_Equal == size) {
            //rr
            for (int i = 0; i < size; i++) {
                if (board[i][c] == player)
                    dumyBoard[i][c]='R';
            }
            return true;
        }
        else
            return false;
    }

    protected boolean Cell_Set(int r, int c) {
        return !(board[r][c] == ' ');
    }

    protected void stopMatch() {
        for (int i = 0; i < mainBoard.getChildCount(); i++) {
            TableRow row = (TableRow) mainBoard.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView tv = (TextView) row.getChildAt(j);
                tv.setOnClickListener(null);
            }
        }
    }

    View.OnClickListener Move(final int r, final int c, final TextView tv) {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                char previousTurn=turn;
                if (!Cell_Set(r, c)) {
                    board[r][c] = turn;
                    if (turn == 'X') {
                        tv.setText(R.string.X);
                        tv.setTextColor(Color.BLACK);
                        turn = 'O';
                    } else if (turn == 'O') {
                        tv.setText(R.string.O);
                        tv.setTextColor(Color.BLACK);
                        turn = 'X';
                    }
                    if (gameStatus() == 0) {
                        tv_turn.setText("Turn: " + turn);
                    } else if (gameStatus() == -1) {
                        tv_turn.setText("Game: Draw");
                        stopMatch();
                    } else {
                        //rr
                        resultStrip(previousTurn);
                        tv_turn.setText("Player '"+previousTurn + "' Wins !!! ");
                        stopMatch();
                    }
                } else {
                    if(tv_turn.getText().length()<=7) {
                        CharSequence text = tv_turn.getText() + " Please choose a Cell Which is not already Occupied";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(TwoPlayer.this, text, duration);
                        toast.show();
                        tv_turn.setText(tv_turn.getText() + " Please choose a Cell Which is not already Occupied");
                    }
                }
            }
        };
    }

    public void resultStrip(char result){

        for (int i = 0; i < mainBoard.getChildCount(); i++) {
            TableRow row = (TableRow) mainBoard.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                TextView tv = (TextView) row.getChildAt(j);
                char ch=board[i][j];
                char rsch=dumyBoard[i][j];
                if(ch==result && rsch=='R'){
                    tv.setText(" "+getEmojiByUnicode()+" ");
                }
            }
        }
        if(index==5)
            index=0;
        else
            index++;
    }

    public String getEmojiByUnicode(){

        return new String(Character.toChars(unicode[index]));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_board, menu);
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
            AlertDialog.Builder dialog = new AlertDialog.Builder(TwoPlayer.this);
            dialog.setMessage(Html.fromHtml("<b>Developers:</b><br/><br/> Akash Doshi" + "<br/>" + "Rahul Patil"+"<br/>"+"Disha Mahajan"));

            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
