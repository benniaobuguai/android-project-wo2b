
## 项目结构
图界传说只是基于此框架下的一个很小的应用场景，[wo2b-common-api] + [wo2b-common-wrapper]本身是一套通用的项目框架，能够让你基于此项目快速搭建属于你自己的项目框架。
>wo2b-project-tu123  
\---wo2b-common-wrapper  
\-----wo2b-common-api  
\-----wo2b-tp-android-support-v7-appcompat  
\-----wo2b-tp-android-support-v7-recyclerview  
\-----wo2b-tp-umeng  


![framework](https://github.com/benniaobuguai/android-project-wo2b/blob/master/wo2b-xxx-design/framework.png)


***
## 项目主要模块设计

+ #### 设计合理的Activity
    ![framework](https://github.com/benniaobuguai/android-project-wo2b/blob/master/wo2b-xxx-design/Activity.png)

+ #### 设计合理的Fragment
    ![framework](https://github.com/benniaobuguai/android-project-wo2b/blob/master/wo2b-xxx-design/Fragment.png)
	
+ #### 风格一致的Dialog
    ![framework](https://github.com/benniaobuguai/android-project-wo2b/blob/master/wo2b-xxx-design/Dialog.png)

+ #### 提供通用的Dao(Data Access Object)
    ![framework](https://github.com/benniaobuguai/android-project-wo2b/blob/master/wo2b-xxx-design/RockyDao.png)

+ #### 其它的待补充


***
## 三步项目跑起来
通过项目结构图，想要让项目运行起来，下载项目代码后，只需要三步。   
    
    
+ wo2b-common-wrapper工程所依赖的工程，wo2b-common-api、wo2b-tp-android-support-v7-appcompat、wo2b-tp-android-support-v7-recyclerview、wo2b-tp-umeng等全部需要转换成“is library”的方式。
![wo2b-common-api](https://github.com/benniaobuguai/android-project-wo2b/blob/master/wo2b-xxx-design/config/wo2b-common-api.png)

+ wo2b-common-wrapper工程通过“add library”添加对上面几个工程的引用，再把自身设置成“is library”。
![wo2b-common-wrapper](https://github.com/benniaobuguai/android-project-wo2b/blob/master/wo2b-xxx-design/config/wo2b-common-wrapper.png)

+ 具体的项目，如当前项目wo2b-project-tu123，通过“add library”添加对com-wo2b-wrapper的引用即可。 
![wo2b-project-tu123](https://github.com/benniaobuguai/android-project-wo2b/blob/master/wo2b-xxx-design/config/wo2b-project-tu123.png)


***
## 重大版本更新日志
1. 基础类名由Rocky***修改成Base***
2. 在BaseFragmentActivity中统一使用Toolbar替换旧版本的ActionBar
3. 基于ViewPager编写了ViewPagerCompat、AutoScrollableView<T>、AutoScrollPoster等通用性组件
4. 引入了Fragment的Lazy Load方案，修复了首页多页加载速度问题。
5. 界面风格进行了大幅度的修改


### 
## 部分截图

<p>
   <img src="https://github.com/benniaobuguai/android-project-wo2b/blob/master/wo2b-xxx-apk/screenshot/1.png" width="270" alt="Screenshot"/>
   <img src="https://github.com/benniaobuguai/android-project-wo2b/blob/master/wo2b-xxx-apk/screenshot/2.png" width="270" alt="Screenshot"/>
   <img src="https://github.com/benniaobuguai/android-project-wo2b/blob/master/wo2b-xxx-apk/screenshot/3.png" width="270" alt="Screenshot"/>
</p>



## 遗留问题
+ 最早前使用了actionbarsherlock，后续替换为Android官方提供的兼容库android.support-v7.appcompat，最新google推荐使用的toolbar目前仅在用户信息部分测试使用，待整合至基类的Activity。
+ 因项目之初，有练手和测试的性质，不可避免地同样的功能会尝试不一样的实现的方式，后续不断优化。在大结构上，还是保持着一致的。
+ wo2b-common-wrapper，整合wo2b系列项目的网络层，并未达到最理想的状态，逐步优化。 
+ 应用图片资源的优化
+ 


## 后续
+ 更多地使用新的api，当然会尽可能地兼容低版本
+ 



## 有问题反馈
