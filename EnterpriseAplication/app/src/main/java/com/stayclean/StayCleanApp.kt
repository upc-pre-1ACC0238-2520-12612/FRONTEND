package com.stayclean

import android.app.Application
import android.util.Log

class StayCleanApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Manejador global para capturar excepciones no controladas y escribirlas en logcat
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("StayClean", "Uncaught exception on thread ${thread.name}", throwable)
            // Dejamos que el manejador por defecto continúe para no alterar el comportamiento
            // val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
            // defaultHandler?.uncaughtException(thread, throwable)
        }
    }
}

