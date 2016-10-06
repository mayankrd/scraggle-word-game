package edu.neu.madcourse.mayankranjandayal.dictionary;

import android.app.ListActivity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import java.util.Collections;

import edu.neu.madcourse.mayankranjandayal.R;

public class TestDictionary extends ListActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    ArrayList<String> lst_ab;
    ArrayList<String> lst_c;
    ArrayList<String> lst_de;
    ArrayList<String> lst_fgh;
    ArrayList<String> lst_ijkl;
    ArrayList<String> lst_mn;
    ArrayList<String> lst_op;
    ArrayList<String> lst_qr;
    ArrayList<String> lst_s;
    ArrayList<String> lst_tu;
    ArrayList<String> lst_vwxyz;

    ArrayList<String> listItems;
    ArrayAdapter<String> adapter;
    Uri notification;
    Ringtone beep;
    ToneGenerator tone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_dictionary);
        Intent intent = getIntent();
        listItems = new ArrayList<String>();
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        setListAdapter(adapter);

        this.loadData();

        tone = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);

        final EditText searchBox = (EditText) findViewById(R.id.searchBox);
        searchBox.addTextChangedListener(new TextWatcher() {

            String key;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0)
                {

                key = s.toString();
                char firstChar = s.charAt(0);
                if (s.length() > 2) {

                    if (firstChar == 'a' || firstChar == 'b') {
                        if (Collections.binarySearch(lst_ab, key) >= 0) {
                            listItems.add(key);
                            adapter.notifyDataSetChanged();
                            tone.startTone(ToneGenerator.TONE_PROP_BEEP);
                        }
                    }

                    else if (firstChar == 'C' || firstChar == 'c') {
                        if (Collections.binarySearch(lst_c, key) >= 0) {
                            //tv.setText(key);
                            listItems.add(key);
                            adapter.notifyDataSetChanged();
                            tone.startTone(ToneGenerator.TONE_PROP_BEEP);
                        }
                    }

                    else if (firstChar == 'D' || firstChar == 'd' || firstChar == 'E' || firstChar == 'e') {
                        if (Collections.binarySearch(lst_de, key) >= 0)
                        {
                            listItems.add(key);
                            adapter.notifyDataSetChanged();
                            tone.startTone(ToneGenerator.TONE_PROP_BEEP);
                        }
                    }

                    else if (firstChar == 'F' || firstChar == 'f' || firstChar == 'G' || firstChar == 'g' || firstChar == 'H' || firstChar == 'h') {
                        if (Collections.binarySearch(lst_fgh, key) >= 0)
                        {
                            listItems.add(key);
                            adapter.notifyDataSetChanged();
                            tone.startTone(ToneGenerator.TONE_PROP_BEEP);
                        }
                    }

                    else if (firstChar == 'I' || firstChar == 'i' || firstChar == 'J' || firstChar == 'j' || firstChar == 'K' || firstChar == 'k' || firstChar == 'L' || firstChar == 'l') {
                        if (Collections.binarySearch(lst_ijkl, key) >= 0)
                        {
                            listItems.add(key);
                            adapter.notifyDataSetChanged();
                            tone.startTone(ToneGenerator.TONE_PROP_BEEP);
                        }
                    }
                    else if (s.charAt(0) == 'M' || s.charAt(0) == 'm' || s.charAt(0) == 'N' || s.charAt(0) == 'n') {
                        if (Collections.binarySearch(lst_mn, key) >= 0)
                        {
                            listItems.add(key);
                            adapter.notifyDataSetChanged();
                            tone.startTone(ToneGenerator.TONE_PROP_BEEP);
                        }
                    }

                    else if (s.charAt(0) == 'O' || s.charAt(0) == 'o' || s.charAt(0) == 'P' || s.charAt(0) == 'p') {
                        if (Collections.binarySearch(lst_op, key) >= 0)
                        {
                            listItems.add(key);
                            adapter.notifyDataSetChanged();
                            tone.startTone(ToneGenerator.TONE_PROP_BEEP);
                        }
                    }

                    else if (s.charAt(0) == 'Q' || s.charAt(0) == 'q' || s.charAt(0) == 'R' || s.charAt(0) == 'r') {
                        if (Collections.binarySearch(lst_qr, key) >= 0)
                        {
                            listItems.add(key);
                            adapter.notifyDataSetChanged();
                            tone.startTone(ToneGenerator.TONE_PROP_BEEP);
                        }
                    }

                    else if (s.charAt(0) == 'S' || s.charAt(0) == 's') {
                        if (Collections.binarySearch(lst_s, key) >= 0)
                        {
                            listItems.add(key);
                            adapter.notifyDataSetChanged();
                            tone.startTone(ToneGenerator.TONE_PROP_BEEP);
                        }
                    }

                    else if (s.charAt(0) == 'T' || s.charAt(0) == 't' || s.charAt(0) == 'U' || s.charAt(0) == 'u') {
                        if (Collections.binarySearch(lst_tu, key) >= 0)
                        {
                            listItems.add(key);
                            adapter.notifyDataSetChanged();
                            tone.startTone(ToneGenerator.TONE_PROP_BEEP);
                        }
                    }

                    else if (s.charAt(0) == 'V' || s.charAt(0) == 'v' || s.charAt(0) == 'W' || s.charAt(0) == 'w' || s.charAt(0) == 'X' || s.charAt(0) == 'x' || s.charAt(0) == 'Y' || s.charAt(0) == 'y' || s.charAt(0) == 'Z' || s.charAt(0) == 'z') {
                        if (Collections.binarySearch(lst_vwxyz, key) >= 0)
                        {
                            listItems.add(key);
                            adapter.notifyDataSetChanged();
                            tone.startTone(ToneGenerator.TONE_PROP_BEEP);
                        }
                    }

                }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void clearText(View view)
    {
        listItems.clear();
        adapter.notifyDataSetChanged();
        EditText searchBox = (EditText) findViewById(R.id.searchBox);
        searchBox.setText("");
    }

    public void displayAck(View view)
    {
        Intent intent = new Intent(this,edu.neu.madcourse.mayankranjandayal.Acknowledgement.class);
        startActivity(intent);
    }

    public void returnToMenu(View view)
    {
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public void loadData()
    {
        try {
            String data;
            InputStream ins;
            BufferedReader reader;

            this.lst_ab =  new ArrayList<String>();
            this.lst_c =  new ArrayList<String>();
            this.lst_de =  new ArrayList<String>();
            this.lst_fgh =  new ArrayList<String>();
            this.lst_ijkl = new ArrayList<String>();
            this.lst_mn = new ArrayList<String>();
            this.lst_op = new ArrayList<String>();
            this.lst_qr = new ArrayList<String>();
            this.lst_s = new ArrayList<String>();
            this.lst_tu = new ArrayList<String>();
            this.lst_vwxyz = new ArrayList<String>();

            ins = getResources().openRawResource(R.raw.wordlist_a_b);
            reader = new BufferedReader(new InputStreamReader(ins));
            while ((data = reader.readLine()) != null) {
                lst_ab.add(data);
            }

            ins = getResources().openRawResource(R.raw.wordlist_c);
            reader = new BufferedReader(new InputStreamReader(ins));
            while ((data = reader.readLine()) != null) {
                lst_c.add(data);
            }

            ins = getResources().openRawResource(R.raw.wordlist_d_e);
            reader = new BufferedReader(new InputStreamReader(ins));
            while ((data = reader.readLine()) != null) {
                lst_de.add(data);
            }

            ins = getResources().openRawResource(R.raw.wordlist_f_h);
            reader = new BufferedReader(new InputStreamReader(ins));
            while ((data = reader.readLine()) != null) {
                lst_fgh.add(data);
            }

            ins = getResources().openRawResource(R.raw.wordlist_i_l);
            reader = new BufferedReader(new InputStreamReader(ins));
            while ((data = reader.readLine()) != null) {
                lst_ijkl.add(data);
            }

            ins = getResources().openRawResource(R.raw.wordlist_m_n);
            reader = new BufferedReader(new InputStreamReader(ins));
            while ((data = reader.readLine()) != null) {
                lst_mn.add(data);
            }

            ins = getResources().openRawResource(R.raw.wordlist_o_p);
            reader = new BufferedReader(new InputStreamReader(ins));
            while ((data = reader.readLine()) != null) {
                lst_op.add(data);
            }

            ins = getResources().openRawResource(R.raw.wordlist_q_r);
            reader = new BufferedReader(new InputStreamReader(ins));
            while ((data = reader.readLine()) != null) {
                lst_qr.add(data);
            }

            ins = getResources().openRawResource(R.raw.wordlist_s);
            reader = new BufferedReader(new InputStreamReader(ins));
            while ((data = reader.readLine()) != null) {
                lst_s.add(data);
            }

            ins = getResources().openRawResource(R.raw.wordlist_t_u);
            reader = new BufferedReader(new InputStreamReader(ins));
            while ((data = reader.readLine()) != null) {
                lst_tu.add(data);
            }

            ins = getResources().openRawResource(R.raw.wordlist_v_z);
            reader = new BufferedReader(new InputStreamReader(ins));
            while ((data = reader.readLine()) != null) {
                lst_vwxyz.add(data);
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
