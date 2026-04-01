package com.betoniarze.predihome.utilities

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.betoniarze.predihome.R
import com.betoniarze.predihome.presentation.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSlider(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
    steps: Int,
    modifier: Modifier = Modifier,
    valueTransformer: (Float) -> Float = { it }
) {
    Slider(
        value = value,
        steps = steps,
        onValueChange = { rawValue ->
            onValueChange(valueTransformer(rawValue))
        },
        valueRange = valueRange,
        modifier = modifier
            .fillMaxWidth(),

        thumb = {
            Box(
                modifier = Modifier
                    .size(dimensionResource(R.dimen.prediction_parameters_slider_thumb_size))
                    .clip(CircleShape)
                    .background(color = MaterialTheme.colorScheme.primary)
            )
        },

        track = { sliderPositions ->
            val leftFraction = (sliderPositions.value - sliderPositions.valueRange.start) /
                    (sliderPositions.valueRange.endInclusive - sliderPositions.valueRange.start)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.prediction_parameters_slider_track_height))
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(leftFraction)
                        .height(dimensionResource(R.dimen.prediction_parameters_slider_track_height))
                        .background(MaterialTheme.colorScheme.primary)
                )
            }
        }
    )
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun Preview(modifier: Modifier = Modifier) {
    Theme.MainTheme {
        CustomSlider(
            value = 1f,
            onValueChange = { },
            valueRange = 1f..50f,
            steps = 50
        )
    }
}

