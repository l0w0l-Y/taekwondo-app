package com.taekwondo.featurefighter.presentation

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AssistantPhoto
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.taekwondo.corecommon.ext.observe
import com.taekwondo.coredata.network.enums.Gender
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
    var club by remember { mutableStateOf("") }
    var trainer by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf(Gender.MALE) }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    var state by remember {
        mutableStateOf(navigationState)
    }
    var totalFights by remember { mutableIntStateOf(0) }
    var wins by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val error = string(id = R.string.error_field_required)
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
                gender = it.gender
                club = it.club
                trainer = it.trainer
                imageUri = it.photo?.let { Uri.parse(it) }
                totalFights = it.totalFights
                wins = it.wins
            }

            CreateFighterViewModel.ErrorState -> {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
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
        club = club,
        onClubChanged = { club = it },
        trainer = trainer,
        onTrainerChanged = { trainer = it },
        gender = gender,
        onGenderChanged = { gender = it },
        state = state,
        totalFights = totalFights,
        wins = wins,
        onSaveClick = viewModel::onSaveClick,
        onUpdateState = { state = it },
        onDeleteFighter = viewModel::onDeleteFighter,
        onUploadClick = viewModel::onUploadClick
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
    club: String,
    onClubChanged: (String) -> Unit,
    trainer: String,
    onTrainerChanged: (String) -> Unit,
    gender: Gender,
    onGenderChanged: (Gender) -> Unit,
    imageUri: Uri?,
    onImageUriChanged: (Uri?) -> Unit = {},
    state: CreateFighterViewModel.ScreenType,
    totalFights: Int,
    wins: Int,
    onSaveClick: (String, Float?, Float?, Float?, String, String?, Gender, String, String) -> Unit,
    onUpdateState: (CreateFighterViewModel.ScreenType) -> Unit,
    onDeleteFighter: () -> Unit,
    onUploadClick: (Uri) -> Unit,
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            onUploadClick(uri)
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = Dimen.padding_8)
    ) {
        if (state == CreateFighterViewModel.Create) {
            Box(Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        launcher.launch(
                            arrayOf(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                                "application/vnd.ms-excel"
                            )
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = Dimen.padding_16)
                ) {
                    Text(text = string(id = R.string.button_upload))
                }
            }
        }
        ImageScreen(
            imageUri,
            onImageUpdate = onImageUriChanged,
            modifier = Modifier.padding(top = Dimen.padding_8),
            enabled = state !is CreateFighterViewModel.Read
        )
        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = Dimen.padding_8),
            verticalArrangement = Arrangement.spacedBy(Dimen.padding_8),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(Dimen.padding_8)) {
                Icon(
                    Icons.Outlined.AssistantPhoto,
                    contentDescription = null,
                    modifier = Modifier.size(Dimen.size_24),
                    tint = MaterialTheme.colorScheme.primary
                )
                Text("Победы: $wins", color = MaterialTheme.colorScheme.primary)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(Dimen.padding_8)) {
                Icon(
                    Icons.Outlined.Badge,
                    contentDescription = null,
                    modifier = Modifier.size(Dimen.size_24)
                )
                Text("Участия: $totalFights")
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = onNameChanged,
                label = { Text(string(id = R.string.text_field_name)) },
                modifier = Modifier.padding(top = Dimen.padding_12),
                enabled = state !is CreateFighterViewModel.Read,
                singleLine = true,
                shape = RoundedCornerShape(Dimen.padding_16),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimen.padding_8)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimen.padding_4),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = string(id = R.string.title_gender_male))
                    RadioButton(
                        selected = gender == Gender.MALE,
                        onClick = { onGenderChanged(Gender.MALE) },
                        enabled = state !is CreateFighterViewModel.Read,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Dimen.padding_4),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = string(id = R.string.title_gender_female))
                    RadioButton(
                        selected = gender == Gender.FEMALE,
                        onClick = { onGenderChanged(Gender.FEMALE) },
                        enabled = state !is CreateFighterViewModel.Read,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
            OutlinedTextField(
                value = years,
                onValueChange = onYearsChanged,
                label = { Text(string(id = R.string.text_field_year)) },
                modifier = Modifier.padding(top = Dimen.padding_12),
                enabled = state !is CreateFighterViewModel.Read,
                singleLine = true,
                shape = RoundedCornerShape(Dimen.padding_16),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
            )
            OutlinedTextField(
                value = weight,
                onValueChange = onWeightChanged,
                label = { Text(string(id = R.string.text_field_weight)) },
                modifier = Modifier.padding(top = Dimen.padding_12),
                enabled = state !is CreateFighterViewModel.Read,
                singleLine = true,
                shape = RoundedCornerShape(Dimen.padding_16),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
            )
            OutlinedTextField(
                value = height,
                onValueChange = onHeightChanged,
                label = { Text(string(id = R.string.text_field_height)) },
                modifier = Modifier.padding(top = Dimen.padding_12),
                enabled = state !is CreateFighterViewModel.Read,
                singleLine = true,
                shape = RoundedCornerShape(Dimen.padding_16),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
            )
            OutlinedTextField(
                value = weightCategory,
                onValueChange = onWeightCategoryChanged,
                label = { Text(string(id = R.string.text_field_weight_category)) },
                modifier = Modifier.padding(top = Dimen.padding_12),
                enabled = state !is CreateFighterViewModel.Read,
                singleLine = true,
                shape = RoundedCornerShape(Dimen.padding_16),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
            )
            OutlinedTextField(
                value = club,
                onValueChange = onClubChanged,
                label = { Text(string(id = R.string.text_field_club)) },
                modifier = Modifier.padding(top = Dimen.padding_12),
                enabled = state !is CreateFighterViewModel.Read,
                singleLine = true,
                shape = RoundedCornerShape(Dimen.padding_16),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
            )
            OutlinedTextField(
                value = trainer,
                onValueChange = onTrainerChanged,
                label = { Text(string(id = R.string.text_field_trainer)) },
                modifier = Modifier.padding(top = Dimen.padding_12),
                enabled = state !is CreateFighterViewModel.Read,
                singleLine = true,
                shape = RoundedCornerShape(Dimen.padding_16),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
            )
        }
        Box {
            when (state) {
                CreateFighterViewModel.Create -> {
                    Button(
                        onClick = {
                            onSaveClick(
                                name,
                                years.toFloatOrNull(),
                                weight.toFloatOrNull(),
                                height.toFloatOrNull(),
                                weightCategory,
                                imageUri?.toString(),
                                gender,
                                club,
                                trainer
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
                                years.toFloatOrNull(),
                                weight.toFloatOrNull(),
                                height.toFloatOrNull(),
                                weightCategory,
                                imageUri?.toString(),
                                gender,
                                club,
                                trainer
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