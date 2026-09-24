package com.uniandes.pactados.data

import androidx.compose.runtime.mutableStateListOf

data class Alarm(
    val id: Int,
    val hour: Int,      // 0 - 23
    val minute: Int,    // 0 - 59
    val name: String,
    val description: String = "",
    val song: String? = null,
    val enabled: Boolean = true
)

// Formato del prototipo: "03:00 p. m."
fun Alarm.formattedTime(): String {
    val hour12 = if (hour % 12 == 0) 12 else hour % 12
    val period = if (hour < 12) "a. m." else "p. m."
    return "%02d:%02d %s".format(hour12, minute, period)
}

// Almacen en memoria compartido por las pantallas de alarmas.
// Al ser un mutableStateListOf, las pantallas se recomponen solas cuando cambia.
object AlarmRepository {
    private var nextId = 1
    val alarms = mutableStateListOf<Alarm>()

    init {
        add(0, 0, "Tomar Agua", "Mantenerme hidratado", "Despacito")
        add(15, 0, "Leer La Odisea")
        add(16, 0, "Estirar")
        add(20, 0, "Sacar al perro")
    }

    fun get(id: Int): Alarm? = alarms.find { it.id == id }

    fun add(hour: Int, minute: Int, name: String, description: String = "", song: String? = null) {
        alarms.add(Alarm(nextId++, hour, minute, name, description, song))
    }

    fun update(alarm: Alarm) {
        val index = alarms.indexOfFirst { it.id == alarm.id }
        if (index >= 0) alarms[index] = alarm
    }

    fun delete(id: Int) {
        alarms.removeAll { it.id == id }
    }

    fun setEnabled(id: Int, enabled: Boolean) {
        get(id)?.let { update(it.copy(enabled = enabled)) }
    }
}
