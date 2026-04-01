package com.betoniarze.predihome.presentation.ui.lists.history

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.BedroomBaby
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.BorderOuter
import androidx.compose.material.icons.outlined.Elevator
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.LocalPharmacy
import androidx.compose.material.icons.outlined.LocalPostOffice
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MonetizationOn
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.SensorDoor
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.betoniarze.predihome.R
import com.betoniarze.predihome.presentation.theme.Theme
import com.betoniarze.predihome.utilities.Units
import com.betoniarze.predihome.utilities.bounceClick
import com.betoniarze.predihome.utilities.dpToSp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetailsDialog(
    modifier: Modifier = Modifier,
    historyRecord: HistoryRecord,
    onDismiss: () -> Unit
) {

    BasicAlertDialog(
        onDismissRequest = { onDismiss() },
        modifier = modifier
    ) {

        Column(
            modifier = Modifier
                .width(dimensionResource(R.dimen.history_details_window_width))
                .background(
                    MaterialTheme.colorScheme.background,
                    RoundedCornerShape(24.dp)
                )
                .border(
                    BorderStroke(4.dp, MaterialTheme.colorScheme.primary),
                    RoundedCornerShape(24.dp)
                )
                .padding(8.dp, bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .bounceClick()
                        .padding(end = 10.dp, top = 10.dp)
                        .clickable { onDismiss() }
                        .size(dimensionResource(R.dimen.history_details_close_icon_size))
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.history_details_title),
                    fontWeight = FontWeight.Bold,
                    fontSize = dpToSp(R.dimen.history_details_title_font_size),
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                )
            }

            HistoryDetail(
                icon = Icons.Outlined.LocationCity,
                text = "City",
                value = historyRecord.formattedCity,
                unit = Units.NONE
            )

            HistoryDetail(
                icon = Icons.Outlined.MonetizationOn,
                text = "Price",
                value = historyRecord.price,
                unit = Units.PLN
            )
            HistoryDetail(
                icon = Icons.Outlined.SensorDoor,
                text = "Rooms",
                value = historyRecord.rooms,
                unit = Units.NONE
            )
            HistoryDetail(
                icon = Icons.Outlined.BorderOuter,
                text = "Square meters",
                value = historyRecord.squareMeters,
                unit = Units.SQUARE_METERS
            )
            HistoryDetail(
                icon = Icons.Outlined.Elevator,
                text = "Floors",
                value = historyRecord.floorCount,
                unit = Units.NONE
            )

            HistoryDetail(
                icon = Icons.Outlined.LocationOn,
                text = "City centre",
                value = historyRecord.centreDistance,
                unit = Units.KILOMETERS
            )
            HistoryDetail(
                icon = Icons.Outlined.BedroomBaby,
                text = "Kindergarten",
                value = historyRecord.kindergartenDistance,
                unit = Units.KILOMETERS
            )
            HistoryDetail(
                icon = Icons.Outlined.Restaurant,
                text = "Restaurant",
                value = historyRecord.restaurantDistance,
                unit = Units.KILOMETERS
            )
            HistoryDetail(
                icon = Icons.Outlined.School,
                text = "College",
                value = historyRecord.collegeDistance,
                unit = Units.KILOMETERS
            )
            HistoryDetail(
                icon = Icons.Outlined.LocalPostOffice,
                text = "Post office",
                value = historyRecord.postOfficeDistance,
                unit = Units.KILOMETERS
            )
            HistoryDetail(
                icon = Icons.Outlined.LocalHospital,
                text = "Clinic",
                value = historyRecord.clinicDistance,
                unit = Units.KILOMETERS
            )
            HistoryDetail(
                icon = Icons.Outlined.Book,
                text = "School",
                value = historyRecord.schoolDistance,
                unit = Units.KILOMETERS
            )
            HistoryDetail(
                icon = Icons.Outlined.LocalPharmacy,
                text = "Pharmacy",
                value = historyRecord.pharmacyDistance,
                unit = Units.KILOMETERS
            )
        }


    }

}


@Composable
fun HistoryDetail(
    icon: ImageVector?,
    text: String,
    value: Any,
    unit: Units
) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {

        icon?.let {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(dimensionResource(R.dimen.history_details_icon_size))
            )
        }

        Text(
            text = "$text:",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = dpToSp(R.dimen.history_details_font_size),
            modifier = Modifier
                .padding(end = 12.dp)
        )

        Text(
            text = "$value $unit",
            color = MaterialTheme.colorScheme.primary,
            fontSize = dpToSp(R.dimen.history_details_font_size)
        )

    }
}


@Preview
@Composable
fun ShowPreviewHistoryDetailsDialog() {
    Theme.MainTheme {
        HistoryDetailsDialog(
            historyRecord = HistoryRecord(),
            onDismiss = {}
        )
    }
}