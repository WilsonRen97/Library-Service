package com.wilsontryingapp2023.libraryservice.thread


import android.os.Handler
import android.os.Looper
import java.util.concurrent.CountDownLatch

class LooperThread(var countdown: CountDownLatch) : Thread() {
    var myHandler: MyThreadHandler? = null // 製作一個Handler物件
    inner class MyThreadHandler(looper : Looper) : Handler(looper)

    override fun run() {
        Looper.prepare()
        myHandler = MyThreadHandler(Looper.myLooper()!!)
        countdown.countDown() // 通知main thread我們完成製作myHandler了
        Looper.loop()
    }
}