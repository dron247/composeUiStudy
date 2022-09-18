package com.example.testcomposaapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testcomposaapplication.ui.theme.DesignSystemTheme
import java.util.*

@Composable
fun CalendarScreen(
    state: State<CalendarState>,
    modifier: Modifier,
    onDaySelected: (Int) -> Unit = {},
    onDetailsClicked: (Date) -> Unit = {},
    onPreviousMonthClick: () -> Unit = {},
    onNextMonthClick: () -> Unit = {}
) {
    val monthsArray = stringArrayResource(id = R.array.months)
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp)
                .height(56.dp)
        ) {
            Text(
                text = "${monthsArray[state.value.month]} ${state.value.year}",
                modifier = Modifier.align(Alignment.CenterStart),
                fontSize = 20.sp,
                style = DesignSystemTheme.typography.h3.medium
            )
            Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                Image(
                    painter = painterResource(id = R.drawable.ic_icon_month_back),
                    contentDescription = stringResource(id = R.string.hint_back),
                    modifier = Modifier.clickable {
                        onPreviousMonthClick()
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_icon_month_forward),
                    contentDescription = stringResource(id = R.string.hint_forward),
                    modifier = Modifier.clickable {
                        onNextMonthClick()
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        CalendarControl(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            onDaySelected = onDaySelected
        )
        Spacer(modifier = Modifier.height(32.dp))
        if (state.value.selectedDay != null) {
            Button(
                onClick = { state.value.selectedDate?.let { onDetailsClicked(it) } },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(48.dp)
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(DesignSystemTheme.colors.accentNegative)
            ) {
                Text(
                    text = stringResource(id = R.string.open_details),
                    style = DesignSystemTheme.typography.p1.medium,
                    color = DesignSystemTheme.colors.textInverted
                )
            }
        } else {
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun CalendarControl(
    state: State<CalendarState>,
    modifier: Modifier,
    onDaySelected: (Int) -> Unit
) {
    val dayArray = stringArrayResource(id = R.array.days)
    val days = mapOf(
        Calendar.MONDAY to dayArray[0],
        Calendar.TUESDAY to dayArray[1],
        Calendar.WEDNESDAY to dayArray[2],
        Calendar.THURSDAY to dayArray[3],
        Calendar.FRIDAY to dayArray[4],
        Calendar.SATURDAY to dayArray[5],
        Calendar.SUNDAY to dayArray[6],
    )
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (dayOfWeek in days) {
            Column(
                modifier = Modifier.width(40.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = dayOfWeek.value.uppercase(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp),
                    textAlign = TextAlign.Center,
                    style = DesignSystemTheme.typography.p2.regular,
                    fontSize = 12.sp,
                    color = DesignSystemTheme.colors.textSecondary
                )
                for (weekOfMonth in 1..state.value.weeksCount) {
                    val day = state.value.getDayNumber(weekOfMonth, dayOfWeek.key)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .clickable { onDaySelected(day) }
                    ) {
                        if (state.value.isInMonth(weekOfMonth, dayOfWeek.key)) {
                            Text(
                                text = "$day",
                                modifier = Modifier.align(Alignment.Center),
                                style = DesignSystemTheme.typography.p1.regular,
                                fontSize = 20.sp,
                                color = when {
                                    day == state.value.selectedDay -> DesignSystemTheme.colors.accentNegative
                                    dayOfWeek.key.isWorkingDay -> DesignSystemTheme.colors.textPrimary
                                    else -> DesignSystemTheme.colors.textTertiary
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    DesignSystemTheme {
        Surface(color = DesignSystemTheme.colors.backgroundPrimary) {
            CalendarScreen(
                state = mutableStateOf(CalendarState.default.selectDay(day = 12)),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private val Int.isWorkingDay: Boolean
    get() = this != Calendar.SATURDAY && this != Calendar.SUNDAY
