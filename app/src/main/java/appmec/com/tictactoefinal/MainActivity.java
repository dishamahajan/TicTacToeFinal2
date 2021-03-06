package appmec.com.tictactoefinal;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    Button button;
    Button button2;
    Button button3;
    Button button4;
    final Context context = this;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    public static final String PREFS_NAME = "TicTacToeFile";
    public static String PlayerX="Player X";
    public static String PlayerY="Player Y";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Home");

        final Context context = this;
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        MainActivity.PlayerX = settings.getString("player1", MainActivity.PlayerX);
        MainActivity.PlayerY = settings.getString("player2", MainActivity.PlayerY);

        button = (Button) findViewById(R.id.button1);
        button2= (Button) findViewById(R.id.button2);
        button3= (Button) findViewById(R.id.button3);
        button4= (Button) findViewById(R.id.button4);

        button4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }

        });

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, OnePlayer.class);
                startActivity(intent);

            }

        });

        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(context, TwoPlayer.class);
                startActivity(intent);


            }

        });

        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showDialogBox();
            }

        });



        MobileAds.initialize(getApplicationContext(),
                "ca-app-pub-7860341576927713~8020931580");
        mAdView = (AdView) findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("D8D6B049EDAB3CB2227DD36B3ED29F2D")
                .addTestDevice("25F149879ED72631F3CB460DEED0436A")
                .addTestDevice("B117F7C5611FB5503E6D2BD2CCA8C928")
                .addTestDevice("5FBF995F76CDF38832A294D8A2EE7DD0")
                .build();
        mAdView.loadAd(adRequest);
    }

    private void showDialogBox() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.player_dialog_box);
        dialog.setCanceledOnTouchOutside(false);
        TextView text = (TextView) dialog.findViewById(R.id.text);
        final Button setPlayerName = (Button) dialog.findViewById(R.id.setPlayerName);
        Button dialogQuit = (Button) dialog.findViewById(R.id.dialogQuit);
        TextView playerX1 = (TextView) dialog.findViewById(R.id.playerX);
        TextView playerO1 = (TextView) dialog.findViewById(R.id.playerO);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if(!settings.getString("player1", MainActivity.PlayerX).equals("Player X")) {
            playerX1.setText(settings.getString("player1", MainActivity.PlayerX));
        }
        if(!settings.getString("player2", MainActivity.PlayerY).equals("Player Y")) {
            playerO1.setText(settings.getString("player2", MainActivity.PlayerY));
        }

        setPlayerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView playerX = (TextView) dialog.findViewById(R.id.playerX);
                TextView playerO = (TextView) dialog.findViewById(R.id.playerO);
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                MainActivity.PlayerX = playerX.getText().toString();
                MainActivity.PlayerY = playerO.getText().toString();
                editor.putString("player1",MainActivity.PlayerX);
                editor.putString("player2",MainActivity.PlayerY);
                editor.commit();
                dialog.dismiss();
            }
        });

        dialogQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                }).setNegativeButton("No", null).show();
    }

}
