# ImagePicker

Android自定义图片选择器，适配Android7.0
----
<br/>
**不知道为什么README文档在Github上排版变混乱了，希望了解该项目的朋友可参考下面的博客**
<br/>
http://blog.csdn.net/lwk520136/article/details/65647033
<br/>
###效果图:
<br />
![](https://github.com/Vanish136/ImagePicker/raw/master/pictures/screen_shot01.png)<br/>
![](https://github.com/Vanish136/ImagePicker/raw/master/pictures/screen_shot02.png)<br/>
![](https://github.com/Vanish136/ImagePicker/raw/master/pictures/screen_shot03.png)<br/>
![](https://github.com/Vanish136/ImagePicker/raw/master/pictures/screen_shot04.png)<br/>
<br/>
###使用方法:
<br/>
1.添加Gradle依赖：
```
dependencies{
         compile 'com.lwkandroid:ImagePicker:1.2.0'
    }
```
<br/>
2.代码中使用：
<br/>
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
###混淆配置<br/>
```
-dontwarn com.lwkandroid.imagepicker.**
-keep class com.lwkandroid.imagepicker.**{*;}
```
<br/>
##注意事项
<br/>
最新版本采用了自定义FileProvider的策略，故不用再修改清单文件。<br/>
参考博客：http://www.cnblogs.com/liushilin/p/6602364.html
<br/>
<br/>
####感谢<br/>
项目中裁剪模块修改自开源项目：https://github.com/oginotihiro/cropview<br/>
感谢所有为开源做出贡献的人！







