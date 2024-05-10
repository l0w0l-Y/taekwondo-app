package com.taekwondo.featureauth.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.taekwondo.corecommon.ext.observe
import com.taekwondo.corenavigation.MainDirection
import com.taekwondo.corenavigation.RegisterDirection
import com.taekwondo.corenavigation.navigate
import com.taekwondo.coretheme.AppTheme
import com.taekwondo.coretheme.Dimen
import com.taekwondo.coreui.compose.string
import com.taekwondo.featureauth.R
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun AuthScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    val event = viewModel.event.receiveAsFlow()
    val context = LocalContext.current
    event.observe {
        when (it) {
            AuthViewModel.NavigateMainState -> {
                navController.navigate(MainDirection.path) {
                    popUpTo(MainDirection.path) {
                        inclusive = true
                    }
                }
            }

            AuthViewModel.ErrorState -> {
                Toast.makeText(context, "Неправильный логин или пароль", Toast.LENGTH_SHORT).show()
            }
        }
    }
    AuthScreen(viewModel::auth) { navController.navigate(RegisterDirection) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(onSaveClick: (String, String) -> Unit, onRegisterClick: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(string(id = R.string.text_field_email)) },
            modifier = Modifier.padding(top = Dimen.padding_36),
            singleLine = true,
            shape = RoundedCornerShape(Dimen.padding_16),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            keyboardActions = KeyboardActions(KeyboardActions.Default.onNext)
        )
        OutlinedTextField(
            value = password,
            placeholder = { Text(string(id = R.string.text_field_password)) },
            onValueChange = { password = it },
            modifier = Modifier.padding(top = Dimen.padding_12),
            singleLine = true,
            shape = RoundedCornerShape(Dimen.padding_16),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            keyboardActions = KeyboardActions(KeyboardActions.Default.onGo),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
                val description =
                    if (passwordVisible) string(id = R.string.cd_hide_password) else string(id = R.string.cd_show_password)

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            }
        )
        Button(
            onClick = { onSaveClick(email, password) },
            modifier = Modifier.padding(top = Dimen.padding_16)
        ) {
            Text(text = string(id = R.string.button_login))
        }
        TextButton(onClick = onRegisterClick) {
            Text(text = string(id = R.string.button_register))
        }
    }
}

@Composable
@Preview
fun PreviewAuthScreen() {
    AppTheme {
        AuthScreen(rememberNavController())
    }
}