package com.uniandes.pactados

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.junit4.StateRestorationTester
import com.uniandes.pactados.screens.CalendarScreen
import org.junit.Rule
import org.junit.Test

class CalendarScreenTest {
    @get:Rule val compose = createComposeRule()

    @Test fun eachDayOpensItsOwnDateAndReturnsToSelectedHabit() {
        compose.setContent { CalendarScreen({}, {}, {}, {}) }
        compose.onNodeWithContentDescription("Filtrar por hábito").performClick()
        compose.onNodeWithText("Leer la odisea").performClick()
        for (day in 1..31) {
            compose.onNodeWithText(day.toString()).performClick()
            compose.onNodeWithText("El $day de Agosto completaste al 100% una tarea ¡Vamos por las demas!").assertExists()
            if (day == 3) compose.onNodeWithText("Martes\n3\nde Agosto").assertIsDisplayed()
            compose.onNodeWithContentDescription("Volver al calendario").performClick()
            compose.onNodeWithText("- Leer la odisea").assertIsDisplayed()
        }
    }

    @Test fun selectedDateAndHabitSurviveStateRestoration() {
        val restoration = StateRestorationTester(compose)
        restoration.setContent { CalendarScreen({}, {}, {}, {}) }
        compose.onNodeWithContentDescription("Filtrar por hábito").performClick()
        compose.onNodeWithText("Sacar al perro").performClick()
        compose.onNodeWithText("3").performClick()
        restoration.emulateSavedInstanceStateRestore()
        compose.onNodeWithText("Martes\n3\nde Agosto").assertIsDisplayed()
        compose.onNodeWithText("Calendario").performClick()
        compose.onNodeWithText("- Sacar al perro").assertIsDisplayed()
    }

    @Test fun selectingHabitUpdatesCalendarAndKeepsSelectionWhenReopening() {
        compose.setContent { CalendarScreen({}, {}, {}, {}) }
        compose.onNodeWithText("- Tomar Agua").assertIsDisplayed()
        compose.onNodeWithContentDescription("3 de agosto: Cumplida").assertExists()
        compose.onNodeWithContentDescription("Filtrar por hábito").performClick()
        compose.onNodeWithText("Tomar agua").assertIsSelected()
        compose.onNodeWithText("Leer la odisea").performClick()
        compose.onNodeWithText("- Leer la odisea").assertIsDisplayed()
        compose.onNodeWithContentDescription("3 de agosto: Pospuesta").assertExists()
        compose.onNodeWithText("El 19% de las alarmas fueron\npospuestas ¡Esfuerzate mas!").assertExists()
        compose.onNodeWithContentDescription("Filtrar por hábito").performClick()
        compose.onNodeWithText("Leer la odisea").assertIsSelected()
        compose.onNodeWithText("Sacar al perro").performClick()
        compose.onNodeWithText("- Sacar al perro").assertIsDisplayed()
        compose.onNodeWithText("El 12% de las alarmas fueron\npospuestas ¡Esfuerzate mas!").assertExists()
    }
}
