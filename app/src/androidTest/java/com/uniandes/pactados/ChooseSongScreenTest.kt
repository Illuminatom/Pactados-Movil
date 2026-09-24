package com.uniandes.pactados

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.uniandes.pactados.screens.ChooseSongScreen
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class ChooseSongScreenTest {
    @get:Rule val compose = createComposeRule()

    @Test fun searchesByArtistAndOnlyConfirmsOnUse() {
        var result: String? = null
        compose.setContent { ChooseSongScreen(null, {}, { result = it }) }
        compose.onNodeWithText("Usar canción").assertIsNotEnabled()
        compose.onNodeWithText("Buscar canción o artista").performTextInput("taylor")
        compose.onNodeWithText("Shake it Off").performClick()
        compose.runOnIdle { assertNull(result) }
        compose.onNodeWithText("Usar canción").performClick()
        compose.runOnIdle { assertEquals("Shake it Off", result) }
    }

    @Test fun emptySearchCanBeClearedAndBackDiscardsDraftSelection() {
        var result: String? = null
        var returned = false
        compose.setContent { ChooseSongScreen("Despacito", { returned = true }, { result = it }) }
        compose.onNodeWithText("Despacito").assertIsSelected()
        compose.onNodeWithText("Buscar canción o artista").performTextInput("zzzzz")
        compose.onNodeWithText("No encontramos canciones.\nPrueba con otro nombre o artista.").assertIsDisplayed()
        compose.onNodeWithContentDescription("Limpiar búsqueda").performClick()
        compose.onNodeWithText("Shake it Off").performClick()
        compose.onNodeWithContentDescription("Volver a la alarma").performClick()
        compose.runOnIdle {
            assertEquals(true, returned)
            assertNull(result)
        }
    }
}
