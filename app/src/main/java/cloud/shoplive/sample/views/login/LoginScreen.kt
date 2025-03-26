package cloud.shoplive.sample.views.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cloud.shoplive.sample.R

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel
) {
    val state by viewModel.loginUiState.collectAsState()

    LoginScreen(
        modifier = modifier,
        onLogin = viewModel::saveUser,
        onIdChanged = viewModel::onIdChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        loginUiState = state
    )
}

@Composable
private fun LoginScreen(
    modifier: Modifier = Modifier,
    onLogin: () -> Unit,
    onIdChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    loginUiState: LoginUiState
) {
    Column(
        modifier = modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        OutlineTextFieldForUserInfo(
            value = loginUiState.id,
            onValueChanged = onIdChanged,
            placeholderRes = R.string.hint_login_id
        )
        OutlineTextFieldForUserInfo(
            value = loginUiState.password,
            onValueChanged = onPasswordChanged,
            placeholderRes = R.string.hint_login_pw
        )
        Button(
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = colorResource(R.color.color_500)
            ),
            onClick = onLogin,
            enabled = loginUiState.password.isNotEmpty() && loginUiState.id.isNotEmpty()
        ) {
            Row(
                modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.bt_login)
                )
            }
        }
    }
}

@Composable
private fun OutlineTextFieldForUserInfo(
    modifier: Modifier = Modifier,
    value: String,
    onValueChanged: (String) -> Unit,
    placeholderRes: Int
) {
    OutlinedTextField(
        colors = OutlinedTextFieldDefaults.colors().copy(
            focusedIndicatorColor = colorResource(R.color.color_200)
        ),
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChanged,
        placeholder = {
            Text(
                text = stringResource(id = placeholderRes)
            )
        }
    )
}

@Composable
@Preview(showBackground = true)
private fun LoginPreview() {
    LoginScreen(
        loginUiState = LoginUiState(
            id = "",
            password = ""
        ),
        onLogin = {},
        onIdChanged = {},
        onPasswordChanged = {}
    )
}