package ca.unb.mobiledev.deadlinesketch

data class Task(val dueDate: String?, val name: String?, val description: String? = null, val id: Int, val priority: String?) {
    constructor(dueDate: String?, name: String?, description: String? = null) : this(
        dueDate,
        name,
        description,
        0,
        null
    )
}


