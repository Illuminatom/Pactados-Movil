package com.uniandes.pactados.data

/** Local August examples, independent of alarm creation and the other screens. */
data class CalendarHabit(
    val id: String,
    val title: String,
    val filterLabel: String,
    val postponedDays: Set<Int>
) {
    val postponedPercentage: Int get() = (postponedDays.size * 100f / 31).toInt()
}

val calendarHabitExamples = listOf(
    CalendarHabit("water", "Tomar Agua", "Tomar agua", setOf(2, 5, 7, 8, 12, 13, 15, 16, 22, 29, 30)),
    CalendarHabit("reading", "Leer la odisea", "Leer la odisea", setOf(3, 6, 10, 15, 20, 24)),
    CalendarHabit("dog", "Sacar al perro", "Sacar al perro", setOf(2, 8, 14, 21))
)
