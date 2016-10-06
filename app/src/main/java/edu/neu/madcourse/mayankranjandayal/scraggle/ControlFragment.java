/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband4 for more book information.
***/
package edu.neu.madcourse.mayankranjandayal.scraggle;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import edu.neu.madcourse.mayankranjandayal.R;

public class ControlFragment extends Fragment {

   ArrayList<String> inputWord;
   GameFragment gameFrg = DataHolder.getInstance().getControlObj();
   int points;
   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {

      View rootView =
            inflater.inflate(R.layout.scraggle_fragment_control, container, false);
      View main = rootView.findViewById(R.id.button_main);
      View restart = rootView.findViewById(R.id.button_restart);
      View check = rootView.findViewById(R.id.button_check);

      //View pause = rootView.findViewById(R.id.button_pause);
      main.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            SharedPreferences myPrefs = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor;
            prefsEditor = myPrefs.edit();
            prefsEditor.putString("REFKEY", "some data");
            prefsEditor.commit();
            getActivity().finish();
         }
      });
      restart.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
            //mSoundPool.play(mSoundRewind, mVolume, mVolume, 1, 0, 1f);
            // ...
            gameFrg.lst_ab= null;
            gameFrg.lst_c = null;
            gameFrg.lst_de= null;
            gameFrg.lst_fgh= null;
            gameFrg.lst_ijkl= null;
            gameFrg.lst_mn= null;
            gameFrg.lst_op= null;
            gameFrg.lst_qr= null;
            gameFrg.lst_s= null;
            gameFrg.lst_tu= null;
            gameFrg.lst_vwxyz = null;
            Intent intent = new Intent(getActivity(), GameActivity.class);
            getActivity().startActivity(intent);
         }
      });

      check.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

            inputWord = DataHolder.getInstance().getArl();
            if(inputWord != null) {
               String word = "";
               for (String s : inputWord) {
                  word += s;
               }

               if (word.length() > 0) {
                  if (wordSearch(word)) {
                     Toast.makeText(getActivity().getApplicationContext(), "Correct Word !", Toast.LENGTH_SHORT).show();
                     points = points + word.length();
                     gameFrg.initTileData();

                  } else
                     Toast.makeText(getActivity().getApplicationContext(), "InCorrect Word !", Toast.LENGTH_SHORT).show();
               }
               else{}
            }
            else {
               Toast.makeText(getActivity().getApplicationContext(), "Please select a word !", Toast.LENGTH_SHORT).show();
            }

         }
      });

      return rootView;
   }

   public boolean wordSearch(String key) {

         char firstChar = key.charAt(0);
         if (firstChar == 'a' || firstChar == 'b') {
            if (Collections.binarySearch(gameFrg.lst_ab, key) >= 0) {
               return true;
            } else return false;
         } else if (firstChar == 'C' || firstChar == 'c') {
            if (Collections.binarySearch(gameFrg.lst_c, key) >= 0) {
               return true;
            } else return false;
         } else if (firstChar == 'D' || firstChar == 'd' || firstChar == 'E' || firstChar == 'e') {
            if (Collections.binarySearch(gameFrg.lst_de, key) >= 0) {
               return true;
            } else return false;
         } else if (firstChar == 'F' || firstChar == 'f' || firstChar == 'G' || firstChar == 'g' || firstChar == 'H' || firstChar == 'h') {
            if (Collections.binarySearch(gameFrg.lst_fgh, key) >= 0) {
               return true;
            } else return false;
         } else if (firstChar == 'I' || firstChar == 'i' || firstChar == 'J' || firstChar == 'j' || firstChar == 'K' || firstChar == 'k' || firstChar == 'L' || firstChar == 'l') {
            if (Collections.binarySearch(gameFrg.lst_ijkl, key) >= 0) {
               return true;
            } else return false;
         } else if (firstChar == 'M' || firstChar == 'm' || firstChar == 'N' || firstChar == 'n') {
            if (Collections.binarySearch(gameFrg.lst_mn, key) >= 0) {
               return true;
            } else return false;
         } else if (firstChar == 'O' || firstChar == 'o' || firstChar == 'P' || firstChar == 'p') {
            if (Collections.binarySearch(gameFrg.lst_op, key) >= 0) {
               return true;
            } else return false;
         } else if (firstChar == 'Q' || firstChar == 'q' || firstChar == 'R' || firstChar == 'r') {
            if (Collections.binarySearch(gameFrg.lst_qr, key) >= 0) {
               return true;
            } else return false;
         } else if (firstChar == 'S' || firstChar == 's') {
            if (Collections.binarySearch(gameFrg.lst_s, key) >= 0) {
               return true;
            } else return false;
         } else if (firstChar == 'T' || firstChar == 't' || firstChar == 'U' || firstChar == 'u') {
            if (Collections.binarySearch(gameFrg.lst_tu, key) >= 0) {
               return true;
            } else return false;
         } else if (firstChar == 'V' || firstChar == 'v' || firstChar == 'W' || firstChar == 'w' || firstChar == 'X' || firstChar == 'x' || firstChar == 'Y' || firstChar == 'y' || firstChar == 'Z' || firstChar == 'z') {
            if (Collections.binarySearch(gameFrg.lst_vwxyz, key) >= 0) {
               return true;
            } else return false;

         } else return false;

      }

}
