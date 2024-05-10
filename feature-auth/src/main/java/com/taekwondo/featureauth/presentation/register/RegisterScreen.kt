package com.taekwondo.featureauth.presentation.register

import android.content.Context
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.taekwondo.corecommon.ext.observe
import com.taekwondo.corenavigation.MainDirection
import com.taekwondo.coretheme.Dimen
import com.taekwondo.coreui.compose.ImageScreen
import com.taekwondo.coreui.compose.string
import com.taekwondo.featureauth.R
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterViewModel = hiltViewModel()) {
    val event = viewModel.event.receiveAsFlow()
    val context = LocalContext.current
    event.observe {
        when (it) {
            RegisterViewModel.NavigateMainState -> {
                navController.navigate(MainDirection.path) {
                    popUpTo(MainDirection.path) {
                        inclusive = true
                    }
                }
            }

            is RegisterViewModel.ErrorState -> {
                sendToast(context, it.message)
            }
        }
    }
    RegisterScreen(
        onSave = viewModel::onRegister
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onSave: (String, String, String, String, String, String?) -> Unit
) {
    var errorMessage = string(id = R.string.error_field_required)
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var isValidEmail by remember { mutableStateOf(true) }
    var password by remember { mutableStateOf("") }
    var isValidPassword by remember { mutableStateOf(true) }
    var phone by remember { mutableStateOf("") }
    var isValidPhone by remember { mutableStateOf(true) }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        ImageScreen(
            imageUri,
            onImageUpdate = { imageUri = it },
            modifier = Modifier.padding(top = Dimen.padding_16)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(string(id = R.string.text_field_name)) },
                modifier = Modifier.padding(top = Dimen.padding_12),
                singleLine = true,
                shape = RoundedCornerShape(Dimen.padding_16),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
            )
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(string(id = R.string.text_field_username)) },
                modifier = Modifier.padding(top = Dimen.padding_8),
                singleLine = true,
                shape = RoundedCornerShape(Dimen.padding_16),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(KeyboardActions.Default.onNext)
            )
            OutlinedTextField(
                value = phone,
                onValueChange = {
                    phone = it
                    isValidPhone = isPhoneValid(it)
                },
                label = { Text(string(id = R.string.text_field_phone)) },
                modifier = Modifier.padding(top = Dimen.padding_8),
                singleLine = true,
                shape = RoundedCornerShape(Dimen.padding_16),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(KeyboardActions.Default.onNext),
                isError = !isValidPhone
            )
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    isValidEmail = isEmailValid(it)
                },
                label = { Text(string(id = R.string.text_field_email)) },
                modifier = Modifier.padding(top = Dimen.padding_8),
                singleLine = true,
                shape = RoundedCornerShape(Dimen.padding_16),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(KeyboardActions.Default.onNext),
                isError = !isValidEmail,
            )
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    isValidPassword = isPasswordValid(it)
                },
                label = { Text(string(id = R.string.text_field_password)) },
                modifier = Modifier.padding(top = Dimen.padding_8),
                singleLine = true,
                shape = RoundedCornerShape(Dimen.padding_16),
                isError = !isValidPassword,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (isValidEmail && isValidPassword && isValidPhone) {
                            onSave(name, username, email, password, phone, imageUri?.toString())
                        } else {
                            sendToast(context, errorMessage)
                        }
                    }
                ),
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
                onClick = {
                    if (isValidEmail && isValidPassword && isValidPhone) {
                        onSave(name, username, email, password, phone, imageUri?.toString())
                    } else {
                        sendToast(context, errorMessage)
                    }
                },
                modifier = Modifier.padding(top = Dimen.padding_16)
            ) {
                Text(text = string(id = R.string.button_register))
            }
        }
    }
}

fun isEmailValid(email: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun isPhoneValid(phone: String): Boolean {
    return Patterns.PHONE.matcher(phone).matches()
}

fun isPasswordValid(password: String): Boolean {
    return password.length >= 6
}

fun sendToast(context: Context, errorMessage: String) {
    Toast.makeText(
        context,
        errorMessage,
        Toast.LENGTH_SHORT
    ).show()
}
