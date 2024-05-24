package com.taekwondo.featureevent.presentation.event

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.taekwondo.corecommon.ext.observe
import com.taekwondo.coredata.network.model.ResultFighterModel
import com.taekwondo.corenavigation.JudgingDirection
import com.taekwondo.corenavigation.MainDirection
import com.taekwondo.corenavigation.UpdateEventDirection
import com.taekwondo.corenavigation.UpdateEventFighterDirection
import com.taekwondo.coretheme.Dimen
import com.taekwondo.coreui.compose.string
import com.taekwondo.featureevent.R
import com.taekwondo.featureevent.presentation.model.EventModel
import com.taekwondo.featureevent.presentation.model.EventStatus
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun EventScreen(
    navController: NavController,
    eventId: Long?,
    viewModel: EventViewModel = hiltViewModel()
) {
    val eventModel by viewModel.eventModel.collectAsState()
    val event = viewModel.event.receiveAsFlow()
    val isJudgingAvailable by viewModel.isJudgingAvailable.collectAsState()
    val results by viewModel.resultModel.collectAsState()
    event.observe {
        when (it) {
            is EventViewModel.NavigateUpdateParticipantsState -> {
                navController.navigate("${UpdateEventFighterDirection.path}?uid=${it.uid}")
            }

            is EventViewModel.NavigateUpdateEventState -> {
                navController.navigate("${UpdateEventDirection.path}?uid=${it.uid}")
            }

            is EventViewModel.NavigateMainState -> {
                navController.navigate(MainDirection.path) {
                    popUpTo(MainDirection.path) {
                        inclusive = true
                    }
                }
            }
        }
    }
    EventScreen(
        isJudgingAvailable = isJudgingAvailable,
        eventModel = eventModel,
        results = results,
        onUpdateParticipants = viewModel::onUpdateParticipants,
        onUpdateEvent = viewModel::onUpdateEvent,
        onJudging = { navController.navigate("${JudgingDirection.path}?eventId=$eventId") },
        onCompleteEvent = viewModel::onCompleteEvent,
        onDeleteEvent = viewModel::onDeleteEvent,
    )
}

@Composable
fun EventScreen(
    isJudgingAvailable: Boolean,
    eventModel: EventModel?,
    results: List<ResultFighterModel>,
    onUpdateParticipants: () -> Unit,
    onUpdateEvent: () -> Unit,
    onJudging: () -> Unit,
    onCompleteEvent: () -> Unit,
    onDeleteEvent: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimen.padding_16)
            .verticalScroll(rememberScrollState())
    ) {
        eventModel?.let {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = it.name, style = MaterialTheme.typography.titleMedium)
                    Text(text = it.date, style = MaterialTheme.typography.titleMedium)
                    Text(text = it.place, style = MaterialTheme.typography.titleMedium)
                }
                IconButton(onClick = onUpdateEvent) {
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
                }
            }
            when (eventModel.status) {
                EventStatus.IN_PROGRESS -> {
                    if (isJudgingAvailable) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = Dimen.padding_12)
                        ) {
                            Button(
                                onClick = onJudging,
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                Text(text = string(R.string.button_judging))
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Dimen.padding_4)
                    ) {
                        Button(
                            onClick = onCompleteEvent,
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Text(text = string(R.string.button_complete_event))
                        }
                    }
                }

                EventStatus.FINISHED -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = Dimen.padding_12)
                    ) {
                        Button(
                            onClick = onDeleteEvent,
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Text(text = string(R.string.button_delete_event))
                        }
                    }
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(Dimen.padding_12),
                        modifier = Modifier.padding(top = Dimen.padding_8)
                    ) {
                        items(results) {
                            Column(
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colorScheme.surface,
                                        shape = RoundedCornerShape(Dimen.radius_8)
                                    )
                                    .padding(Dimen.radius_4),
                            ) {
                                Text(
                                    text = string(id = R.string.title_winner),
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = it.winner,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(top = Dimen.padding_2)
                                )
                                Text(
                                    text = string(id = R.string.title_loser),
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = Dimen.padding_8)
                                )
                                Text(
                                    text = it.loser,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.padding(top = Dimen.padding_2)
                                )
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier.padding(top = Dimen.padding_4),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    string(R.string.title_main_judge),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                IconButton(onClick = onUpdateParticipants) {
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
                }
            }
            Row(
                modifier = Modifier.padding(top = Dimen.padding_4),
                horizontalArrangement = Arrangement.spacedBy(Dimen.padding_12),
            ) {
                it.mainJudge?.let { mainJudge ->
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Dimen.padding_4)
                    ) {
                        AsyncImage(
                            model = mainJudge.photo,
                            contentDescription = null,
                            modifier = Modifier
                                .clip(RoundedCornerShape(Dimen.radius_8))
                                .width(80.dp)
                                .height(140.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = mainJudge.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.width(100.dp)
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.padding(top = Dimen.padding_4),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    string(R.string.title_judges_size, eventModel.judges.size),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                IconButton(onClick = onUpdateParticipants) {
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
                }
            }
            LazyRow(
                modifier = Modifier.padding(top = Dimen.padding_4),
                horizontalArrangement = Arrangement.spacedBy(Dimen.padding_12),
            ) {
                items(it.judges) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Dimen.padding_4)
                    ) {
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
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.width(100.dp)
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.padding(top = Dimen.padding_4),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = string(R.string.title_fighters_size, eventModel.fighters.size),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                IconButton(onClick = onUpdateParticipants) {
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = null)
                }
            }
            LazyRow(
                modifier = Modifier.padding(top = Dimen.padding_4),
                horizontalArrangement = Arrangement.spacedBy(Dimen.padding_12),
            ) {
                items(it.fighters) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Dimen.padding_4)
                    ) {
                        if (it.photo != null) {
                            AsyncImage(
                                model = it.photo,
                                contentDescription = null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(Dimen.radius_8))
                                    .width(80.dp)
                                    .height(140.dp),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .width(80.dp)
                                    .height(140.dp)
                                    .clip(RoundedCornerShape(Dimen.radius_8))
                                    .background(MaterialTheme.colorScheme.surface)
                            ) {
                                Icon(
                                    painter = painterResource(id = com.taekwondo.coreui.R.drawable.ic_placeholder),
                                    contentDescription = null,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.width(100.dp)
                        )
                    }
                }
            }
        }
    }
}