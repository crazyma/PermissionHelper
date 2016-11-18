PermissionHelper
===
Make you permission setting more easier for post-Android6.0 devices.

Setting
===
in app `build.gradle`
``` XML
repositories {
    maven {
        url  "http://dl.bintray.com/badu/maven"
    }
}

...

dependencies {
    ...
    compile 'com.crazyma.phelper:phelper:1.0.3'
}
```

How To
===
> step 1: create Permission Helper and add required permission

``` java
PermissionHelper mPermissionHelper 
        = new PermissionHelper(activity);
/* or   = new PermissionHelper(fragment);  */

/*    put whatever permission you want    */
mPermissionHelper.addPermission(ACCESS_FINE_LOCATION);
mPermissionHelper.addPermission(CAMERA);

```

> step 2: setup Listener

``` java
mPermissionHelper.setListener(new PermissionHelper.PermissionListener() {
            @Override
            public void allPermissionGranted(int requestCode) {
                if(requestCode == Your_Request_Code){
                    
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void somePermissionDenied(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                /*    if there is any permission denied, it would call this method    */
                
                if (MainActivity.this.shouldShowRequestPermissionRationale(permissions[0])) {
                    //  show rational message about requesting permission
                    
                } else {
                    //  show permission denied message
                    
                }
            }
        });
```

> step 3: setup permission result receiver from Activity or Fragment

``` java
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
}
```


> step 4: call check method in wherever you want

``` java

checkAndRequestRequiredPermission(Your_Request_Code);

/* if you just want to know the permission checked or not,
   you don't want to request it,
   call this method    
*/   
mPermissionHelper.checkRequiredPermission();
   
```
