package com.bhubert.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

/**
 * Author: Hubert
 * E-mail: hbh@erongdu.com
 * Date: 2018/3/14 下午4:52
 * <p/>
 * Description: 运行时权限校验工具类
 */
public class PermissionUtils {
    private static final String PACKAGE_URL_SCHEME      = "package:"; // 方案
    public static final  int    PERMISSIONS_GRANTED     = 0; // 权限授权
    public static final  int    PERMISSIONS_DENIED      = 1; // 权限拒绝
    public static final  int    PERMISSION_REQUEST_CODE = 0; // 系统权限管理页面的参数

    /**
     * 申请权限
     *
     * @param activity
     *         activity
     * @param permissions
     *         权限列表
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void askForPermissions(Activity activity, String[] permissions) {
        // 如果不是android6.0以上的系统，则不需要检查是否已经获取授权
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }
        if (permissions.length > 0) {
            activity.requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * 校验权限
     *
     * @param context
     *         context
     * @param permissions
     *         权限列表
     *
     * @return 是否授予该权限
     * true - 授予
     * false - 还未授予
     */
    public static boolean checkPermissions(Context context, String[] permissions) {
        // 如果不是android6.0以上的系统，则不需要检查是否已经获取授权
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (!checkPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 校验权限
     *
     * @param context
     *         context
     * @param permission
     *         需要校验的权限
     *
     * @return 是否授予该权限
     * true - 授予
     * false - 还未授予
     */
    public static boolean checkPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        int verify = ContextCompat.checkSelfPermission(context, permission);
        // PackageManager.PERMISSION_GRANTED    授予权限
        // PackageManager.PERMISSION_DENIED     没有权限
        return verify == PackageManager.PERMISSION_GRANTED;
    }

    // 含有全部的权限
    public static boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    // 权限result
    public  static void allPermissionsResult(Activity activity, int permissionCode) {
        activity.setResult(permissionCode);
        activity.finish();
    }

    // 启动应用的设置
    private static void startAppSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + activity.getPackageName()));
        activity.startActivity(intent);
    }


    public static void showAskDialog(final Activity activity) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(activity);
        builder.setTitle(R.string.help);
        builder.setMessage(R.string.string_help_text);

        // 拒绝, 退出应用
        builder.setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                allPermissionsResult(activity,PERMISSIONS_DENIED);
            }
        });

        builder.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings(activity);
            }
        });

        builder.setCancelable(false);

        builder.show();
    }
}
