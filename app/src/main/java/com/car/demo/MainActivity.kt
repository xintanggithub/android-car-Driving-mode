package com.car.demo

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.car.util.CarUxRestrictionsHelper
import androidx.car.uxrestrictions.CarUxRestrictions.*
import androidx.car.uxrestrictions.OnUxRestrictionsChangedListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var carHelper: CarUxRestrictionsHelper? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)) {
            return
        }
        carHelper = CarUxRestrictionsHelper(this@MainActivity,
            OnUxRestrictionsChangedListener {
                //如果前景活动需要分散优化，则进行传达。
                val a = it.isDistractionOptimizationRequired
                //对当前驾驶状态有效的Car UX限制的组合。
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
    }

    override fun onResume() {
        super.onResume()
        carHelper?.start()
    }

    override fun onStop() {
        super.onStop()
        carHelper?.stop()
    }

    fun getName(code: Int) = when (code) {
        UX_RESTRICTIONS_FULLY_RESTRICTED -> "UX_RESTRICTIONS_FULLY_RESTRICTED"
        UX_RESTRICTIONS_BASELINE -> "UX_RESTRICTIONS_BASELINE"
        UX_RESTRICTIONS_LIMIT_CONTENT -> "UX_RESTRICTIONS_LIMIT_CONTENT"
        UX_RESTRICTIONS_LIMIT_STRING_LENGTH -> "UX_RESTRICTIONS_LIMIT_STRING_LENGTH"
        UX_RESTRICTIONS_NO_DIALPAD -> "UX_RESTRICTIONS_NO_DIALPAD"
        UX_RESTRICTIONS_NO_FILTERING -> "UX_RESTRICTIONS_NO_FILTERING"
        UX_RESTRICTIONS_NO_KEYBOARD -> "UX_RESTRICTIONS_NO_KEYBOARD"
        UX_RESTRICTIONS_NO_SETUP -> "UX_RESTRICTIONS_NO_SETUP"
        UX_RESTRICTIONS_NO_TEXT_MESSAGE -> "UX_RESTRICTIONS_NO_TEXT_MESSAGE"
        UX_RESTRICTIONS_NO_VIDEO -> "UX_RESTRICTIONS_NO_VIDEO"
        UX_RESTRICTIONS_NO_VOICE_TRANSCRIPTION -> "UX_RESTRICTIONS_NO_VOICE_TRANSCRIPTION"
        else -> " null "
    }


}
