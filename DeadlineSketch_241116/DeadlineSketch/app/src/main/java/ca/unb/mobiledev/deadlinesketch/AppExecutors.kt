package ca.unb.mobiledev.deadlinesketch

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object AppExecutors {
    val databaseExecutor: ExecutorService = Executors.newFixedThreadPool(4)
    val networkExecutor: ExecutorService = Executors.newCachedThreadPool()
    val mainThreadExecutor: Executor = MainThreadExecutor()

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}
