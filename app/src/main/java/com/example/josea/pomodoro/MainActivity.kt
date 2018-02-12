package com.example.josea.pomodoro

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.graphics.Color
import android.opengl.Visibility
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.EditText
import android.widget.ProgressBar
import android.os.CountDownTimer
import android.support.annotation.RequiresApi
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import org.jetbrains.anko.*
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.support.v4.app.NotificationCompat


class MainActivity : AppCompatActivity() {
    private var notificationManager: NotificationManager? = null

    private val originalMinutes = 1
    private val breakSeconds=(1*60*1000).toLong()


    private var secondsRemaining = (originalMinutes * 60 *1000).toLong()

    private enum class TimerStatus {
        STARTED,
        STOPPED,
        PAUSED
    }
    private enum class TaskStatus{
        BREAK,
        WORK
    }

    private var timerStatus = TimerStatus.STOPPED
    private var taskStatus=TaskStatus.WORK
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initview()
    }
    private fun initview(){
        setProgressBar()
        imageViewStartStop.setOnClickListener({start_stop()})
        imageViewPause.setOnClickListener({pause()})
    }
    private fun start_stop(){
        if (timerStatus==TimerStatus.STOPPED){
            secondsRemaining=(originalMinutes * 60 * 1000).toLong()
            imageViewStartStop.setImageResource(R.drawable.icon_stop)
            imageViewPause.visibility= View.VISIBLE
            setProgressBar()
            timerStatus=TimerStatus.STARTED
            setTimer(secondsRemaining)
        }else if(timerStatus==TimerStatus.PAUSED){
            imageViewPause.visibility= View.VISIBLE
            imageViewStartStop.setImageResource(R.drawable.icon_stop)
            secondsRemaining=(secondsRemaining*1000)
            timerStatus=TimerStatus.STARTED
            setTimer(secondsRemaining)
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
    private fun setTimer(secondsTimer:Long){
        countDownTimer = object : CountDownTimer(secondsTimer, 1000) {
            override fun onFinish() = timerFinish()
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
    private fun timerFinish(){
        if(taskStatus==TaskStatus.WORK){
            taskStatus=TaskStatus.BREAK
            timerStatus=TimerStatus.STOPPED
            var x=alert( "Es tiempo de un descanso","Tiempo!!") {
                yesButton {breakTime()}
            }.build()
            x.setCancelable(false)
            x.setCanceledOnTouchOutside(false)
            x.show()
        }else{
            taskStatus=TaskStatus.WORK
            val x=alert( "Tu descanso termino, A trabajar!!","Tiempo!!") {
                yesButton {start_stop()}
            }.build()
            x.setCancelable(false)
            x.setCanceledOnTouchOutside(false)
            x.show()
        }
    }

    private fun breakTime(){
        setTimer(breakSeconds)
        stop()
    }

    private fun stop(){
        if (timerStatus==TimerStatus.STARTED){
            secondsRemaining=(originalMinutes * 60 * 1000).toLong()
            updateCountdownUI()
            timerStatus=TimerStatus.STOPPED
            imageViewPause.visibility=View.INVISIBLE
            imageViewStartStop.setImageResource(R.drawable.icon_start)
            countDownTimer!!.cancel()
        }
    }
}
