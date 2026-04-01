package com.betoniarze.predihome.presentation.ui.prediction

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.betoniarze.predihome.R
import com.betoniarze.predihome.presentation.navigation.NavigationItem
import com.betoniarze.predihome.presentation.theme.Dimensions

@Composable
fun SelectView(navController: NavHostController) {
    var area by remember { mutableStateOf(95f) }
    var rooms by remember { mutableStateOf(3f) }
    var year by remember { mutableStateOf(2023f) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        BoxWithConstraints {
            val screenWidth = maxWidth
            val screenHeight = maxHeight

            Image(
                painter = painterResource(id = R.drawable.house_with_people),
                contentDescription = "Description of the image",
                modifier = Modifier
                    .size(
                        width = screenWidth * 0.7f,
                        height = screenHeight * 0.25f
                    )
                    .offset(
                        y = (-0.2f * screenHeight.value).dp
                    )
            )
        }

        // Slider component
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 350.dp)
                .background(Color(0xFFEFEFEF), shape = MaterialTheme.shapes.medium),
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Area Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Area",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6B6B6B) // Adjust for styling
                        )
                        Slider(
                            value = area,
                            onValueChange = { area = it },
                            valueRange = 0f..200f,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFFA0522D), // Custom color
                                activeTrackColor = Color(0xFFA0522D),
                                inactiveTrackColor = Color(0xFFD3C4B7)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFE0E0E0), shape = MaterialTheme.shapes.small)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .shadow(4.dp, shape = MaterialTheme.shapes.small)
                    ) {
                        Text(
                            text = "${area.toInt()} m²",
                            fontSize = 14.sp,
                            color = Color(0xFF6B6B6B)
                        )
                    }
                }

                // Rooms Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Rooms",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6B6B6B)
                        )
                        Slider(
                            value = rooms,
                            onValueChange = { rooms = it },
                            valueRange = 1f..10f,
                            steps = 9,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFFA0522D), // Custom color
                                activeTrackColor = Color(0xFFA0522D),
                                inactiveTrackColor = Color(0xFFD3C4B7)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFE0E0E0), shape = MaterialTheme.shapes.small)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .shadow(4.dp, shape = MaterialTheme.shapes.small)
                    ) {
                        Text(
                            text = "${rooms.toInt()}",
                            fontSize = 14.sp,
                            color = Color(0xFF6B6B6B)
                        )
                    }
                }

                // Year Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Year",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6B6B6B)
                        )
                        Slider(
                            value = year,
                            onValueChange = { year = it },
                            valueRange = 1900f..2023f,
                            steps = 123,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFFA0522D), // Custom color
                                activeTrackColor = Color(0xFFA0522D),
                                inactiveTrackColor = Color(0xFFD3C4B7)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFE0E0E0), shape = MaterialTheme.shapes.small)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .shadow(4.dp, shape = MaterialTheme.shapes.small)
                    ) {
                        Text(
                            text = "${year.toInt()}",
                            fontSize = 14.sp,
                            color = Color(0xFF6B6B6B)
                        )
                    }
                }
            }
        }

        // Button at the bottom
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                modifier = Modifier.width(350.dp),
                shape = MaterialTheme.shapes.medium,
                onClick = {
                    navController.navigate(NavigationItem.PredictionMap.route)
                }
            ) {
                Text(
                    text = " Next",
                    textAlign = TextAlign.Center,
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    modifier = Modifier
                        .align(alignment = Alignment.CenterVertically)
                        .fillMaxWidth()
                        .weight(1f)
                )
                Icon(
                    painter = painterResource(id = R.drawable.outline_keyboard_arrow_right_24),
                    contentDescription = "Arrow right",
                    modifier = Modifier
                        .align(alignment = Alignment.Bottom)
                        .requiredSize(Dimensions.HOME_ICON_SIZE)
                )
            }
        }
    }
}
@Composable
fun SliderRowWithLabel(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    unit: String = "",
    isWholeNumber: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = valueRange,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFA0522D),
                    activeTrackColor = Color(0xFFA0522D),
                    inactiveTrackColor = Color.LightGray
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Surface(
            modifier = Modifier
                .background(Color(0xFFD1C4B2), shape = MaterialTheme.shapes.small)
                .padding(horizontal = 8.dp, vertical = 4.dp),
            tonalElevation = 4.dp
        ) {
            Text(
                text = if (isWholeNumber) "${value.toInt()} $unit" else "${String.format("%.1f", value)} $unit",
                fontSize = 16.sp,
                color = Color(0xFFA0522D),
                textAlign = TextAlign.Center
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewSelectView() {
    val navController = rememberNavController()
    SelectView(navController)
}
