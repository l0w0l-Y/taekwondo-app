package com.taekwondo.featureevent.presentation.fighters

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.taekwondo.corecommon.ext.observe
import com.taekwondo.coredata.network.model.FighterModel
import com.taekwondo.corenavigation.MainDirection
import com.taekwondo.coretheme.Dimen
import com.taekwondo.coreui.compose.string
import com.taekwondo.featureevent.R
import com.taekwondo.featureevent.presentation.model.JudgeModel
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun UpdateEventFighterScreen(
    navController: NavController, viewModel: UpdateEventFighterViewModel = hiltViewModel()
) {
    val judges by viewModel.judges.collectAsState()
    val fighters by viewModel.fighters.collectAsState()
    val mainJudges by viewModel.mainJudges.collectAsState()
    val event = viewModel.event.receiveAsFlow()
    val context = LocalContext.current
    val errorMainJudgeMessage = string(id = R.string.error_message_main_judge)
    val errorZeroJudgesMessage = string(id = R.string.error_message_zero_judges)
    val errorZeroFightersMessage = string(id = R.string.error_message_zero_fighters)
    val errorCountFightersMessage = string(id = R.string.error_count_fighters)
    event.observe {
        when (it) {
            is UpdateEventFighterViewModel.NavigateMainState -> {
                navController.navigate(MainDirection.path) {
                    popUpTo(MainDirection.path) {
                        inclusive = true
                    }
                }
            }

            is UpdateEventFighterViewModel.ErrorMainJudgesState -> {
                Toast.makeText(context, errorMainJudgeMessage, Toast.LENGTH_SHORT).show()
            }

            UpdateEventFighterViewModel.ErrorZeroFightersState -> {
                Toast.makeText(context, errorZeroFightersMessage, Toast.LENGTH_SHORT).show()
            }

            UpdateEventFighterViewModel.ErrorZeroJudgesState -> {
                Toast.makeText(context, errorZeroJudgesMessage, Toast.LENGTH_SHORT).show()
            }

            UpdateEventFighterViewModel.ErrorCountFightersState -> {
                Toast.makeText(context, errorCountFightersMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
    UpdateEventFighterScreen(
        mainJudges = mainJudges,
        judges = judges,
        fighters = fighters,
        onUpdateFighter = viewModel::updateFighters,
        onUpdateJudge = viewModel::updateJudges,
        onUpdateMainJudge = viewModel::updateMainJudge,
    ) { viewModel.updateEventFighter(judges, fighters, mainJudges) }
}

@Composable
fun UpdateEventFighterScreen(
    mainJudges: List<JudgeModel>,
    judges: List<JudgeModel>,
    fighters: List<FighterModel>,
    onUpdateFighter: (FighterModel) -> Unit,
    onUpdateJudge: (JudgeModel) -> Unit,
    onUpdateMainJudge: (JudgeModel) -> Unit,
    onSaveClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Dimen.padding_16)
    ) {
        Text(
            string(id = R.string.title_fighter),
            modifier = Modifier.padding(top = Dimen.padding_16),
            style = MaterialTheme.typography.titleMedium
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimen.padding_12),
            horizontalArrangement = Arrangement.spacedBy(Dimen.padding_12),
        ) {
            items(fighters) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Dimen.padding_4),
                    modifier = Modifier.clickable {
                        onUpdateFighter(it)
                    }
                ) {
                    if (it.photo != null) {
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
                    } else {
                        Box(
                            modifier = Modifier
                                .width(80.dp)
                                .height(140.dp)
                                .clip(RoundedCornerShape(Dimen.radius_8))
                                .background(MaterialTheme.colorScheme.surface)
                                .alpha(if (it.isPicked) 1.0f else 0.5f)
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
        Text(
            string(id = R.string.title_main_judge),
            modifier = Modifier.padding(top = Dimen.padding_16),
            style = MaterialTheme.typography.titleMedium
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimen.padding_12),
            horizontalArrangement = Arrangement.spacedBy(Dimen.padding_12),
        ) {
            items(mainJudges) {
                Column(verticalArrangement = Arrangement.spacedBy(Dimen.padding_4),
                    modifier = Modifier.clickable {
                        onUpdateMainJudge(it)
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
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.width(100.dp)
                    )
                }
            }
        }
        Text(
            string(id = R.string.title_judges),
            modifier = Modifier.padding(top = Dimen.padding_16),
            style = MaterialTheme.typography.titleMedium
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Dimen.padding_12),
            horizontalArrangement = Arrangement.spacedBy(Dimen.padding_12),
        ) {
            items(judges) {
                Column(verticalArrangement = Arrangement.spacedBy(Dimen.padding_4),
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
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.width(100.dp)
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