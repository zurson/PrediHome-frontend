package com.betoniarze.predihome.presentation.ui.form

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.betoniarze.predihome.R
import com.betoniarze.predihome.core.host.LoginActivity
import com.betoniarze.predihome.presentation.theme.Strings
import com.betoniarze.predihome.presentation.theme.Theme
import com.betoniarze.predihome.presentation.viewmodel.AuthViewModel
import com.betoniarze.predihome.utilities.changeActivity
import com.betoniarze.predihome.utilities.dpToSp


@Composable
fun RegisterView() {
    val context = LocalContext.current
    val viewModel = AuthViewModel()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        /* INFO SECTION */
        val (infoAppName, infoTitleRef, infoDescRef, infoImageRef) = createRefs()

        val infoTopGuideline = createGuidelineFromTop(0.06f)
        val infoBottomGuideline = createGuidelineFromTop(0.21f)

        HeaderText(
            modifier = Modifier.constrainAs(infoAppName) {
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })

        LoginImage(
            modifier = Modifier.constrainAs(infoImageRef) {
                top.linkTo(infoTopGuideline)
                bottom.linkTo(infoBottomGuideline)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
        )

        LoginTitle(
            text = Strings.LOGIN_TITLE_REVERSED,
            modifier = Modifier.constrainAs(infoTitleRef) {
                top.linkTo(infoImageRef.top)
                bottom.linkTo(infoDescRef.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        LoginDescription(
            modifier = Modifier.constrainAs(infoDescRef) {
                top.linkTo(infoTitleRef.bottom)
                bottom.linkTo(infoBottomGuideline)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )


        /* FORMS SECTION */

        val (formsRef) = createRefs()
        val formsTopGuideLine = createGuidelineFromTop(0.25f)
        val formsBottomGuideLine = createGuidelineFromTop(0.6f)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(formsRef) {
                    top.linkTo(formsTopGuideLine)
                    bottom.linkTo(formsBottomGuideLine)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }

        ) {
            AuthTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.auth_form_padding)),
                keyboardType = KeyboardType.Email,
                leadingIcon = Icons.Outlined.Email,
                value = viewModel.email,
                label = Strings.LOGIN_FROM_EMAIL_LABEL,
                onValueChange = { viewModel.email = it },
                isError = viewModel.isErrorEmail,
                errorMessage = Strings.LOGIN_EMAIL_ERROR
            )

            AuthPasswordTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.auth_form_padding)),
                label = Strings.LOGIN_FROM_PASSWORD_LABEL,
                onValueChange = { viewModel.password = it },
                value = viewModel.password,
                viewModel = viewModel,
                showVisualityToggleIcon = true,
                isError = viewModel.isErrorPassword,
                errorMessage = Strings.LOGIN_PASSWORD_ERROR
            )

            AuthPasswordTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(R.dimen.auth_form_padding)),
                label = Strings.LOGIN_FORM_REPEAT_PASSWORD_LABEL,
                onValueChange = { viewModel.repeatedPassword = it },
                value = viewModel.repeatedPassword,
                viewModel = viewModel,
                showVisualityToggleIcon = false,
                isError = viewModel.isErrorRepeatPassword,
                errorMessage = Strings.LOGIN_REPEAT_PASSWORD_ERROR
            )
        }


        /* BUTTONS SECTION */

        val (loginButtonRef) = createRefs()
        val buttonsTopGuideline = createGuidelineFromTop(0.6f)
        val buttonsBottomGuideline = createGuidelineFromTop(0.9f)
        val buttonsStartGuideline = createGuidelineFromStart(0.05f)
        val buttonsEndGuideline = createGuidelineFromEnd(0.05f)

        Column(
            modifier = Modifier
                .constrainAs(loginButtonRef) {
                    top.linkTo(buttonsTopGuideline)
                    bottom.linkTo(buttonsBottomGuideline)
                    start.linkTo(buttonsStartGuideline)
                    end.linkTo(buttonsEndGuideline)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }

        ) {

            /* Login Button */

            CustomOutlinedButton(
                onClick = {
                    if (viewModel.validateEmail() && viewModel.validatePassword() && viewModel.validateRepeatPassword())
                        viewModel.registerViaEmail(context)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = Strings.LOGIN_CREATE_ACCOUNT_BUTTON_TEXT,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = dpToSp(R.dimen.auth_button_text_size)
                )
            }

            /* SOCIAL MEDIA */

            Row(modifier = Modifier.weight(1f)) {

                /* Google Button */
                CustomOutlinedButton(
                    onClick = {
                        viewModel.loginWithGoogle(context, GoogleAuthType.REGISTER)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = painterResource(R.mipmap.ic_google),
                        contentDescription = "",
                        contentScale = ContentScale.Fit
                    )
                }

                /* Facebook Button */
                CustomOutlinedButton(
                    onClick = {
                        viewModel.loginWithFacebook(context)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = painterResource(R.mipmap.ic_fb),
                        contentDescription = "",
                        contentScale = ContentScale.Fit
                    )
                }
            }

            /* DIVIDER SECTION */

            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )

                Text(
                    modifier = Modifier
                        .weight(0.3f)
                        .fillMaxWidth(),
                    text = Strings.LOGIN_DIVIDER_TEXT,
                    textAlign = TextAlign.Center,
                    fontSize = dpToSp(R.dimen.auth_divider_text_size),
                    color = MaterialTheme.colorScheme.onBackground,
                )

                Divider(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }


            /* Sign up Button */

            CustomOutlinedButton(
                onClick = {
                    changeActivity(
                        context = context,
                        activity = LoginActivity::class,
                        finish = true
                    )
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = Strings.LOGIN_LOGIN_BUTTON_TEXT,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = dpToSp(R.dimen.auth_button_text_size)
                )
            }
        }


        /* TERMS SECTION */

        val (termsRef) = createRefs()

        Text(
            text = Strings.LOGIN_TERMS,
            fontSize = dpToSp(R.dimen.auth_terms_text_size),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp)
                .padding(vertical = 24.dp)
                .constrainAs(termsRef) {
                    top.linkTo(buttonsBottomGuideline)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Show1() {
    Theme.MainTheme {
        RegisterView()
    }
}