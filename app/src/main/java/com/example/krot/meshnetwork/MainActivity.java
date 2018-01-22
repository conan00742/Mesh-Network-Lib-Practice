package com.example.krot.meshnetwork;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.krot.meshnetwork.model.MessagePresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements MessageContract.MessageView {

    public static final String PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final int PERMISSION_REQUEST_CODE = 102;

    @BindView(R.id.icon_quit_app)
    ImageView mIconQuit;

    @BindView(R.id.tv_message)
    TextView mTvMessage;

    @BindView(R.id.tv_device_count)
    TextView mTvPeerCount;

    @BindView(R.id.edt_message)
    EditText mEdtMessage;

    @BindView(R.id.tv_action_send_message)
    TextView tvActionSendMessage;

    private MessageContract.MessagePresenter messagePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (ContextCompat.checkSelfPermission(this, PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            startActivityForResult(PermissionRequestActivity.getInstance(MainActivity.this, PERMISSION), PERMISSION_REQUEST_CODE);
        }
        messagePresenter = new MessagePresenterImpl(getApplication(),this);
        messagePresenter.setActivity(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        messagePresenter.startScan();
    }


    @Override
    protected void onStop() {
        super.onStop();
        messagePresenter.stopScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                messagePresenter.startScan();
            } else {
                finish();
            }
        }
    }

    @OnClick(R.id.tv_action_send_message)
    public void doSendMessage() {
        messagePresenter.sendMessage(mEdtMessage.getText().toString().trim());
    }

    @Override
    public void setPeers(int peerCount) {
        mTvPeerCount.setText("" + peerCount);
    }

    @Override
    public void showMessage(String message) {
        mTvMessage.setText(message);
    }

}
