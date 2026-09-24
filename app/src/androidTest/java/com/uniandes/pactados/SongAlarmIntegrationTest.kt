package com.uniandes.pactados

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.uniandes.pactados.data.AlarmRepository
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SongAlarmIntegrationTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test fun choosingSongPreservesDraftAndSavesItWithNewAlarm() {
        compose.onAllNodes(hasSetTextAction())[0].performTextInput("3000000000")
        compose.onAllNodes(hasSetTextAction())[1].performTextInput("demo")
        compose.onNodeWithText("Iniciar Sesión").performScrollTo().performClick()
        compose.onNodeWithText("Crear Alarma").performClick()
        compose.onAllNodes(hasSetTextAction())[2].performScrollTo().performTextInput("Prueba canción")
        compose.onAllNodes(hasSetTextAction())[3].performScrollTo().performTextInput("Descripción conservada")
        compose.onNodeWithText("Elegir Cancion").performScrollTo().performClick()
        compose.onNodeWithText("Happy").performScrollTo().performClick()
        compose.onNodeWithText("Usar canción").performClick()
        compose.onNodeWithText("Prueba canción").assertExists()
        compose.onNodeWithText("Descripción conservada").assertExists()
        compose.onNodeWithText("Happy").performScrollTo().performClick()
        compose.onNodeWithText("Happy").assertIsSelected()
        compose.onNodeWithText("Shake it Off").performScrollTo().performClick()
        compose.onNodeWithContentDescription("Volver a la alarma").performClick()
        compose.onNodeWithText("Happy").assertExists()
        compose.onNodeWithText("Crear Alarma").performScrollTo().performClick()
        compose.runOnIdle {
            val alarm = AlarmRepository.alarms.last { it.name == "Prueba canción" }
            assertEquals("Happy", alarm.song)
            assertEquals("Descripción conservada", alarm.description)
            AlarmRepository.delete(alarm.id)
        }
    }
}
