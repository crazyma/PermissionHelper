package com.crazyma.permissionhelper;

import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crazyma.phelper.PermissionHelper;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "crazyma";
    private final int REQUEST_ACCESS_FINE_LOCATION_API = 4;

    private LinearLayout linearLayout;

    private PermissionHelper mPermissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayout = (LinearLayout) findViewById(R.id.linear_layout);

        setupPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setupPermission(){
        mPermissionHelper = new PermissionHelper(this);
        mPermissionHelper.addPermission(ACCESS_FINE_LOCATION);
        mPermissionHelper.setListener(new PermissionHelper.PermissionListener() {
            @Override
            public void allPermissionGranted(int requestCode) {
                if(requestCode == REQUEST_ACCESS_FINE_LOCATION_API){
                    Toast.makeText(MainActivity.this,R.string.toast_granted_msg_1,Toast.LENGTH_LONG).show();
                }

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void somePermissionDenied(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                if (MainActivity.this.shouldShowRequestPermissionRationale(permissions[0])) {
                    //  show rational dialog
                    Log.i(TAG, "onRequestPermissionsResult: permission denied and need to show rational dialog");
                    final int code = requestCode;
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(R.string.dialog_title)
                            .setMessage(R.string.dialog_msg)
                            .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPermissionHelper.requestPermissions(code);
                                }
                            })
                            .setNegativeButton(R.string.dialog_cancel,null)
                            .show();

                } else {
                    //  no rational dialog
                    Log.i(TAG, "onRequestPermissionsResult: permission denied and not rational dialog");
                    Snackbar.make(linearLayout,"You don't have permission",Snackbar.LENGTH_LONG)
                            .setAction("Go to Setting", new View.OnClickListener(){

                                @Override
                                public void onClick(View v) {
                                    PermissionHelper.jump2AppSettings(MainActivity.this);
                                }
                            })
                            .show();
                }
            }
        });
    }

    public void buttonClicked(View v){
        if(mPermissionHelper.checkRequiredPermission(REQUEST_ACCESS_FINE_LOCATION_API))
            Toast.makeText(this,R.string.toast_granted_msg_2,Toast.LENGTH_LONG).show();
    }
}
