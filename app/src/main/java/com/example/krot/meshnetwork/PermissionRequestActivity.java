package com.example.krot.meshnetwork;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Krot on 1/19/18.
 */

public class PermissionRequestActivity extends AppCompatActivity {

    private static final String PERMISSION_TAG = "permission";

    static public Intent getInstance(Context context, String permission) {
        Intent intent = new Intent(context, PermissionRequestActivity.class);
        intent.putExtra(PERMISSION_TAG, permission);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent receivedIntent = getIntent();
        String permission = "";
        if (receivedIntent != null) {
            permission = receivedIntent.getStringExtra(PERMISSION_TAG);
        }
        ActivityCompat.requestPermissions(this, new String[] {permission}, 101);
        setResult(RESULT_OK);
        finish();
    }

}
