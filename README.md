# ImagePicker

Android自定义图片选择器，适配Android7.0
----
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

```
dependencies{
         compile 'com.lwkandroid:ImagePicker:1.3.1'
    }
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
<br/>

### 解决冲突

**库中有依赖`SupportV4` 、`Glide`，为防止和主项目冲突，可参考以下做法：** <br />

1.对于`SupportV4`，该库中引用的是`25.3.1`版本，解决冲突可以尝试两种做法： <br />

①可以选择在gradle中引用时exclude掉该库中的V4包：
```
compile('com.lwkandroid:ImagePicker:1.3.1') {
        exclude module: 'support-v4'
    }
```
②在项目的`gradle.properties`文件中添加版本号指定：
```
LIB_ANDROID_SUPPORT_VERSION=25.3.1 //这里指定为你自己的Support版本号
```
<br />

2.对于`Glide`，该库中使用的是`4.0.0-RC1`版本，解决冲突可以尝试两种做法： <br />
①可以选择在gradle中引用时exclude掉该库中的Glide包：
```
compile('com.lwkandroid:ImagePicker:1.3.1') {
        exclude module: 'glide'
    }
```
**使用这种方式就必须在代码中手动调用`.displayer(Your displayer)` !!!**

②在项目的`gradle.properties`文件中添加版本号指定：
```
LIB_GLIDE_VERSION=4.0.0-RC1 //这里指定为你自己的版本号
```
**这里要注意，Glide3.X版本和4.X版本api有变动，无法互相兼容，如果主项目使用的是3.X版本，这种方式是无效的，会导致崩溃！**

### 混淆配置

```
-dontwarn com.lwkandroid.imagepicker.**
-keep class com.lwkandroid.imagepicker.**{*;}
```
<br/>

## 注意事项

最新版本采用了自定义FileProvider的策略，故不用再修改清单文件。<br/>
参考博客：http://www.cnblogs.com/liushilin/p/6602364.html
<br/>

#### 感谢
项目中裁剪模块修改自开源项目：https://github.com/oginotihiro/cropview<br/>
感谢所有为开源做出贡献的人！







