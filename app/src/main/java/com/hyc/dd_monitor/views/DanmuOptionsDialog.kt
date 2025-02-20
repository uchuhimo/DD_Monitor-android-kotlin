package com.hyc.dd_monitor.views

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.hyc.dd_monitor.R
import com.hyc.dd_monitor.models.PlayerOptions
import kotlin.math.roundToInt

class DanmuOptionsDialog(context: Context, playerId: Int?) : Dialog(context) {

    var playerId: Int? = null

    var onDanmuOptionsChangeListener: ((options: PlayerOptions) -> Unit)? = null

    init {
        this.playerId = playerId
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_danmu_options)

        val title = findViewById<TextView>(R.id.danmu_options_title)

        var playerOptions = PlayerOptions()

        if (playerId != null) {
            title.text = "窗口#${playerId!! +1}弹幕设置"
            context.getSharedPreferences("sp", AppCompatActivity.MODE_PRIVATE).getString("opts$playerId", "")?.let {
                try {
                    Log.d("playeroptions", "load $it")
                    playerOptions = Gson().fromJson(it, PlayerOptions::class.java)
                }catch (e:java.lang.Exception) {

                }
            }
        }else{
            title.text = "全局弹幕设置"
        }



        val switch = findViewById<Switch>(R.id.isShow_switch)
        switch.isChecked = playerOptions.isDanmuShow
        switch.setOnCheckedChangeListener { compoundButton, b ->
            playerOptions.isDanmuShow = b
            onDanmuOptionsChangeListener?.invoke(playerOptions)
        }

        val pos = findViewById<RadioGroup>(R.id.danmu_position)

        when (playerOptions.danmuPosition) {
            0 -> pos.check(R.id.danmu_position_lt)
            1 -> pos.check(R.id.danmu_position_lb)
            2 -> pos.check(R.id.danmu_position_rt)
            3 -> pos.check(R.id.danmu_position_rb)
        }

        pos.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.danmu_position_lt -> playerOptions.danmuPosition = 0
                R.id.danmu_position_lb -> playerOptions.danmuPosition = 1
                R.id.danmu_position_rt -> playerOptions.danmuPosition = 2
                R.id.danmu_position_rb -> playerOptions.danmuPosition = 3
            }
            onDanmuOptionsChangeListener?.invoke(playerOptions)
        }

        val fontsize = findViewById<TextView>(R.id.fontsize_textview)
        fontsize.text = "${playerOptions.danmuSize}"
        findViewById<Button>(R.id.fontsize_add).setOnClickListener {
            val fs = fontsize.text.toString().toInt()
            if (fs + 1 <= 32) {
                fontsize.text = "${fs+1}"
                playerOptions.danmuSize = fs+1
                onDanmuOptionsChangeListener?.invoke(playerOptions)

            }
        }
        findViewById<Button>(R.id.fontsize_minus).setOnClickListener {
            val fs = fontsize.text.toString().toInt()
            if (fs - 1 >= 6) {
                fontsize.text = "${fs-1}"
                playerOptions.danmuSize = fs-1
                onDanmuOptionsChangeListener?.invoke(playerOptions)
            }
        }

        val step = 0.05f
        val width = findViewById<TextView>(R.id.width_textview)
        width.text = "${(playerOptions.danmuWidth*100f).roundToInt()}%"
        findViewById<Button>(R.id.width_add).setOnClickListener {
            val w = playerOptions.danmuWidth
            if (w + step <= 1f) {
                width.text = "${((w+step)*100f).roundToInt()}%"
                playerOptions.danmuWidth = w + step
                onDanmuOptionsChangeListener?.invoke(playerOptions)

            }
        }
        findViewById<Button>(R.id.width_minus).setOnClickListener {
            val w = playerOptions.danmuWidth
            if (w - step >= step) {
//                width.text = String.format("%.1f", w-step)
                width.text = "${((w-step)*100f).roundToInt()}%"
                playerOptions.danmuWidth = w - step
                onDanmuOptionsChangeListener?.invoke(playerOptions)

            }
        }

        val height = findViewById<TextView>(R.id.height_textview)
        height.text = "${(playerOptions.danmuHeight*100f).roundToInt()}%"
        findViewById<Button>(R.id.height_add).setOnClickListener {
            val h = playerOptions.danmuHeight
            if (h + step <= 1f) {
//                height.text = String.format("%.1f", h+step)
                height.text = "${((h+step)*100f).roundToInt()}%"
                playerOptions.danmuHeight = h + step
                onDanmuOptionsChangeListener?.invoke(playerOptions)

            }
        }
        findViewById<Button>(R.id.height_minus).setOnClickListener {
            val h = playerOptions.danmuHeight
            if (h - step >= step) {
                height.text = "${((h-step)*100f).roundToInt()}%"
                playerOptions.danmuHeight = h - step
                onDanmuOptionsChangeListener?.invoke(playerOptions)

            }
        }

        val interpreterStyle = findViewById<RadioGroup>(R.id.interpreter_style)

        when (playerOptions.interpreterStyle) {
            0 -> interpreterStyle.check(R.id.interpreter_hide)
            1 -> interpreterStyle.check(R.id.interpreter_show)
            2 -> interpreterStyle.check(R.id.interpreter_showonly)
        }

        interpreterStyle.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.interpreter_hide -> playerOptions.interpreterStyle = 0
                R.id.interpreter_show -> playerOptions.interpreterStyle = 1
                R.id.interpreter_showonly -> playerOptions.interpreterStyle = 2
            }
            onDanmuOptionsChangeListener?.invoke(playerOptions)
        }

        findViewById<Button>(R.id.dialog_cancel_btn).setOnClickListener {
            dismiss()
        }
    }
}