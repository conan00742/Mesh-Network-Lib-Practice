package com.example.krot.meshnetwork.model;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.example.krot.meshnetwork.MainActivity;
import com.example.krot.meshnetwork.MessageContract;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import io.underdark.Underdark;
import io.underdark.transport.Link;
import io.underdark.transport.Transport;
import io.underdark.transport.TransportKind;
import io.underdark.transport.TransportListener;

/**
 * Created by Krot on 1/19/18.
 */

public class MessagePresenterImpl extends AndroidViewModel implements TransportListener, MessageContract.MessagePresenter{

    private List<Link> links = new ArrayList<>();
    private String message;
    private Transport transport;
    private long nodeId;
    private MessageContract.MessageView messageView;
    private boolean isRunning;
    private Activity activity;
    private static final int APP_ID = 1359033;

    public MessagePresenterImpl(@NonNull Application application, MessageContract.MessageView messageView) {
        super(application);
        this.messageView = messageView;
        do {
            nodeId = new Random().nextLong();
        } while (nodeId == 0);

        if (nodeId < 0) {
            nodeId = -nodeId;
        }

        EnumSet<TransportKind> transportKinds = EnumSet.of(TransportKind.BLUETOOTH, TransportKind.WIFI);

        transport = Underdark.configureTransport(APP_ID, nodeId,
                this,
                null,
                application,
                transportKinds);

    }



    @Override
    public void transportNeedsActivity(Transport transport, ActivityCallback callback) {
        callback.accept(activity);
    }

    @Override
    public void transportLinkConnected(Transport transport, Link link) {
        links.add(link);
        messageView.setPeers(links.size());
    }

    @Override
    public void transportLinkDisconnected(Transport transport, Link link) {
        links.remove(link);
        messageView.setPeers(links.size());
        if (links.isEmpty()) {
            messageView.showMessage("");
        }
    }

    @Override
    public void transportLinkDidReceiveFrame(Transport transport, Link link, byte[] frameData) {
        message = new String(frameData);
        messageView.showMessage(message);
    }

    @Override
    public void sendMessage(String message) {
        byte[] data = message.getBytes();
        if (links.isEmpty()) {
            return;
        }

        for (Link link : links) {
            link.sendFrame(data);
        }
    }


    @Override
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void startScan() {
        if (isRunning) {
            return;
        }

        isRunning = true;
        transport.start();
    }

    @Override
    public void stopScan() {
        if (!isRunning) {
            return;
        }

        isRunning = false;
        transport.stop();
    }
}
