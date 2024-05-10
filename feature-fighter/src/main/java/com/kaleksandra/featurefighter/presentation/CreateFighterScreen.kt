package com.kaleksandra.featurefighter.presentation

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import com.taekwondo.coreui.compose.ImageScreen
import com.taekwondo.coreui.compose.string
import com.taekwondo.featurefighter.R
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun CreateFighterScreen(
    navController: NavController,
    navigationState: CreateFighterViewModel.ScreenType,
    viewModel: CreateFighterViewModel = hiltViewModel()
) {
    val event = viewModel.event.receiveAsFlow()
    var name by remember { mutableStateOf("") }
    var years by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weightCategory by remember { mutableStateOf("") }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var state by remember {
        mutableStateOf(navigationState)
    }
    event.observe {
        when (it) {
            CreateFighterViewModel.NavigateMainState -> {
                navController.navigate(MainDirection.path)
            }

            is CreateFighterViewModel.UpdateFighterState -> {
                name = it.name
                years = it.age
                weight = it.weight
                height = it.height
                weightCategory = it.weightCategory
                imageUri = it.photo?.let { Uri.parse(it) }
            }
        }
    }
    CreateFighterScreen(
        name = name,
        onNameChanged = { name = it },
        years = years,
        onYearsChanged = { years = it },
        weight = weight,
        onWeightChanged = { weight = it },
        height = height,
        onHeightChanged = { height = it },
        weightCategory = weightCategory,
        onWeightCategoryChanged = { weightCategory = it },
        imageUri = imageUri,
        onImageUriChanged = { imageUri = it },
        state = state,
        onSaveClick = viewModel::onSaveClick,
        onUpdateState = { state = it },
        onDeleteFighter = viewModel::onDeleteFighter
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFighterScreen(
    name: String,
    onNameChanged: (String) -> Unit,
    years: String,
    onYearsChanged: (String) -> Unit,
    weight: String,
    onWeightChanged: (String) -> Unit,
    height: String,
    onHeightChanged: (String) -> Unit,
    weightCategory: String,
    onWeightCategoryChanged: (String) -> Unit,
    imageUri: Uri?,
    onImageUriChanged: (Uri?) -> Unit = {},
    state: CreateFighterViewModel.ScreenType,
    onSaveClick: (String, Float, Float, Float, String, String?) -> Unit,
    onUpdateState: (CreateFighterViewModel.ScreenType) -> Unit,
    onDeleteFighter: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        ImageScreen(imageUri, onImageUpdate = onImageUriChanged)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            TextField(
                value = name,
                onValueChange = onNameChanged,
                label = { Text(string(id = R.string.text_field_name)) },
                modifier = Modifier.padding(top = Dimen.padding_36),
                readOnly = state is CreateFighterViewModel.Read,
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
                value = years,
                onValueChange = onYearsChanged,
                label = { Text(string(id = R.string.text_field_year)) },
                modifier = Modifier.padding(top = Dimen.padding_36),
                readOnly = state is CreateFighterViewModel.Read,
                singleLine = true,
                shape = RoundedCornerShape(Dimen.padding_16),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.onSecondary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(KeyboardActions.Default.onNext)
            )
            TextField(
                value = weight,
                onValueChange = onWeightChanged,
                label = { Text(string(id = R.string.text_field_weight)) },
                modifier = Modifier.padding(top = Dimen.padding_36),
                readOnly = state is CreateFighterViewModel.Read,
                singleLine = true,
                shape = RoundedCornerShape(Dimen.padding_16),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.onSecondary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(KeyboardActions.Default.onNext)
            )
            TextField(
                value = height,
                onValueChange = onHeightChanged,
                label = { Text(string(id = R.string.text_field_height)) },
                modifier = Modifier.padding(top = Dimen.padding_36),
                readOnly = state is CreateFighterViewModel.Read,
                singleLine = true,
                shape = RoundedCornerShape(Dimen.padding_16),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = MaterialTheme.colorScheme.onSecondary,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(KeyboardActions.Default.onNext)
            )
            TextField(
                value = weightCategory,
                onValueChange = onWeightCategoryChanged,
                label = { Text(string(id = R.string.text_field_weight_category)) },
                modifier = Modifier.padding(top = Dimen.padding_36),
                readOnly = state is CreateFighterViewModel.Read,
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
        }
        Box {
            when (state) {
                CreateFighterViewModel.Create -> {
                    Button(
                        onClick = {
                            onSaveClick(
                                name,
                                years.toFloat(),
                                weight.toFloat(),
                                height.toFloat(),
                                weightCategory,
                                imageUri?.toString(),
                            )
                        },
                        modifier = Modifier.padding(top = Dimen.padding_16)
                    ) {
                        Text(text = string(id = R.string.button_create))
                    }
                }

                CreateFighterViewModel.Read -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(Dimen.padding_16)) {
                        Button(
                            onClick = {
                                onUpdateState(CreateFighterViewModel.Update)
                            },
                            modifier = Modifier.padding(top = Dimen.padding_16)
                        ) {
                            Text(text = string(id = R.string.button_update))
                        }

                        Button(
                            onClick = {
                                onDeleteFighter()
                            },
                            modifier = Modifier.padding(top = Dimen.padding_16)
                        ) {
                            Text(text = string(id = R.string.button_delete))
                        }
                    }
                }

                CreateFighterViewModel.Update -> {
                    Button(
                        onClick = {
                            onSaveClick(
                                name,
                                years.toFloat(),
                                weight.toFloat(),
                                height.toFloat(),
                                weightCategory,
                                imageUri?.toString(),
                            )
                        },
                        modifier = Modifier.padding(top = Dimen.padding_16)
                    ) {
                        Text(text = string(id = R.string.button_save))
                    }
                }
            }
        }
    }
}