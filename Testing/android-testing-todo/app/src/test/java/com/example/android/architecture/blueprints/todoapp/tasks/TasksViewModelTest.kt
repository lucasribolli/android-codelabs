package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTaskRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.nullValue
import org.hamcrest.core.IsNot.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TasksViewModelTest {
    private lateinit var tasksViewModel: TasksViewModel

    private lateinit var tasksRepository: FakeTaskRepository

    @Before
    fun setupViewModel() {
        tasksRepository = FakeTaskRepository()
        tasksRepository.addTasks(
            Task("Title1", "Description1"),
            Task("Title2", "Description2", true),
            Task("Title3", "Description3", true)
        )
        tasksViewModel = TasksViewModel(tasksRepository)
    }

    // When you write tests that include testing LiveData, use this rule!
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun addNewTask_setsNewTaskEvent() {
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

    @Test
    fun setFilterAllTasks_tasksAddViewVisible() {
        // Given a fresh ViewModel
        // When the filter type is ALL_TASKS
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)
        // Then the "Add task" action is visible
        assertThat(tasksViewModel.tasksAddViewVisible.getOrAwaitValue(), `is`(true))
    }
}