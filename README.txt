# README——DEMO

## Features

### 项目概述

本项目为北京理工大学本科2017级软件工程课程实践的作品——手机防迟到App。本项目旨在开发一款轻量级的功能性手机应用，为用户的时间管理提供更好的策略，帮助用户减少迟到的可能性。

- Application name: 爷来啦
- Package name: edu.bit.mecoming
- Version: 1.0
- Size: 10.24MB

### 功能简介

- 命名并创建待办事项
- 添加待办事项地点，实时测算待办事项地点距用户位置的步行时间
- 轻量级地图搜索功能，用户可以随时查看/搜索地图
- 排列待办事项，全局显示当前所有代办信息
- 查看待办事项详细时间/地点/备注
- 待办事项智能后台提醒，从此不再迟到

## Index Description

- app
  - build
  - libs
    - fastjson-1.2.62.jar                                       &emsp;//json解码使用的库
    - TencentLocationSdk_v7.2.4_r3f64968c_20190828_105426.jar   &emsp;//腾讯定位SDK
    - TencentMapSDK_Raster_v_1.3.4.jar                          &emsp;//腾讯地图SDK
    - tencent-mapsdk-library-release-4.3.0.316238ae.jar         &emsp;//腾讯地图SDK
  - src                           &emsp;//应用资源文件夹
    - main                        &emsp;//主程序文件夹
      - java                      &emsp;//java文件夹，存放所有的java文件
        - edu.bit.mecoming        &emsp;//Java包名
          - algorithm             &emsp;//算法文件夹
            - MainService.java    &emsp;//主服务类，一直在后台挂起，计算路程等
            - TodoEvent.java      &emsp;//待办事项类
          - managers              &emsp;//功能模块文件夹
            - AlarmManager.java   &emsp;//提醒管理，由谢楚云开发维护
            - LocationUtils.java  &emsp;//定位工具，由严霜开发维护
            - MapManager.java     &emsp;//地图及路程计算管理，由危昊成开发维护
            - XmlFileManager.java &emsp;//文件管理，由严霜开发维护
          - ui
            - main
              - adapter
                - LvAdapter.java  &emsp;//ListView的adapter，用于适配主界面的事件列表
              - TodoListFragment.java &emsp;//主界面碎片
          - AddActivity.java      &emsp;//添加事件活动
          - DeleteActivity.java   &emsp;//删除事件活动
          - MainActivity.java     &emsp;//主活动
          - MapMainActivity.java  &emsp;//选择地点活动
      - res
        - layout
          - activity_add.xml      &emsp;//添加事件活动布局文件
          - activity_main.xml     &emsp;//主活动布局文件
          - fragment_todolist.xml &emsp;//待办列表碎片布局
          - delete.xml            &emsp;//删除活动布局文件
          - list_item.xml         &emsp;//ListView适配器样式文件
          - map_activity_main.xml &emsp;//寻找地点活动布局文件
      - AndroidManifest.xml       &emsp;//Android应用清单
    - build
    - gradle

## Dependencies

使用了腾讯地图SDK，腾讯定位SDK

## User Guide

### [教程]v1.0用户使用不完全指北

#### 安装

##### 安装包位置

v1.0 release版本的安装包目录为`$\app\release\app-release.apk`

也可以在[百度网盘](https://pan.baidu.com/s/16_NyCUaW4KulLIr0x7_i3Q)中下载，提取码: 7hj3

##### 权限信息

```xml
<!--腾讯地图 SDK 要求的权限(开始)-->
<!--访问网络获取地图服务-->
<uses-permission android:name="android.permission.INTERNET"/>
<!--检查网络可用性-->
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<!-- 访问WiFi状态 -->
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<!--需要外部存储写权限用于保存地图缓存-->
<uses-permission android:name="android.permission.VIBRATE" />
<!--获取 device id 辨别设备-->
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
<!--腾讯地图 SDK 要求的权限(结束)-->

<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<!-- Android Q新增权限，允许应用在后台发起定位，如应用target为Q，请添加此权限 -->
<uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
<!--如果设置了target >= 28 如果需要启动后台定位则必须声明这个权限-->
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<!-- 通过GPS得到精确位置 -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<!-- 通过网络得到粗略位置 -->
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<!-- 修改WiFi状态，发起WiFi扫描, 需要WiFi信息用于网络定位 -->
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
<!-- 访问网络的变化, 需要某些信息用于网络定位 -->
<uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
```
#### 使用

##### 创建待办事项

点击主页面下方“+”按钮，即可创建新的待办

##### 输入代办地点

点击“新增待办事项”——“代办地点”——“点击添加地点”，即可进入地点选择界面

在地点选择界面输入目标地点并点击确认，以存储目标地点

##### 查看/移除待办事项

在主页面点击新添加的待办事项可进入查看页面

查看页面具体显示了单个代办的名称/时间/地点

在查看页面下方，你可以：

- 点击左侧按钮返回主界面
- 点击右侧按钮删除本项待办

## License

没有License和CopyRight，这个项目可以是开源的

## Authors

开发者：严霜、危昊成、王奥博、谢楚云、陈涛

对于项目的意见和建议，欢迎随时与我们沟通，联系方式：

Email：<haochengwei_whc@outlook.com>

QQ：1044113683

