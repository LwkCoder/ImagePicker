# ImagePicker


<br/>

##  Android10以上的注意事项
在Android10以上由于新增了作用域外部存储的访问限制，所以在设置ImagePicker的缓存路径时（拍照、裁剪），不可使用非App作用域的以外的存储路径当作缓存目录，比如以下路径就不能使用：
```
//不可设置这样的缓存路径
Environment.getExternalStorageDirectory().getAbsolutePath()+"/mycache/"
```

否则会发生错误：
```
java.io.FileNotFoundException: /storage/emulated/0/mycache/IMG_1586834402947.jpg: open failed: ENOENT (No such file or directory)
```

进而导致在Activity的onActivityResult()中无法获取返回数据，引起崩溃：
```
2020-04-14 11:20:09.844 13553-13553/com.lwkandroid.imagepicker E/AndroidRuntime: FATAL EXCEPTION: main
    Process: com.lwkandroid.imagepicker, PID: 13553
    java.lang.RuntimeException: Failure delivering result ResultInfo{who=null, request=111, result=-1, data=Intent { (has extras) }} to activity {com.lwkandroid.imagepicker/com.lwkandroid.imagepickerdemo.MainActivity}: java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.String com.lwkandroid.imagepicker.data.ImageBean.toString()' on a null object reference
        at android.app.ActivityThread.deliverResults(ActivityThread.java:5097)
        at android.app.ActivityThread.handleSendResult(ActivityThread.java:5138)
        at android.app.servertransaction.ActivityResultItem.execute(ActivityResultItem.java:51)
        at android.app.servertransaction.TransactionExecutor.executeCallbacks(TransactionExecutor.java:135)
        at android.app.servertransaction.TransactionExecutor.execute(TransactionExecutor.java:95)
        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:2147)
        at android.os.Handler.dispatchMessage(Handler.java:107)
        at android.os.Looper.loop(Looper.java:237)
        at android.app.ActivityThread.main(ActivityThread.java:7811)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:493)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1076)
     Caused by: java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.String com.lwkandroid.imagepicker.data.ImageBean.toString()' on a null object reference
        at com.lwkandroid.imagepickerdemo.MainActivity.onActivityResult(MainActivity.java:91)
        at android.app.Activity.dispatchActivityResult(Activity.java:8292)
        at android.app.ActivityThread.deliverResults(ActivityThread.java:5090)
        at android.app.ActivityThread.handleSendResult(ActivityThread.java:5138) 
        at android.app.servertransaction.ActivityResultItem.execute(ActivityResultItem.java:51) 
        at android.app.servertransaction.TransactionExecutor.executeCallbacks(TransactionExecutor.java:135) 
        at android.app.servertransaction.TransactionExecutor.execute(TransactionExecutor.java:95) 
        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:2147) 
        at android.os.Handler.dispatchMessage(Handler.java:107) 
        at android.os.Looper.loop(Looper.java:237) 
        at android.app.ActivityThread.main(ActivityThread.java:7811) 
        at java.lang.reflect.Method.invoke(Native Method) 
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:493) 
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:1076) 
```

### 解决方案：
#### 1.AndroidManifest中添加`android:requestLegacyExternalStorage="true"`
ImagePicker在1.4.5的版本中已经在AndroidManifest中添加了`android:requestLegacyExternalStorage="true"`，但这是一种临时解决方案。
#### 2.使用App专属的缓存路径
ImagePicker在1.4.6中将原来默认的缓存路径改为`context.getExternalFilesDir(Environment.DIRECTORY_DCIM).getAbsolutePath()`，并且希望大家在使用的时候，如果要自定义缓存路径，请尽量使用作用域内的地址。

<br/>
<br/>

**希望了解该项目的朋友可参考下面的博客**
<br/>
http://blog.csdn.net/lwk520136/article/details/65647033 <br/>
http://www.jianshu.com/p/46b5918976e1 <br />

