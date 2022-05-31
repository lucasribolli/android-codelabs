package com.example.android.devbyteviewer.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.android.devbyteviewer.database.getDatabase
import com.example.android.devbyteviewer.repository.VideosRepository
import retrofit2.HttpException
import timber.log.Timber

class RefreshDataWorker(appContext: Context, params: WorkerParameters)
    : CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "com.example.android.devbyteviewer.work.RefreshDataWorker"
    }

    /**
     A suspending function is a function that can be paused and resumed later.
     A suspending function can execute a long running operation and wait for
     it to complete without blocking the main thread.

     doWork is called on a background thread

     3 types of result:
     Result.success() — work completed successfully.
     Result.failure() — work completed with a permanent failure.
     Result.retry() — work encountered a transient failure and should be retried.
     */
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = VideosRepository(database)

        try {
            repository.refreshVideos()
            Timber.d("Work request for sync is run")
        } catch (e: HttpException) {
            Timber.e("Work request for sync error: $e")
            return Result.retry()
        }

        DebugWorkerNotification(applicationContext).show()

        return Result.success()
    }
}