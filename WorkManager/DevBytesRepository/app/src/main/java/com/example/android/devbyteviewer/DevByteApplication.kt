/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.devbyteviewer

import android.app.Application
import androidx.work.*
import com.example.android.devbyteviewer.work.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Override application to setup background work via WorkManager
 *
 * When the process for your application or package is created, the Application
 * class (or any subclass of Application) is instantiated before any other class.
 */
class DevByteApplication : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    /**
     * onCreate is called before the first screen is shown to the user.
     *
     * Use it to setup any background tasks, running expensive setup operations in a background
     * thread to avoid delaying app start.
     */
    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            Timber.plant(Timber.DebugTree())
            setupRecurringWork()
        }
    }

    /**
     * Setup WorkManager background job to fetch new network data daily.
     *
     * The `onCreate()` method runs in the main thread. Performing a long-running
     * operation in `onCreate()` might block the UI thread and cause a delay in
     * loading the app. To avoid this problem, run tasks such as initializing
     * Timber and scheduling WorkManager off the main thread, inside a coroutine.
     */
    private fun setupRecurringWork() {
        /*
        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(
            1, TimeUnit.DAYS).build()

        val oneOff = OneTimeWorkRequestBuilder<RefreshDataWorker>().apply {
            setInitialDelay(1, TimeUnit.MINUTES)
        }.build()
        WorkManager.getInstance().enqueueUniqueWork(
            RefreshDataWorker.WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            oneOff
        )
        */

//        Periodic work can't have an initial delay as one of its constraints.
        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(
            15, TimeUnit.MINUTES).build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            repeatingRequest
        )
    }
}
