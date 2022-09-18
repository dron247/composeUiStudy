package com.example.testcomposaapplication

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.testcomposaapplication.ui.theme.DesignSystemTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DetailsScreen(
    state: State<CalendarState>,
    modifier: Modifier
) {
    val animState = remember {
        MutableTransitionState(false).apply {
            // Start the animation immediately.
            targetState = true
        }
    }
    Column(modifier = modifier) {
        DetailsHeader(state)
        AnimatedVisibility(visibleState = animState) {
            TodoList()
        }
    }
}

@Composable
private fun DetailsHeader(state: State<CalendarState>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(
            text = stringResource(
                id = R.string.format_day_plans_header,
                state.value.selectedDay ?: 0
            ),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart)
                .padding(start = 20.dp, end = 20.dp),
            style = DesignSystemTheme.typography.h3.medium
        )
    }
}

@Composable
private fun TodoList() {
    val listState = rememberLazyListState()
    val todoList = generateContent()
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        items(items = todoList, itemContent = { item ->
            when (item) {
                is TodoItem.Header -> ListHeader(text = item.title)
                is TodoItem.Item -> ListItem(time = item.time, text = item.content)
            }
        })
    }
}

@Composable
private fun ListHeader(text: String) {
    Text(
        text = text,
        style = DesignSystemTheme.typography.h3.medium,
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
    )
}

@Composable
private fun ListItem(time: String, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .padding(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 8.dp),
    ) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(DesignSystemTheme.colors.accentNegative)
        )
        Text(
            text = time,
            style = DesignSystemTheme.typography.h3.regular,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp)
        )
        Text(
            text = text,
            style = DesignSystemTheme.typography.h3.regular
        )
    }
}

@Preview
@Composable
private fun preview() {
    DesignSystemTheme {
        Surface(color = DesignSystemTheme.colors.backgroundPrimary) {
            DetailsScreen(
                mutableStateOf(CalendarState.default.selectDay(day = 12)),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private fun generateContent(): List<TodoItem> = listOf(
    TodoItem.Header("Ночь"),
    TodoItem.Item("00:00", "Спать"),
    TodoItem.Item("01:00", "Спать"),
    TodoItem.Item("02:00", "Спать"),
    TodoItem.Item("03:00", "Спать"),
    TodoItem.Item("04:00", "Спать"),
    TodoItem.Item("05:00", "Спать"),
    TodoItem.Header("Утро"),
    TodoItem.Item("06:00", "Встать"),
    TodoItem.Item("06:30", "Покакать"),
    TodoItem.Item("07:00", "Почистить зубы"),
    TodoItem.Item("07:03", "Покушать"),
    TodoItem.Item("07:30", "Выйти"),
    TodoItem.Item(
        "08:00",
        "Для желающих усложнить можно попробовать сделать список с разными ViewType (заголовок “День” и список дел, заголовок “Утро” и список дел)"
    ),
    TodoItem.Item(
        "09:00",
        "Для желающих усложнить можно попробовать сделать список с разными ViewType (заголовок “День” и список дел, заголовок “Утро” и список дел)"
    ),
    TodoItem.Header("Вечер"),
    TodoItem.Item("22:00", "Спать"),
)

sealed class TodoItem {
    class Header(val title: String) : TodoItem()
    class Item(val time: String, val content: String) : TodoItem()
}
