package com.taekwondo.featureevent.presentation.fighters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
import com.taekwondo.featureevent.presentation.model.JudgeModel
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun UpdateEventFighterScreen(
    navController: NavController,
    viewModel: UpdateEventFighterViewModel = hiltViewModel()
) {
    val judges by viewModel.judges.collectAsState()
    val fighters by viewModel.fighters.collectAsState()
    val event = viewModel.event.receiveAsFlow()
    event.observe {
        when (it) {
            is UpdateEventFighterViewModel.NavigateMainState -> {
                navController.navigate(MainDirection.path) {
                    popUpTo(MainDirection.path) {
                        inclusive = true
                    }
                }
            }
        }
    }
    UpdateEventFighterScreen(
        judges = judges,
        fighters = fighters,
        onUpdateFighter = viewModel::updateFighters,
        onUpdateJudge = viewModel::updateJudges
    ) { viewModel.updateEventFighter(judges, fighters) }
}

@Composable
fun UpdateEventFighterScreen(
    judges: List<JudgeModel>,
    fighters: List<FighterModel>,
    onUpdateFighter: (FighterModel) -> Unit,
    onUpdateJudge: (JudgeModel) -> Unit,
    onSaveClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimen.padding_16)
    ) {
        Text(
            "Бойцы",
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
        Text(
            "Судьи",
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
            items(judges) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Dimen.padding_4),
                    modifier = Modifier.clickable {
                        onUpdateJudge(it)
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
            Button(
                onClick = onSaveClick, modifier = Modifier
                    .padding(top = Dimen.padding_16)
                    .align(
                        Alignment.Center
                    )
            ) {
                Text(text = string(id = R.string.button_save))
            }
        }
    }
}