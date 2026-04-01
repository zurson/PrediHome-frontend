package com.betoniarze.predihome.presentation.ui.prediction

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.betoniarze.predihome.R
import com.betoniarze.predihome.presentation.navigation.NavigationItem
import com.betoniarze.predihome.presentation.theme.Theme
import com.betoniarze.predihome.presentation.ui.prediction.map.City
import com.betoniarze.predihome.presentation.viewmodel.PredictionViewModel
import com.betoniarze.predihome.utilities.CustomSlider
import com.betoniarze.predihome.utilities.Units
import com.betoniarze.predihome.utilities.dpToSp
import com.betoniarze.predihome.utilities.scrollbar
import com.google.android.gms.maps.model.LatLng

private const val startEndPercentagePadding = 0.05f


@Composable
fun PredictionParametersView(
    navController: NavHostController,
    predictionViewModel: PredictionViewModel
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        /* CONFIGURATION */
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

        /* SLIDERS */
        val (slidersListRef) = createRefs()
        val slidersListBottomGuideline = createGuidelineFromTop(0.8f)

        SlidersList(
            viewModel = predictionViewModel,
            modifier = Modifier
                .constrainAs(slidersListRef) {
                    top.linkTo(headerImageBottomGuideline)
                    bottom.linkTo(slidersListBottomGuideline)
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        )

        /* ACCEPT BUTTON */
        val (buttonRef) = createRefs()
        val buttonBottomGuideline = createGuidelineFromBottom(0.05f)

        PredictButton(
            viewModel = predictionViewModel,
            navController = navController,
            modifier = Modifier
                .constrainAs(buttonRef) {
                    top.linkTo(slidersListBottomGuideline)
                    bottom.linkTo(buttonBottomGuideline)
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
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
private fun SlidersList(modifier: Modifier = Modifier, viewModel: PredictionViewModel) {
    val scrollState = rememberLazyListState()

    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp)
            .scrollbar(
                state = scrollState,
                horizontal = false,
            )
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = scrollState,
        ) {
            val sliderList = viewModel.sliderDataList

            items(sliderList) { data ->
                SingleSlider(
                    viewModel = viewModel,
                    label = data.label,
                    value = data.value.value,
                    unit = data.unit,
                    onValueChange = data.onValueChange,
                    valueRange = data.valueRange,
                    steps = data.steps(),
                    nonDecimal = data.nonDecimal
                )
            }
        }
    }

}


@Composable
private fun SingleSlider(
    viewModel: PredictionViewModel,
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    unit: Units,
    onValueChange: (Float) -> Unit,
    steps: Int,
    nonDecimal: Boolean
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        /* LABEL */
        Text(
            text = label,
            fontSize = dpToSp(R.dimen.prediction_parameters_label_text_size),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        /* SLIDER + VALUE */
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CustomSlider(
                value = value,
                valueRange = valueRange,
                onValueChange = onValueChange,
                steps = steps,
                valueTransformer = { viewModel.fixValue(it, nonDecimal) },
                modifier = Modifier
                    .weight(3f)
                    .padding(8.dp)
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .clip(shape = RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .weight(1f)
                    .fillMaxSize()
            ) {

                Text(
                    text = viewModel.getResultValueWithUnit(value, unit),
                    fontSize = dpToSp(R.dimen.prediction_parameters_slider_value_text_size),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(4.dp)
                )
            }
        }
    }
}


@Composable
private fun PredictButton(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: PredictionViewModel
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(
            shape = MaterialTheme.shapes.medium,
            onClick = {
                viewModel.navigateTo(
                    navController = navController,
                    navigationItem = NavigationItem.PredictionResult,
                    doPredictionBefore = true
                )
            }
        ) {
            Text(
                text = stringResource(R.string.prediction_button_name),
                textAlign = TextAlign.Center,
                fontSize = dpToSp(R.dimen.prediction_parameters_predict_button_text_size),
                modifier = Modifier
                    .fillMaxWidth()
            )
            Icon(
                painter = painterResource(id = R.drawable.outline_keyboard_arrow_right_24),
                contentDescription = null,
                modifier = Modifier
                    .requiredSize(dimensionResource(R.dimen.prediction_parameters_predict_button_icon_size))
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun PreviewPredictionView() {
    val navController = rememberNavController()
    val mockPredictionViewModel = PredictionViewModel()

    mockPredictionViewModel.updateMapsData(
        City(
            name = "Mock City",
            latlng = LatLng(51.0, 19.0),
            _centreDistance = 10.0
        )
    )

    Theme.MainTheme {
        PredictionParametersView(navController, mockPredictionViewModel)
    }

}
