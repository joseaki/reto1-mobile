package com.example.josea.pomodoro

import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.EditText
import android.widget.ProgressBar
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var secondsRemaining = (25 * 60 *1000).toLong()

    private enum class TimerStatus {
        STARTED,
        STOPPED,
        PAUSED
    }

    private var timerStatus = TimerStatus.STOPPED

    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initview()
    }
    private fun initview(){
        imageViewStartStop.setOnClickListener({start_stop()})
        imageViewPause.setOnClickListener({pause()})
    }
    private fun start_stop(){
        if (timerStatus==TimerStatus.STOPPED){
            secondsRemaining=(25 * 60 * 1000).toLong()
            imageViewStartStop.setImageResource(R.drawable.icon_stop)
            imageViewPause.visibility= View.VISIBLE
            setProgressBar()
            timerStatus=TimerStatus.STARTED
            setTimer()
        }else if(timerStatus==TimerStatus.PAUSED){
            imageViewPause.visibility= View.VISIBLE
            imageViewStartStop.setImageResource(R.drawable.icon_stop)
            secondsRemaining=(secondsRemaining*1000)
            timerStatus=TimerStatus.STARTED
            setTimer()
        }else{
            stop()
        }
    }
    private fun pause(){
        imageViewStartStop.setImageResource(R.drawable.icon_start)
        timerStatus=TimerStatus.PAUSED
        countDownTimer!!.cancel()
        imageViewPause.visibility= View.INVISIBLE
    }
    private fun setTimer(){
        countDownTimer = object : CountDownTimer(secondsRemaining, 1000) {
            override fun onFinish() = stop()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished/1000
                updateCountdownUI()
            }
        }.start()
    }
    private fun setProgressBar(){
        progressBarCircle.max=(secondsRemaining/1000).toInt()
    }
    private fun updateCountdownUI(){
        val minutes=(secondsRemaining/60).toInt()
        val seconds=(secondsRemaining%60).toInt()
        textViewTime.text= minutes.toString() + ":" + seconds
        progressBarCircle.progress=(secondsRemaining).toInt()
    }

    private fun stop(){
        if (timerStatus==TimerStatus.STARTED){
            secondsRemaining=(25 * 60 * 1000).toLong()
            updateCountdownUI()
            textViewTime.text="25:00"
            timerStatus=TimerStatus.STOPPED
            imageViewPause.visibility=View.INVISIBLE
            imageViewStartStop.setImageResource(R.drawable.icon_start)
            countDownTimer!!.cancel()
        }
    }
}
