package com.betoniarze.predihome.presentation.ui.form

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.betoniarze.predihome.R
import com.betoniarze.predihome.presentation.theme.Dimensions
import com.betoniarze.predihome.presentation.theme.Theme
import com.betoniarze.predihome.utilities.FirebaseAuthManager


@Composable
fun ProfileView() {
    val context = LocalContext.current
    val authManager = FirebaseAuthManager()
    val userEmail = authManager.getEmail()?.substringBefore("@") ?: ""

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.home_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                RoundedCornerBox(
                    widthPercentage = 0.8f,
                    heightPercentage = 0.5f,
                    cornerRadius = 16.dp,
                    text = "Hello, $userEmail"
                )

                LogoutButton(context = context, authManager = authManager)
            }
        }
    }
}


@Composable
fun RoundedCornerBox(
    widthPercentage: Float,
    heightPercentage: Float,
    cornerRadius: Dp,
    text: String
) {

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth(fraction = widthPercentage)
            .height(screenHeight * heightPercentage)
            .clip(RoundedCornerShape(cornerRadius))
            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)),


        ) {

        val iconSize = maxHeight * 0.5f

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_account_circle_24),
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun LogoutButton(context: Context, authManager: FirebaseAuthManager) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            modifier = Modifier
                .width(screenWidth * 0.8f)
                .align(Alignment.CenterHorizontally),
            shape = MaterialTheme.shapes.large,
            onClick = {
                authManager.logoutUser(context)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_logout_24),
                contentDescription = "Logout",
                modifier = Modifier
                    .align(alignment = Alignment.Top)
                    .requiredSize(size = Dimensions.HOME_ICON_SIZE)
            )

            Text(
                text = stringResource(R.string.logout_button_text),
                textAlign = TextAlign.Center,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                modifier = Modifier
                    .wrapContentSize(align = Alignment.Center)
                    .weight(1f)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewProfileView() {
    Theme.MainTheme {
        ProfileView()
    }
}