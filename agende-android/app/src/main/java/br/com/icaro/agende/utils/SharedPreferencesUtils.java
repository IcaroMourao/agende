package br.com.icaro.agende.utils;

import android.content.Context;
import android.content.SharedPreferences;

import br.com.icaro.agende.R;

public class SharedPreferencesUtils {

    private static final String EMPTY = "";

    public static void writeInSharedPreferences(String key, String value) {
        SharedPreferences preferences = AgendeApplicationUtils.getAgendeAppContext().getSharedPreferences(String.valueOf(R.string.preference_private), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        if (key == null) {
            return;
        }
        else {
            if(value == null){
                value = EMPTY;
            }
            edit.putString(String.valueOf(key), String.valueOf(value));
            edit.apply();
        }
    }

    public static String readOfSharedPreferences(String key){
        SharedPreferences preferences = AgendeApplicationUtils.getAgendeAppContext().getSharedPreferences(String.valueOf(R.string.preference_private), Context.MODE_PRIVATE);
        String value = preferences.getString(String.valueOf(key), EMPTY);
        return value;
    }

}
