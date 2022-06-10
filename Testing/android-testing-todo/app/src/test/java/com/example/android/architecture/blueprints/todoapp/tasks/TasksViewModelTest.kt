package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {

    @Test
    fun addNewTask_setsNewTaskEvent() {
        // TasksViewModel needs an Application context as parameter
        // In local test, AndroidX provides a simulated Android environment
        val tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
        tasksViewModel.addNewTask()

        // TODO test LiveData
    }
}