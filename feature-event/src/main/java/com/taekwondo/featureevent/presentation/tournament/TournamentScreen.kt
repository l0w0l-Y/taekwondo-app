package com.taekwondo.featureevent.presentation.tournament

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.taekwondo.corecommon.ext.observe
import com.taekwondo.coredata.network.model.TournamentModel
import com.taekwondo.corenavigation.JudgingDirection
import com.taekwondo.coretheme.Dimen
import com.taekwondo.coreui.compose.string
import com.taekwondo.featureevent.R
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun TournamentScreen(
    navController: NavController,
    viewModel: TournamentViewModel = hiltViewModel()
) {
    val list by viewModel.tournament.collectAsState()
    val round by viewModel.round.collectAsState()
    val isJudgingAvailable by viewModel.isJudgingAvailable.collectAsState()
    val event = viewModel.event.receiveAsFlow()
    event.observe {
        when (it) {
            is TournamentViewModel.JudgingState -> {
                navController.navigate(JudgingDirection.path + "?eventId=${it.eventId}&fightId=${it.fightId}")
            }
        }
    }
    TournamentScreen(list, round, isJudgingAvailable, viewModel::onJudge, viewModel::onComplete)
}

@Composable
fun TournamentScreen(
    list: List<TournamentModel>,
    round: Int,
    isJudgingAvailable: Boolean,
    onJudge: (Long) -> Unit,
    onComplete: (Long) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                vertical = Dimen.padding_12,
                horizontal = Dimen.padding_16
            )
    ) {
        Text(
            string(id = R.string.tournament_grid),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimen.padding_8)
        )
        for (item in round downTo 1) {
            TournamentItem(list, item, isJudgingAvailable, onJudge, onComplete)
        }
    }
}

@Composable
fun TournamentItem(
    list: List<TournamentModel>,
    round: Int,
    isJudgingAvailable: Boolean,
    onJudge: (Long) -> Unit,
    onComplete: (Long) -> Unit,
) {
    val roundNumber by remember { mutableIntStateOf(Math.pow(2.0, (round - 1).toDouble()).toInt()) }
    Column() {
        Row(
            modifier = Modifier.padding(Dimen.padding_8),
            horizontalArrangement = Arrangement.spacedBy(Dimen.padding_12)
        ) {
            Text(
                if (roundNumber > 1) "1/$roundNumber" else string(id = R.string.title_final),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
        }
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Dimen.padding_8)
        ) {
            items(list.filter { it.round == round }) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Dimen.padding_12)
                ) {
                    Text(
                        text = string(id = R.string.title_group_fighters, it.group + 1),
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.surface,
                                RoundedCornerShape(Dimen.radius_8)
                            )
                            .padding(
                                vertical = Dimen.padding_4,
                                horizontal = Dimen.padding_8
                            )
                    )
                    Text(
                        it.fighter1?.name ?: string(id = R.string.title_fighter_not_found),
                        modifier = Modifier
                            .padding(top = Dimen.padding_12)
                            .background(
                                if (it.winnerIndex == 0L) Color.Green else Color.Transparent,
                                RoundedCornerShape(Dimen.radius_16)
                            )
                            .padding(Dimen.padding_8),
                        color = if (it.fighter1?.name != null) Color.Black else Color.LightGray
                    )
                    Text(
                        "VS",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = Dimen.padding_8),
                    )
                    Text(
                        it.fighter2?.name ?: string(id = R.string.title_fighter_not_found),
                        modifier = Modifier
                            .padding(top = Dimen.padding_12)
                            .background(
                                if (it.winnerIndex == 1L) Color.Green else Color.Transparent,
                                RoundedCornerShape(Dimen.radius_16)
                            )
                            .padding(Dimen.padding_8),
                        color = if (it.fighter2?.name != null) Color.Black else Color.LightGray
                    )
                    if (isJudgingAvailable) {
                        if (it.fighter1 != null && it.fighter2 != null && it.winnerIndex == null) {
                            Button(onClick = { onJudge(it.id) }) {
                                Text(string(id = R.string.button_open_judging))
                            }
                            TextButton(onClick = { onComplete(it.id) }) {
                                Text(string(R.string.button_close_fight))
                            }
                        }
                    }
                    if (it.fighter1 != null && it.fighter2 != null && it.winnerIndex != null) {
                        Column(modifier = Modifier.padding(top = Dimen.padding_8)) {
                            Text(
                                string(id = R.string.title_judge_choice),
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            it.judgeChoices.forEach { judge ->
                                Text(
                                    text = judge.name + " - " + if (judge.choiceId == 0L) it.fighter1?.name
                                        ?: "" else it.fighter2?.name ?: "",
                                    modifier = Modifier.padding(top = Dimen.padding_8),
                                    color = Color.Gray,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}