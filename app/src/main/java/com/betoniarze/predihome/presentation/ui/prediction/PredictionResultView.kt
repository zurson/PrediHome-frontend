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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.betoniarze.predihome.R
import com.betoniarze.predihome.presentation.navigation.NavigationItem
import com.betoniarze.predihome.presentation.theme.Theme
import com.betoniarze.predihome.presentation.viewmodel.PredictionViewModel
import com.betoniarze.predihome.utilities.Units
import com.betoniarze.predihome.utilities.dpToSp
import com.betoniarze.predihome.utilities.scrollbar

private const val startEndPercentagePadding = 0.05f

@Composable
fun PredictionResultView(
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

        /* PREDICTION RESULT */
        val (resultBoxRef) = createRefs()
        val resultBoxStartGuideline = createGuidelineFromStart(0.3f)
        val resultBoxTopGuideline = createGuidelineFromTop(0.08f)
        val resultBoxBottomGuideline = createGuidelineFromTop(0.2f)

        PredictionResultBox(
            viewModel = predictionViewModel,
            modifier = Modifier.constrainAs(resultBoxRef) {
                start.linkTo(resultBoxStartGuideline)
                end.linkTo(parent.end)
                top.linkTo(resultBoxTopGuideline)
                bottom.linkTo(resultBoxBottomGuideline)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
        )

        /* BACKGROUND IMAGE */
        val (headerImageRef) = createRefs()
        val backgroundImageTopGuideline = createGuidelineFromTop(0.25f)
        val backgroundImageBottomGuideline = createGuidelineFromTop(0.55f)

        MainImage(modifier = Modifier.constrainAs(headerImageRef) {
            top.linkTo(backgroundImageTopGuideline)
            bottom.linkTo(backgroundImageBottomGuideline)
            start.linkTo(startGuideline)
            end.linkTo(endGuideline)
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        })

        /* SLIDERS */
        val (slidersListRef) = createRefs()
        val valuesListTopGuideline = createGuidelineFromTop(0.4f)
        val valuesListBottomGuideline = createGuidelineFromTop(0.8f)

        ParametersList(
            viewModel = predictionViewModel,
            modifier = Modifier
                .constrainAs(slidersListRef) {
                    top.linkTo(valuesListTopGuideline)
                    bottom.linkTo(valuesListBottomGuideline)
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
        )

        /* ACCEPT BUTTON */
        val (buttonRef) = createRefs()
        val buttonBottomGuideline = createGuidelineFromBottom(0.05f)

        BackButton(
            viewModel = predictionViewModel,
            navController = navController,
            modifier = Modifier.constrainAs(buttonRef) {
                top.linkTo(valuesListBottomGuideline)
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
private fun ParametersList(viewModel: PredictionViewModel, modifier: Modifier) {
    val scrollState = rememberLazyListState()
    val parametersList = viewModel.getParametersList(viewModel)

    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .scrollbar(
                state = scrollState,
                horizontal = false,
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            state = scrollState,
        ) {
            items(parametersList) { parameter ->
                PredictionParameter(
                    viewModel = viewModel,
                    label = parameter.name,
                    value = parameter.value,
                    unit = parameter.unit
                )
            }
        }
    }
}


@Composable
private fun PredictionParameter(
    viewModel: PredictionViewModel,
    label: String,
    unit: Units,
    value: Any,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = dpToSp(R.dimen.prediction_result_parameter_label_text_size),
                fontWeight = FontWeight.Bold
            )

            Text(
                text = viewModel.getResultValueWithUnit(value, unit),
                color = MaterialTheme.colorScheme.primary,
                fontSize = dpToSp(R.dimen.prediction_result_parameter_value_text_size),
                fontWeight = FontWeight.Bold
            )
        }

        Divider(
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}


@Composable
private fun MainImage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.person_on_balcony_image),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxHeight().aspectRatio(1.5f).align(Alignment.Center)
        )
    }
}


@Composable
private fun PredictionResultBox(modifier: Modifier = Modifier, viewModel: PredictionViewModel) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(32.dp, 0.dp, 0.dp, 64.dp))
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row(

        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(8.dp)
            ) {

                if (viewModel.predictionInProgress) {
                    ProgressIndicator()
                } else {
                    Text(
                        text = "${viewModel.predictionValue}",
                        fontWeight = FontWeight.Bold,
                        fontSize = dpToSp(R.dimen.prediction_result_predicted_value_text_size),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Text(
                    text = Units.PLN.toString(),
                    color = MaterialTheme.colorScheme.background,
                    fontSize = dpToSp(R.dimen.prediction_result_predicted_value_unit_text_size)
                )

                Divider(
                    color = MaterialTheme.colorScheme.background,
                    thickness = dimensionResource(R.dimen.prediction_result_predicted_unit_divider_thickness),
                    modifier = Modifier
                        .width(dimensionResource(R.dimen.prediction_result_predicted_unit_divider_width))
                )

                Text(
                    text = Units.MONTH.toString(),
                    color = MaterialTheme.colorScheme.background,
                    fontSize = dpToSp(R.dimen.prediction_result_predicted_value_unit_text_size)
                )
            }
        }
    }
}


@Composable
private fun ProgressIndicator() {
    Box(
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(dimensionResource(R.dimen.prediction_result_progress_indicator_size))
        )
    }
}


@Composable
private fun BackButton(
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
                    navigationItem = NavigationItem.Home,
                )
            }
        ) {
            Text(
                text = stringResource(R.string.prediction_back_button_name),
                textAlign = TextAlign.Center,
                fontSize = dpToSp(R.dimen.prediction_result_back_button_text_size),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewPredictionResultView() {
    Theme.MainTheme {
        PredictionResultView(rememberNavController(), PredictionViewModel())
    }
}
