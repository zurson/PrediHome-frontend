package com.betoniarze.predihome.presentation.ui.lists

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.betoniarze.predihome.R
import com.betoniarze.predihome.core.communication.CommunicationManager
import com.betoniarze.predihome.presentation.theme.Theme
import com.betoniarze.predihome.utilities.scrollbar
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Preview(showBackground = true)
@Composable
fun PreviewPopularRegionsView() {
    Theme.MainTheme {
        PopularRegionsView()
    }
}

@Composable
fun PopularRegionsView() {
    val scrollState = rememberLazyListState()
    val cityList = remember { mutableStateMapOf<String, Long>() }
    var time by remember { mutableLongStateOf(0L) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        CommunicationManager().fetchPopularRegions()?.let { result ->
            cityList.clear()
            cityList.putAll(result)
            time = cityList["Time"] ?: 0L
            cityList.remove("Time")
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.regions_poland),
                contentDescription = "Poland map",
                modifier = Modifier
                    .weight(0.4f),
                contentScale = ContentScale.FillHeight
            )
            Text(
                text = "Status: ${formatTime(time)}",
                fontSize = dimensionResource(R.dimen.pr_status_font_size).value.sp
            )
            Box(
                modifier = Modifier
                    .weight(0.6f)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(24.dp)
                    )
                    .padding(16.dp)
                    .scrollbar(
                        state = scrollState,
                        horizontal = false
                    )
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = scrollState
                ) {
                    items(cityList.entries.sortedByDescending { it.value }
                        .toList()) { (cityName, cityPopularity) ->
                        CityItem(number = cityPopularity, name = cityName)
                    }
                    if (isLoading) {
                        item {
                            ProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun ProgressIndicator() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(dimensionResource(R.dimen.pr_progress_indicator_size))
        )
    }
}


@Composable
fun CityItem(number: Long, name: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .height(dimensionResource(R.dimen.pr_city_item_height)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(0.35f)
                .fillMaxHeight()
                .background(
                    color = MaterialTheme.colorScheme.primary, RoundedCornerShape(
                        dimensionResource(R.dimen.pr_city_item_rounding)
                    )
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = number.toString(),
                fontSize = dimensionResource(R.dimen.pr_city_item_font_size).value.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Column(
            modifier = Modifier
                .weight(0.65f)
                .padding(start = dimensionResource(R.dimen.pr_city_item_start_padding))
        ) {
            Text(
                text = name,
                fontSize = dimensionResource(R.dimen.pr_city_item_font_size).value.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

fun formatTime(timeInMillis: Long): String {
    if (timeInMillis == 0L) return "No data"

    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeInMillis), ZoneId.systemDefault())

    return formatter.format(dateTime)
}
