package com.betoniarze.predihome.presentation.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.betoniarze.predihome.R
import com.betoniarze.predihome.presentation.theme.Theme
import com.betoniarze.predihome.utilities.dpToSp


private const val startEndPercentagePadding = 0.03f


@Composable
fun HomeView() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        /* CONFIGURATION */
        val startGuideline = createGuidelineFromStart(startEndPercentagePadding)
        val endGuideline = createGuidelineFromEnd(startEndPercentagePadding)
        val headerImageTopGuidelineFloat = 0.0f
        val headerImageBottomGuidelineFloat = 0.4f
        val logoAndGreetingTopGuidelineFloat = headerImageBottomGuidelineFloat - 0.11f
        val logoAndGreetingBottomGuidelineFloat = headerImageBottomGuidelineFloat + 0.1f

        /* HEADER IMAGE */
        val (headerImageRef) = createRefs()
        val headerImageTopGuideline = createGuidelineFromTop(headerImageTopGuidelineFloat)
        val headerImageBottomGuideline = createGuidelineFromTop(headerImageBottomGuidelineFloat)

        HeaderImage(modifier = Modifier.constrainAs(headerImageRef) {
            top.linkTo(headerImageTopGuideline)
            bottom.linkTo(headerImageBottomGuideline)
            start.linkTo(startGuideline)
            end.linkTo(endGuideline)
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        })

        /* LOGO WITH GREETING */
        val (logoAndGreetingRef) = createRefs()
        val logoAndGreetingTopGuideline = createGuidelineFromTop(logoAndGreetingTopGuidelineFloat)
        val logoAndGreetingBottomGuideline =
            createGuidelineFromTop(logoAndGreetingBottomGuidelineFloat)

        LogoAndGreeting(modifier = Modifier.constrainAs(logoAndGreetingRef) {
            top.linkTo(logoAndGreetingTopGuideline)
            bottom.linkTo(logoAndGreetingBottomGuideline)
            start.linkTo(startGuideline)
            end.linkTo(endGuideline)
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        })

        /* DESCRIPTION */
        val (descriptionRef) = createRefs()
        val descriptionTopGuideline = createGuidelineFromTop(0.5f)
        val descriptionBottomGuideline = createGuidelineFromBottom(0.05f)

        Description(modifier = Modifier.constrainAs(descriptionRef) {
            top.linkTo(descriptionTopGuideline)
            bottom.linkTo(descriptionBottomGuideline)
            start.linkTo(parent.start)
            end.linkTo(endGuideline)
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        })

    }
}


@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
private fun HeaderImage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 32.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.home_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(15.dp))
        )
    }
}


@Composable
fun LogoAndGreeting(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.home_hello),
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            fontSize = dpToSp(R.dimen.home_welcome_text_size),
            modifier = Modifier
                .padding(top = 8.dp)
        )

        Image(
            painter = painterResource(R.drawable.logogold),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp)
        )
    }
}


@Composable
fun Description(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(topEnd = 85.dp, bottomEnd = 1250.dp))
            .background(color = MaterialTheme.colorScheme.surface)
            .fillMaxSize()
    ) {
        DescriptionText()
        DescriptionMap()
    }
}


@Composable
fun DescriptionText() {
    Text(
        textAlign = TextAlign.Center,
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontSize = dpToSp(R.dimen.home_description_text_size),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            ) {
                appendLine(stringResource(R.string.home_description_1))
            }
            withStyle(
                style = SpanStyle(
                    fontSize = dpToSp(R.dimen.home_description_text_size),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(stringResource(R.string.home_description_2))
                append(" ")
            }
            withStyle(
                style = SpanStyle(
                    fontSize = dpToSp(R.dimen.home_description_text_size),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(stringResource(R.string.home_description_3))
                append("\n")
            }
            withStyle(
                style = SpanStyle(
                    fontSize = dpToSp(R.dimen.home_description_text_size),
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold,
                )
            ) {
                append("12 ")
            }
            withStyle(
                style = SpanStyle(
                    fontSize = dpToSp(R.dimen.home_description_text_size),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                )
            ) {
                append("march")
            }
            withStyle(
                style = SpanStyle(
                    fontSize = dpToSp(R.dimen.home_description_text_size),
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
            ) {
                append(" 2024")
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
            .padding(8.dp)
    )
}


@Composable
fun DescriptionMap() {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.home_poland),
                contentDescription = "Poland",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.home_map_image_size))
            )
            Icon(
                painter = painterResource(id = R.drawable.baseline_location_pin_24),
                contentDescription = "Map pin",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.home_pin_icon_size))
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    Theme.MainTheme {
        HomeView()
    }
}