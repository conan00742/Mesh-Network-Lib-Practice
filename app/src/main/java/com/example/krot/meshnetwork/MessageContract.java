package com.example.krot.meshnetwork;

import android.app.Activity;

/**
 * Created by Krot on 1/17/18.
 */

public interface MessageContract {

    interface MessageView {
        void setPeers(int peerCount);
        void showMessage(String message);
    }

    interface MessagePresenter {
        void sendMessage(String message);
        void setActivity(Activity activity);
        void startScan();
        void stopScan();
    }
}
