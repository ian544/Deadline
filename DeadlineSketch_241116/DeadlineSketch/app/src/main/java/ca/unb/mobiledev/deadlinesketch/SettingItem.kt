package ca.unb.mobiledev.deadlinesketch

class SettingItem(
    val title: String,
    val children: List<String> = emptyList(),
    var isExpanded: Boolean = false
)