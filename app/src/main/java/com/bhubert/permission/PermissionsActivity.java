package com.bhubert.permission;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;


/**
 * Author: Hubert
 * E-mail: hbh@erongdu.com
 * Date: 2018/3/12 下午8:02
 * <p/>
 * Description:
 */
public class PermissionsActivity extends AppCompatActivity {
    private static final String EXTRA_PERMISSIONS   = "extra_permission"; // 权限参数
    private boolean isRequireCheck; // 是否需要系统权限检测, 防止和系统提示框重叠

    // 启动当前权限页面的公开接口
    public static void startActivityForResult(Activity activity, int requestCode, String... permissions) {
        Intent intent = new Intent(activity, PermissionsActivity.class);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);
        ActivityCompat.startActivityForResult(activity, intent, requestCode, null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || !getIntent().hasExtra(EXTRA_PERMISSIONS)) {
            throw new RuntimeException("PermissionsActivity需要使用静态startActivityForResult方法启动!");
        }
        setContentView(R.layout.activity_permissions);

        isRequireCheck = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRequireCheck) {
            String[] permissions = getPermissions();
            if (!PermissionUtils.checkPermissions(this, permissions)) {
                PermissionUtils.askForPermissions(this, permissions);
            } else {
                PermissionUtils.allPermissionsResult(this, PermissionUtils.PERMISSIONS_GRANTED);
            }
        } else {
            isRequireCheck = true;
        }
    }

    /**
     * 用户权限处理,
     * 如果全部获取, 则直接过.
     * 如果权限缺失, 则提示Dialog.
     *
     * @param requestCode
     *         请求码
     * @param permissions
     *         权限
     * @param grantResults
     *         结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        if (requestCode == PermissionUtils.PERMISSION_REQUEST_CODE &&
                PermissionUtils.hasAllPermissionsGranted(grantResults)) {
            isRequireCheck = true;
            PermissionUtils.allPermissionsResult(this, PermissionUtils.PERMISSIONS_GRANTED);
        } else {
            isRequireCheck = false;
            PermissionUtils.showAskDialog(this);
        }
    }

    // 返回传递的权限参数
    private String[] getPermissions() {
        return getIntent().getStringArrayExtra(EXTRA_PERMISSIONS);
    }
}
