package com.bhubert.permission;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    // 所需的全部权限
    public static final String[] PERMISSIONS              = new String[]{
            Manifest.permission.CAMERA
    };
    public static final int      REQUEST_CODE_PERMISSIONS = 0x9999;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!PermissionUtils.checkPermissions(this, PERMISSIONS)) {
            PermissionsActivity.startActivityForResult(this, REQUEST_CODE_PERMISSIONS, PERMISSIONS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE_PERMISSIONS && resultCode == PermissionUtils.PERMISSIONS_DENIED) {
            finish();
        }
    }
}
