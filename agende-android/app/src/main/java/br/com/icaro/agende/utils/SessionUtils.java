package br.com.icaro.agende.utils;

import android.content.Context;

import br.com.icaro.agende.R;
import br.com.icaro.agende.model.JWToken;

public class SessionUtils {

    private static final String EMPTY = "";

    public static void writeToken(JWToken token, Context context) {
        String access, refresh;

        if (token != null) {
            access = token.getAccess();
            refresh = token.getRefresh();
        }
        else {
            access = EMPTY;
            refresh = EMPTY;
        }
        SharedPreferencesUtils.writeInSharedPreferences(String.valueOf(R.string.access_token), access, context);
        SharedPreferencesUtils.writeInSharedPreferences(String.valueOf(R.string.refresh_token), refresh, context);
    }

    public static JWToken readToken(Context context){
        String access = SharedPreferencesUtils.readOfSharedPreferences(String.valueOf(R.string.access_token), context);
        String refresh = SharedPreferencesUtils.readOfSharedPreferences(String.valueOf(R.string.refresh_token), context);
        return (access.equals(EMPTY) && refresh.equals(EMPTY))? null : new JWToken(access, refresh);
    }

    public static void writeUser(String username, Context context){
        if(username != null){
            SharedPreferencesUtils.writeInSharedPreferences(String.valueOf(R.string.logged_user), username, context);
        }
    }

    public static String readUser(Context context){
        String username = SharedPreferencesUtils.readOfSharedPreferences(String.valueOf(R.string.logged_user), context);
        return (username.equals(EMPTY))? null : username;
    }

    public static boolean isLogged(Context context){
        return SessionUtils.readToken(context) != null;
    }
}
