# APermissionUtils
#### Android6.0 运行时权限处理工具类, PermissionUtils处理权限，PermissionsActivity处理Activity所需权限并反馈处理结果
##### 以相机为例，首先在 Manifest 文件中定义好相机权限：
<pre>
 < uses-permission android:name="android.permission.CAMERA"\>
</pre>
##### 注册 PermissionsActivity
<pre>
< activity android:name=".PermissionsActivity"/>
</pre>
* 第一步 在Activity onResume() 中判断所需权限
<pre>
@Override
protected void onResume() {
     super.onResume();
     if (!PermissionUtils.checkPermissions(this, PERMISSIONS)) {
         PermissionsActivity.startActivityForResult(this, REQUEST_CODE_PERMISSIONS, PERMISSIONS);
     }
 }
 </pre>
 
 * 第二步 在Activity onActivityResult()中处理权限拒绝
 <pre>
 @Override
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
     // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
     if (requestCode == REQUEST_CODE_PERMISSIONS && resultCode == PermissionUtils.PERMISSIONS_DENIED) {
         finish();
     }
 }
 </pre>
