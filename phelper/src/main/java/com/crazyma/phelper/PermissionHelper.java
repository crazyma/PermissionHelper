package com.crazyma.phelper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 2016/11/18.
 */

public class PermissionHelper {
    //  if your just want to check permission and don't request, use the requestCode
    final static public int REQUEST_NOT_REQUIRING_PERMISSION = 0;

    private Fragment fragment;
    private AppCompatActivity activity;
    private Context context;
    private List<String> permissionList;
    private PermissionListener mListener;

    public static void jump2AppSettings(Context context){
        if(context != null) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.getPackageName(), null));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public PermissionHelper(Fragment fragment) {
        this.fragment = fragment;
        context = fragment.getActivity();
        permissionList = new ArrayList<>();
    }

    public PermissionHelper(AppCompatActivity activity) {
        this.activity = activity;
        context = activity;
        permissionList = new ArrayList<>();
    }

    public void addPermission(String permission){
        permissionList.add(permission);
    }

    public void addPermissions(List<String> list){
        permissionList.addAll(list);
    }

    public PermissionListener getListener() {
        return mListener;
    }

    public void setListener(PermissionListener listener) {
        this.mListener = listener;
    }

    public void requestPermissions(int requestCode){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            if (activity != null) {
                activity.requestPermissions(permissions, requestCode);
            }else if (fragment != null) {
                fragment.requestPermissions(permissions, requestCode);
            }
        }
    }

    public boolean checkRequiredPermission(int requestCode){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(context != null){
                for (String permission : permissionList) {
                    if(ContextCompat.checkSelfPermission(context,permission)
                            == PackageManager.PERMISSION_DENIED){
                        //  request permissions or not
                        if(REQUEST_NOT_REQUIRING_PERMISSION != requestCode)
                            requestPermissions(requestCode);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        boolean somethingDenied = false;
        for (int grantResult : grantResults) {
            if(grantResult == PackageManager.PERMISSION_DENIED){
                somethingDenied = true;
                break;
            }
        }
        if(mListener != null)
            if(somethingDenied){
                mListener.somePermissionDenied(requestCode, permissions, grantResults);
            }else{
                mListener.allPermissionGranted(requestCode);
            }
    }

    public interface PermissionListener{
        void allPermissionGranted(int requestCode);
        void somePermissionDenied(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
    }

}
