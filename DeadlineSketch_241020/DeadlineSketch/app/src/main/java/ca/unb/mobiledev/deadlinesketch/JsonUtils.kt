package ca.unb.mobiledev.deadlinesketch

import android.content.Context
import org.json.JSONObject
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class JsonUtils(context: Context) {
    private lateinit var tasks: ArrayList<Task>

    private fun processJSON(context: Context) {
        tasks = ArrayList()
        try {
            val jsonObject = JSONObject(Objects.requireNonNull(loadJSONFromAssets(context)))

            val jsonArray = jsonObject.getJSONArray(KEY_TASKS)
            for (i in 0 until jsonArray.length()) {
                var alpha = jsonArray.getJSONObject(i)
                var newTask = Task(alpha.getString(KEY_DUE_DATE),alpha.getString(KEY_NAME),alpha.getString(
                    KEY_DESCRIPTION))
                tasks.add(newTask)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun loadJSONFromAssets(context: Context): String? {
        val assetInstance = context.assets
        val inputString = assetInstance.open(TASK_JSON_FILE).bufferedReader().use {
            it.readText ()
        }
        return inputString
    }

    fun getCourses(): ArrayList<Task> {
        return tasks
    }

    companion object {
        private const val TASK_JSON_FILE = "Tasks.json"
        private const val KEY_TASKS = "tasks"
        private const val KEY_DUE_DATE = "DueDate"
        private const val KEY_NAME = "name"
        private const val KEY_DESCRIPTION = "description"
    }

    init {
        processJSON(context)
    }
}