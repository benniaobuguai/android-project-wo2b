
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


## 有问题反馈
