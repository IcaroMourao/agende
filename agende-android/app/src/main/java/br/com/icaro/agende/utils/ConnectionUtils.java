package br.com.icaro.agende.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import br.com.icaro.agende.BuildConfig;


public class ConnectionUtils {

    private static boolean hasInternetConnection(){
        ConnectivityManager cm = (ConnectivityManager)AgendeApplicationUtils.getAgendeAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private static boolean isConnectedToServer(){
        Socket sock = new Socket();
        try {
            sock.connect(new InetSocketAddress(BuildConfig.SERVER_URL.split(":")[0], 8080), 1500);
            sock.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean hasConnectionAndIsConnectedToServer(){
        return hasInternetConnection() && isConnectedToServer();
    }

}
