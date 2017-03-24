# ImagePicker
====
Android自定义图片选择器，适配Android7.0
----
<br/>
###效果图:
![](https://github.com/Vanish136/ImagePicker/raw/master/pictures/screen_shot01.png)
![](https://github.com/Vanish136/ImagePicker/raw/master/pictures/screen_shot02.png)
![](https://github.com/Vanish136/ImagePicker/raw/master/pictures/screen_shot03.png)
![](https://github.com/Vanish136/ImagePicker/raw/master/pictures/screen_shot04.png)
<br/>
###使用方法:
1.添加Gradle依赖：
```
dependencies{
         compile 'com.lwkandroid:ImagePicker:1.0.0'
    }
```
<br/>
2.代码中使用：
```
    //发起图片选择
    new ImagePicker.Builder()
                   .pickType(ImagePickType.MUTIL) //设置选取类型(拍照ONLY_CAMERA、单选SINGLE、多选MUTIL)
                   .maxNum(9) //设置最大选择数量(此选项只对多选生效，拍照和单选都是1，修改后也无效)
                   .needCamera(true) //是否需要在界面中显示相机入口(类似微信那样)
                   .cachePath(cachePath) //自定义缓存路径(拍照和裁剪都需要用到缓存)
                   .doCrop(1,1,300,300) //裁剪功能需要调用这个方法，多选模式下无效，参数：aspectX,aspectY,outputX,outputY
                   .displayer(new GlideImagePickerDisplayer()) //自定义图片加载器，默认是Glide实现的,可自定义图片加载器
                   .build()
                   .start(this, REQUEST_CODE, RESULT_CODE); //自定义RequestCode和ResultCode

    //重写Activity或Fragment中OnActivityResult()
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CODE && data != null)
        {
            //获取选择的图片数据
            List<ImageBean> resultList = data.getParcelableArrayListExtra(ImagePicker.INTENT_RESULT_DATA);
        }
    }
```
<br/>
##注意事项
由于Android7.0以上StrictMode策略的存在，本库中拍照使用了FileProvider，如果引用该库的工程中也使用到了FileProvider，为了避免清单文件合并出错，需要做出以下调整：<br/>
1.在主module的`strings.xml`中定义FileProvider的`authorities`,代码如下：
```
//ImagePicker中使用的名字就是app_fileprovider_authorities，这样就可以覆盖掉库中的authorities
<string name="app_fileprovider_authorities">com.sample.fileprovider</string>
```
2.在主module中定义`临时授权目录的xml文件`中添加该库的授权目录，代码如下：
```
<paths>
    <external-path
        name="imagepicker"
        path=""/>
</paths>
```
3.修改主module的`AndroidManiFest`文件中FileProvider模块，代码如下：
```
<provider
      android:name="android.support.v4.content.FileProvider"
      android:authorities="@string/app_fileprovider_authorities"
      android:exported="false"
      android:grantUriPermissions="true"
      tools:replace="android:authorities"> //避免冲突
      <meta-data
          android:name="android.support.FILE_PROVIDER_PATHS"
          android:resource="@xml/fileprovider_path"
          tools:replace="android:resource"/> //避免冲突
</provider>
```
<br/>
<br/>
####感谢
项目中裁剪模块修改自开源项目：https://github.com/oginotihiro/cropview<br/>
感谢所有为开源做出贡献的人！







