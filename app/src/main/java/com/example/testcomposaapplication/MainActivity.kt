package com.example.testcomposaapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.testcomposaapplication.ui.theme.DesignSystemTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DesignSystemTheme {
                Surface(color = DesignSystemTheme.colors.backgroundPrimary) {
                    CalendarApplicationUi()
                }
            }
        }
    }

    @Composable
    fun CalendarApplicationUi() {
        val calendarState = remember { mutableStateOf(CalendarState.default) }
        val navigationState = remember { mutableStateOf(SelectedScreen.Calendar) }

        when (navigationState.value) {
            SelectedScreen.Calendar -> {
                CalendarScreen(
                    state = calendarState,
                    modifier = Modifier.fillMaxSize(),
                    onDaySelected = { dayNumber ->
                        calendarState.value = calendarState.value.selectDay(day = dayNumber)
                    },
                    onPreviousMonthClick = {
                        calendarState.value = calendarState.value.previousMonth()
                    },
                    onNextMonthClick = {
                        calendarState.value = calendarState.value.nextMonth()
                    },
                    onDetailsClicked = {
                        navigationState.value = SelectedScreen.Details
                    }
                )
            }
            SelectedScreen.Details -> {
                BackHandler(enabled = true) {
                    navigationState.value = SelectedScreen.Calendar
                }
                DetailsScreen(state = calendarState, modifier = Modifier.fillMaxSize())
            }
        }
    }
}

enum class SelectedScreen {
    Calendar,
    Details
}
