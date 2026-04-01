package com.betoniarze.predihome.presentation.ui.lists.history

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.outlined.SubtitlesOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.betoniarze.predihome.R
import com.betoniarze.predihome.presentation.theme.Theme
import com.betoniarze.predihome.presentation.viewmodel.HistoryViewModel
import com.betoniarze.predihome.utilities.Units
import com.betoniarze.predihome.utilities.bounceClick
import com.betoniarze.predihome.utilities.dpToSp
import com.betoniarze.predihome.utilities.formatTimestamp
import com.betoniarze.predihome.utilities.scrollbar
import kotlinx.coroutines.launch

private const val startEndPercentagePadding = 0.05f

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HistoryView() {
    val viewModel = HistoryViewModel()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    scope.launch {
        viewModel.fetchUserHistory()
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        /* CONFIGURATION */
        ShowHistoryDetailsDialog(viewModel = viewModel)

        val startGuideline = createGuidelineFromStart(startEndPercentagePadding)
        val endGuideline = createGuidelineFromEnd(startEndPercentagePadding)

        /* HEADER IMAGE */

        val (headerImageRef) = createRefs()
        val headerImageTopGuideline = createGuidelineFromTop(0.0f)
        val headerImageBottomGuideline = createGuidelineFromTop(0.4f)

        HeaderImage(modifier = Modifier.constrainAs(headerImageRef) {
            top.linkTo(headerImageTopGuideline)
            bottom.linkTo(headerImageBottomGuideline)
            start.linkTo(startGuideline)
            end.linkTo(endGuideline)
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        })

        /* HISTORY LIST */

        val (historyListRef) = createRefs()
        val historyListBottomGuideline = createGuidelineFromBottom(0.05f)

        HistoryList(
            context = context,
            viewModel = viewModel,
            modifier = Modifier
                .constrainAs(historyListRef) {
                    top.linkTo(headerImageBottomGuideline)
                    bottom.linkTo(historyListBottomGuideline)
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                },
        )
    }
}

@Composable
private fun HeaderImage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(45.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.house_with_people),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxHeight().aspectRatio(1.2f).align(Alignment.Center)
        )
    }
}


@Composable
private fun HistoryList(modifier: Modifier = Modifier, viewModel: HistoryViewModel, context: Context) {
    val history = viewModel.history
    val scrollState = rememberLazyListState()
    val isAtBottom = !scrollState.canScrollForward

    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp)
            .scrollbar(
                state = scrollState,
                horizontal = false
            )
    ) {

        NoHistoryMessage(viewModel = viewModel)

        LazyColumn(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            state = scrollState
        ) {
            items(history) { history ->
                HistoryItem(historyRecord = history, viewModel = viewModel)
            }

            item {
                if (viewModel.historyLoading)
                    ProgressIndicator()
            }
        }

        LaunchedEffect(isAtBottom) {
            if (isAtBottom && viewModel.historyLoaded)
                viewModel.loadMoreHistory(context = context)
        }
    }
}


@Composable
private fun HistoryItem(historyRecord: HistoryRecord, viewModel: HistoryViewModel) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick()
            .clickable(
                enabled = true
            ) {
                viewModel.clickedHistoryDetails = historyRecord
                viewModel.toggleShowHistoryDetails()
            }
    ) {

        /* DATE */

        Text(
            text = formatTimestamp(historyRecord.predictionDate),
            fontSize = dpToSp(R.dimen.history_font_size),
            modifier = Modifier
                .clip(shape = RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.primary)
                .padding(4.dp)
                .weight(1f),
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )

        /* CITY */

        Text(
            text = historyRecord.formattedCity,
            fontSize = dpToSp(R.dimen.history_font_size),
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1.5f),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        /* PRICE */

        Text(
            text = "${historyRecord.price} ${Units.PLN}",
            fontSize = dpToSp(R.dimen.history_font_size),
            modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        /* ICON */

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowRight,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .size(dimensionResource(R.dimen.history_icon_size))
                .weight(0.5f)
        )
    }
}


@Composable
private fun ProgressIndicator() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(dimensionResource(R.dimen.history_progress_indicator_size))
        )
    }
}


@Composable
private fun ShowHistoryDetailsDialog(viewModel: HistoryViewModel) {
    if (viewModel.showHistoryDetails) {
        HistoryDetailsDialog(
            historyRecord = viewModel.clickedHistoryDetails,
            onDismiss = { viewModel.toggleShowHistoryDetails() }
        )
    }
}


@Composable
private fun NoHistoryMessage(modifier: Modifier = Modifier, viewModel: HistoryViewModel) {
    if (viewModel.historyEmpty && viewModel.historyLoaded) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.SubtitlesOff,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.history_empty_list_icon_size))
            )

            Text(
                text = "No data found!",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = dpToSp(R.dimen.history_empty_list_font_size)
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HistoryPreview() {
    Theme.MainTheme {
        HistoryView()
    }
}

