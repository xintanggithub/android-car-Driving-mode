# android car Driving mode Demo

![image](https://github.com/xintanggithub/android-car-Driving-mode/blob/master/1.png)
![image](https://github.com/xintanggithub/android-car-Driving-mode/blob/master/2.png)

### 参考文档

- [CarUxRestrictionsManager](https://developer.android.com/reference/android/car/drivingstate/CarUxRestrictionsManager)

- [CarUxRestrictions](https://developer.android.com/reference/androidx/car/uxrestrictions/CarUxRestrictions)

### 引用

```
    implementation 'com.android.support:car:28.0.0-alpha5'
    implementation 'androidx.car:car-cluster:1.0.0-alpha5'
```

#### 注意

- androidx和support库是不兼容的，需要将support库转为Androidx，Android studio有提供该功能。[操作参考](https://www.jianshu.com/p/41de8689615d)

- AndroidManifest.xml配置 一定！一定！一定！要记得加这个meta-data属性，是所有的activity，这个是告诉系统，这个activity的驾驶模式由我自己来处理
    ```
            <activity android:name=".MainActivity">
                <intent-filter>
                    <action android:name="android.intent.action.MAIN" />
    
                    <category android:name="android.intent.category.LAUNCHER" />
                </intent-filter>
                <!-- 下面的meta-data一定要加上->
                <meta-data
                    android:name="distractionOptimized"
                    android:value="true" />
            </activity>
    ```

### 使用简介

#### 1. 使用CarUx辅助类 CarUxRestrictionsHelper

```
lateinit var carHelper: CarUxRestrictionsHelper
```

#### 2. 添加驾驶模式变化监听 OnUxRestrictionsChangedListener

```
        carHelper = CarUxRestrictionsHelper(this@MainActivity,
            OnUxRestrictionsChangedListener {
                val a = it.isDistractionOptimizationRequired
                val b = it.activeRestrictions
                //获取允许的最大内容深度级别数或通过单个任务中的任意一个路径查看遍历
                val c = it.maxContentDepth
                //获取在通过单个任务中的任何一个路径进行遍历期间可以向用户显示的最大允许内容项数量
                val d = it.maxCumulativeContentItems
                //获取UX_RESTRICTIONS_LIMIT_STRING_LENGTH施加时可显示的通用字符串的最大长度 。
                val e = it.maxRestrictedStringLength

                tv_content.text = " 驾驶模式限制是否开启：$a \n UX限制的组合code: $b【${getName(b)}】 \n 最大内容深度级别：$c" +
                    "  \n 可向用户显示的最大允许内容：$d \n 长文本限制时可显示的最大长度：$e"
            })
```

##### 注意

- 绑定了OnUxRestrictionsChangedListener之后，CarUxRestrictionsHelper会马上回调一次，本次回调内容为当前的信息

- isDistractionOptimizationRequired: 如果前景活动需要分散优化，则进行传达,true代表需要做分心处理，false为不需要（不需要可能是驾驶模式没有开启、或者停车等状态）

- activeRestrictions: 对当前驾驶状态有效的Car UX限制的组合 不同的flag代表不同的限制类型

  ```
  
  //No specific restrictions in place, but baseline distraction optimization guidelines need to be adhered to when isDistractionOptimizationRequired() is true.
  int	UX_RESTRICTIONS_BASELINE
  
  //All the above restrictions are in effect.
  int	UX_RESTRICTIONS_FULLY_RESTRICTED
  
  //Limit the number of items displayed on the screen.
  int	UX_RESTRICTIONS_LIMIT_CONTENT
  
  /General purpose strings length cannot exceed the character limit provided by getMaxRestrictedStringLength()
  int	UX_RESTRICTIONS_LIMIT_STRING_LENGTH
  
  //No dialpad for the purpose of initiating a phone call.
  int	UX_RESTRICTIONS_NO_DIALPAD
  
  //No filtering a list.
  int	UX_RESTRICTIONS_NO_FILTERING
  
  //No text entry for the purpose of searching etc.
  int	UX_RESTRICTIONS_NO_KEYBOARD
  
  //No setup that requires form entry or interaction with external devices.
  int	UX_RESTRICTIONS_NO_SETUP
  
  //No Text Message (SMS, email, conversational, etc.)
  int	UX_RESTRICTIONS_NO_TEXT_MESSAGE
  
  //No video - no animated frames > 1fps.
  int	UX_RESTRICTIONS_NO_VIDEO
  
  //No text transcription (live or leave behind) of voice can be shown.
  int	UX_RESTRICTIONS_NO_VOICE_TRANSCRIPTION
  ```
  
#### 3. CarUxRestrictionsHelper的start和stop
  
```
    override fun onResume() {
        super.onResume()
        carHelper.start()
    }

    override fun onPause() {
        super.onPause()
        carHelper.stop()
    }
```
  
  - start 一般在活动界面刷新时使用（如果界面存在刷新的可能，如activity，则建议在onResume()）
  
  - stop 一般在界面停止活动的时候使用 (如activity，则建议在onPause())
