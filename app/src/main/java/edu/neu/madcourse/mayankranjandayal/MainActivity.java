package edu.neu.madcourse.mayankranjandayal;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Toast;

import edu.neu.madcourse.mayankranjandayal.dictionary.TestDictionary;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Mayank Ranjan Dayal");
    }

    /** Called when the user clicks the About button */
    public void displayAbout(View view) {

        Intent intent = new Intent(this, DisplayAboutDetailsActivity.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Generate Error button */
    public void generateError(View view) {

        long num=10/0;
    }
    public void quitApp(View view) {

        finish();
        System.exit(0);
    }
    public void launchTicTacToe(View view)
    {
        Intent intent = new Intent(this,edu.neu.madcourse.mayankranjandayal.tictactoe.MainActivity.class);
        startActivity(intent);
    }

    public void launchDictionary (View view)
    {
        Intent intent = new Intent(this,TestDictionary.class);
        startActivity(intent);
    }

    public void launchWordGame (View view)
    {
        Intent intent = new Intent(this,edu.neu.madcourse.mayankranjandayal.scraggle.ScraggleHome.class);
        startActivity(intent);
    }

    public void launchComm(View view)
    {
        Intent intent = new Intent(this, edu.neu.madcourse.mayankranjandayal.tobedeleted.Communication.class);
        startActivity(intent);

    }

    public void launchTwoPGame(View view) {
        Intent intent = new Intent(this, edu.neu.madcourse.mayankranjandayal.two_player_word_game.twoPlayerWordGameHome.class);
        startActivity(intent);
    }

    public void launchTrickiest(View view){
        try {
            Intent intent = new Intent("com.shlok.tp");
            startActivity(intent);
        }catch(Exception e){
            Toast.makeText(this.getApplicationContext(), "Primary App not installed !", Toast.LENGTH_SHORT).show();
        }
    }

    public void launchProject(View view){

        Intent intent = new Intent(this, BurpeeMainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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