### 效果图:
![](https://github.com/Vanish136/ImagePicker/raw/master/pictures/screen_shot01.png)<br/>
![](https://github.com/Vanish136/ImagePicker/raw/master/pictures/screen_shot02.png)<br/>
![](https://github.com/Vanish136/ImagePicker/raw/master/pictures/screen_shot03.png)<br/>
![](https://github.com/Vanish136/ImagePicker/raw/master/pictures/screen_shot04.png)<br/>

### 使用方法:
**1.添加Gradle依赖：**

最新的版本号以[这里](https://github.com/Vanish136/ImagePicker/releases)为准

```
  #your-version是指的你自己项目里引用的版本号
  #last-version请查看上面的最新版本号

  #AndroidStudio3.0以下
  //在自己项目里添加Glide的引用（Glide要求4.0版本以上）
  compile("com.github.bumptech.glide:glide:your-version") {
      exclude(group: 'com.android.support', module: 'support-v4')
  }
  //在自己项目里添加support-V4的引用
  compile "com.android.support:support-v4:your-version"
  //添加该库引用，并去除自带的support-V4、Glide引用，防止版本冲突
  compile("com.lwkandroid:ImagePicker:last-verison") {
      exclude(group: 'com.android.support', module: 'support-v4')
      exclude(group: 'com.github.bumptech.glide', module: 'glide')
  }

  #AndroidStudio3.0以上
  //在自己项目里添加Glide的引用（Glide要求4.0版本以上）
  implementation("com.github.bumptech.glide:glide:your-version") {
      exclude(group: 'com.android.support', module: 'support-v4')
  }
  //在自己项目里添加support-V4的引用
  implementation "com.android.support:support-v4:your-version"
  //添加该库引用，并去除自带的support-V4、Glide引用，防止版本冲突
  implementation("com.lwkandroid:ImagePicker:last-verison") {
      exclude(group: 'com.android.support', module: 'support-v4')
      exclude(group: 'com.github.bumptech.glide', module: 'glide')
  }
```
- 注意：如果自己项目里的`Glide`版本低于4.0，那么需要实现接口`IImagePickerDisplayer`,调用的时候传进去：
 ```
 //代码中实现IImagePickerDisplayer接口
 public class YourDisplayer implements IImagePickerDisplayer
 {
     @Override
     public void display(Context context, String url, ImageView imageView, int maxWidth, int maxHeight)
     {
         //TODO 实现加载图片的方法
     }

     @Override
     public void display(Context context, String url, ImageView imageView, int placeHolder, int errorHolder, int maxWidth, int maxHeight)
     {
         //TODO 实现加载图片的方法
     }
 }

 //调用的时候手动添加.displayer()
     new ImagePicker()
         ... //省略配置参数
         .displayer(new YourDisplayer()) //改为自定义图片加载器，必须调用!!!
         .start(this, REQUEST_CODE); //自定义RequestCode
 ```
<br />

**2.代码中使用：**

```
    //发起图片选择
    new ImagePicker()
        .pickType(ImagePickType.MUTIL) //设置选取类型(拍照ONLY_CAMERA、单选SINGLE、多选MUTIL)
        .maxNum(9) //设置最大选择数量(此选项只对多选生效，拍照和单选都是1，修改后也无效)
        .needCamera(true) //是否需要在界面中显示相机入口(类似微信那样)
        .cachePath(cachePath) //自定义缓存路径(拍照和裁剪都需要用到缓存)
        .doCrop(1,1,300,300) //裁剪功能需要调用这个方法，多选模式下无效，参数：aspectX,aspectY,outputX,outputY
        .displayer(new GlideImagePickerDisplayer()) //自定义图片加载器，默认是Glide实现的,可自定义图片加载器
        .start(this, REQUEST_CODE); //自定义RequestCode

    //重写Activity或Fragment中OnActivityResult()
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null)
        {
            //获取选择的图片数据
            List<ImageBean> resultList = data.getParcelableArrayListExtra(ImagePicker.INTENT_RESULT_DATA);
        }
    }
```
<br />

### 混淆配置

```
-dontwarn com.lwkandroid.imagepicker.**
-keep class com.lwkandroid.imagepicker.**{*;}
```
<br />

## 注意事项

最新版本采用了自定义FileProvider的策略，故不用再修改清单文件。<br/>
参考博客：http://www.cnblogs.com/liushilin/p/6602364.html
<br/>

#### 感谢
项目中裁剪模块修改自开源项目：https://github.com/oginotihiro/cropview<br/>
感谢所有为开源做出贡献的人！







