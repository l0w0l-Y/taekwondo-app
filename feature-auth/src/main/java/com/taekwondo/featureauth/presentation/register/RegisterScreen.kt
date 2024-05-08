package com.taekwondo.featureauth.presentation.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.taekwondo.corecommon.ext.observe
import com.taekwondo.corenavigation.MainDirection
import com.taekwondo.coretheme.Dimen
import com.taekwondo.coreui.compose.string
import com.taekwondo.featureauth.R
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterViewModel = hiltViewModel()) {
    val event = viewModel.event.receiveAsFlow()
    event.observe {
        when (it) {
            RegisterViewModel.NavigateMainState -> {
                navController.navigate(MainDirection.path)
            }
        }
    }
    RegisterScreen(onSave = viewModel::onRegister)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(onSave: (String, String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text(string(id = R.string.text_field_name)) },
            modifier = Modifier.padding(top = Dimen.padding_36),
            singleLine = true,
            shape = RoundedCornerShape(Dimen.padding_16),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.onSecondary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            keyboardActions = KeyboardActions(KeyboardActions.Default.onNext)
        )
        TextField(
            value = surname,
            onValueChange = { surname = it },
            placeholder = { Text(string(id = R.string.text_field_surname)) },
            modifier = Modifier.padding(top = Dimen.padding_36),
            singleLine = true,
            shape = RoundedCornerShape(Dimen.padding_16),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.onSecondary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            keyboardActions = KeyboardActions(KeyboardActions.Default.onNext)
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text(string(id = R.string.text_field_email)) },
            modifier = Modifier.padding(top = Dimen.padding_36),
            singleLine = true,
            shape = RoundedCornerShape(Dimen.padding_16),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.onSecondary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            keyboardActions = KeyboardActions(KeyboardActions.Default.onNext)
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text(string(id = R.string.text_field_password)) },
            modifier = Modifier.padding(top = Dimen.padding_36),
            singleLine = true,
            shape = RoundedCornerShape(Dimen.padding_16),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.onSecondary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            keyboardActions = KeyboardActions(KeyboardActions.Default.onNext)
        )
        Button(
            onClick = { onSave(name, surname, email, password) },
            modifier = Modifier.padding(top = Dimen.padding_16)
        ) {
            Text(text = string(id = R.string.button_register))
        }
    }
}