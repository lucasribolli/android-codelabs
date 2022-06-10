package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.nullValue
import org.hamcrest.core.IsNot.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [30])
class TasksViewModelTest {
    // When you write tests that include testing LiveData, use this rule!
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun addNewTask_setsNewTaskEvent() {
        // TasksViewModel needs an Application context as parameter
        // In local test, AndroidX provides a simulated Android environment
        val tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())

        /*
            // Version without getOrAwait extension function (a lot of boilerplate)
            // Create observer - no need for it to do anything!
            val observer = Observer<Event<Unit>> {}

            try {
                tasksViewModel.newTaskEvent.observeForever(observer)
                tasksViewModel.addNewTask()

                val value = tasksViewModel.newTaskEvent.value
                assertThat(value?.getContentIfNotHandled(), (not(nullValue())))
            } finally {
                // Whatever happens, don't forget to remove the observer!
                tasksViewModel.newTaskEvent.removeObserver(observer)
            }
         */

        // When adding a new task
        tasksViewModel.addNewTask()

        // Then the new task event is triggered
        val value = tasksViewModel.newTaskEvent.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled(), not(nullValue()))
    }
}