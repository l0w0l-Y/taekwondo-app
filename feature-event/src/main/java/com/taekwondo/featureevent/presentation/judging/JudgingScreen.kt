package com.taekwondo.featureevent.presentation.judging

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.taekwondo.corecommon.ext.observe
import com.taekwondo.corenavigation.MainDirection
import com.taekwondo.coretheme.Dimen
import com.taekwondo.coreui.compose.string
import com.taekwondo.featureevent.R
import com.taekwondo.featureevent.presentation.model.FighterModel
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun JudgingScreen(
    navController: NavController,
    viewModel: JudgingViewModel = hiltViewModel(),
) {
    val round by viewModel.round.collectAsState()
    val fighters by viewModel.fighters.collectAsState()
    val selectedFighters by viewModel.selectedFighters.collectAsState()
    val event = viewModel.event.receiveAsFlow()
    var isFightersSelected by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val errorMessage = string(id = R.string.error_select_fighters)
    event.observe {
        when (it) {
            is JudgingViewModel.JudgingState -> {
                isFightersSelected = true
            }

            is JudgingViewModel.ErrorState -> {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
    JudgingScreen(
        isFightersSelected,
        fighters,
        selectedFighters,
        round,
        viewModel::updateFighters,
        viewModel::onCheckFighters,
        { navController.navigate(MainDirection.path) }
    ) {
        viewModel.savePoints(it)
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun JudgingScreen(
    isFightersSelected: Boolean,
    fighters: List<FighterModel>,
    selectedFighters: List<FighterModel>,
    round: Int,
    onUpdateFighter: (FighterModel) -> Unit,
    onCheckFighters: () -> Unit,
    onComplete: () -> Unit,
    savePoints: (Pair<Float, Float>) -> Unit
) {
    var resultFirst by remember { mutableFloatStateOf(0f) }
    var resultSecond by remember { mutableFloatStateOf(0f) }
    var isFightersOpen by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = round) {
        resultFirst = 0f
        resultSecond = 0f
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimen.padding_16)
    ) {
        if (!isFightersSelected) {
            Column(Modifier.fillMaxSize()) {
                Text(
                    string(id = R.string.title_fighter),
                    modifier = Modifier.padding(top = Dimen.padding_16),
                    style = MaterialTheme.typography.titleMedium
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = Dimen.padding_12),
                    horizontalArrangement = Arrangement.spacedBy(Dimen.padding_12),
                ) {
                    items(fighters) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(Dimen.padding_4),
                            modifier = Modifier.clickable {
                                onUpdateFighter(it)
                            }) {
                            AsyncImage(
                                model = it.photo,
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(Dimen.radius_8))
                                    .width(80.dp)
                                    .height(140.dp),
                                contentScale = ContentScale.Crop,
                                alpha = if (it.isPicked) 1.0f else 0.5f
                            )
                            Text(
                                text = it.name,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
                Box(modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = onCheckFighters, modifier = Modifier.align(Alignment.Center)) {
                        Text(string(id = R.string.button_select_fighters))
                    }
                }
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimen.padding_12)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { isFightersOpen = !isFightersOpen }
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = string(id = R.string.title_selected_fighters))
                    AnimatedVisibility(visible = isFightersOpen) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(Dimen.padding_20),
                        ) {
                            items(selectedFighters) {
                                Column {
                                    AsyncImage(
                                        model = it.photo,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(Dimen.radius_8))
                                            .width(80.dp)
                                            .height(140.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                    Text(
                                        text = it.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
                Icon(
                    imageVector = if (isFightersOpen) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = null
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(string(id = R.string.title_round, round))
                ScorePanelScreen(round, Modifier.fillMaxWidth()) { resultFirst += it }
                Text(
                    text = string(id = R.string.title_points, String.format("%.2f", resultFirst)),
                    modifier = Modifier.padding(top = Dimen.padding_4)
                )
                if (selectedFighters.size > 1) {
                    ScorePanelScreen(round, Modifier.fillMaxWidth()) { resultSecond += it }
                    Text(
                        text = string(
                            id = R.string.title_points,
                            String.format("%.2f", resultSecond)
                        ),
                        modifier = Modifier.padding(top = Dimen.padding_4)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimen.padding_4)
                ) {
                    Button(
                        onClick = {
                            savePoints(
                                if (selectedFighters.size == 2) {
                                    resultFirst to resultSecond
                                } else {
                                    resultFirst to 0f
                                }
                            )
                        },
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(text = string(id = R.string.button_next_round))
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimen.padding_4)
                ) {
                    Button(
                        onClick = onComplete,
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(text = string(id = R.string.button_end_fight))
                    }
                }
            }
        }
    }
}

@Composable
fun ScorePanelScreen(
    round: Int,
    modifier: Modifier = Modifier,
    onResultChange: (Float) -> Unit
) {
    Column(modifier) {
        Text(
            text = string(id = R.string.title_kicks),
            modifier = Modifier.padding(top = Dimen.padding_20)
        )
        Row(
            modifier = modifier.padding(top = Dimen.padding_12)
        ) {
            ScoreButton(round, 1.0f, onResultChange)
            ScoreButton(round, 2.0f, onResultChange)
            ScoreButton(round, 3.0f, onResultChange)
            ScoreButton(round, 4.0f, onResultChange)
            ScoreButton(round, 5.0f, onResultChange)
        }
        Text(
            text = string(id = R.string.title_warnings),
            modifier = Modifier.padding(top = Dimen.padding_20)
        )
        Row(
            modifier = modifier.padding(top = Dimen.padding_12)
        ) {
            ScoreButton(round, -0.33f, onResultChange)
            ScoreButton(round, -1.0f, onResultChange)
        }
    }
}

@Composable
fun ScoreButton(round: Int, point: Float, onUpdatePoints: (Float) -> Unit) {
    var isChecked by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = round) {
        isChecked = false
    }
    TextButton(
        onClick = {
            onUpdatePoints(if (!isChecked) point else -point)
            isChecked = !isChecked
        },
        modifier = Modifier.background(if (isChecked) Color.Green else Color.White),
    ) {
        Text(point.toString(), color = Color.Black)
    }
}