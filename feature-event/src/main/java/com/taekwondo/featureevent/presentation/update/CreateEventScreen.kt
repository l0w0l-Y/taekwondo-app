package com.taekwondo.featureevent.presentation.update

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.taekwondo.corecommon.ext.observe
import com.taekwondo.corenavigation.MainDirection
import com.taekwondo.coretheme.Dimen
import com.taekwondo.coreui.compose.string
import com.taekwondo.featureevent.R
import kotlinx.coroutines.flow.receiveAsFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(navController: NavController, viewModel: CreateEventViewModel = hiltViewModel()) {
    val event = viewModel.event.receiveAsFlow()
    val context = LocalContext.current
    val error = string(id = R.string.error_field_required)
    event.observe {
        when (it) {
            CreateEventViewModel.NavigateMainState -> {
                navController.navigate(MainDirection.path) {
                    popUpTo(MainDirection.path) {
                        inclusive = true
                    }
                }
            }

            CreateEventViewModel.ErrorState -> {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        }
    }
    CreateEventScreen(viewModel::createEvent)
}

@ExperimentalMaterial3Api
@Composable
fun CreateEventScreen(saveEvent: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var place by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long): Boolean {
            return utcTimeMillis >= System.currentTimeMillis()
        }
    })
    var date by remember { mutableStateOf("") }
    val interactionSource = remember { MutableInteractionSource() }
    if (showDatePicker) {
        Dialog(
            onDismissRequest = { showDatePicker = false },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            )
        ) {
            Column(
                Modifier.background(Color.White, RoundedCornerShape(Dimen.padding_16))
            ) {
                DatePicker(
                    state = datePickerState,
                    dateFormatter = DatePickerDefaults.dateFormatter()
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = Dimen.padding_4, bottom = Dimen.padding_4)
                ) {
                    Button(
                        modifier = Modifier.align(Alignment.BottomEnd),
                        onClick = {
                            datePickerState.selectedDateMillis?.let {
                                date = convertMillisToDate(it)
                            }
                            showDatePicker = false
                        }) {
                        Text(string(id = R.string.button_select_date))
                    }
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = Dimen.padding_20),
        verticalArrangement = Arrangement.spacedBy(Dimen.padding_8),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = string(id = R.string.title_create_event),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(string(id = R.string.text_field_event_name)) },
            singleLine = true,
            shape = RoundedCornerShape(Dimen.padding_16),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.padding(top = Dimen.padding_8)
        )
        OutlinedTextField(
            value = date,
            onValueChange = {},
            enabled = false,
            readOnly = true,
            label = { Text(string(id = R.string.text_field_event_date)) },
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    showDatePicker = true
                },
            singleLine = true,
            shape = RoundedCornerShape(Dimen.padding_16),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
        )
        OutlinedTextField(
            value = place,
            onValueChange = { place = it },
            label = { Text(string(id = R.string.text_field_event_place)) },
            singleLine = true,
            shape = RoundedCornerShape(Dimen.padding_16),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
        )
        Button(onClick = { saveEvent(name, date, place) }) {
            Text(string(id = R.string.button_save_event))
        }
    }
}

private fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}