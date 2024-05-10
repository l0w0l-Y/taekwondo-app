package com.kaleksandra.featuremain.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kaleksandra.featuremain.presentation.model.EventModel
import com.kaleksandra.featuremain.presentation.model.FighterModel
import com.taekwondo.corecommon.ext.observe
import com.taekwondo.corenavigation.AuthDirection
import com.taekwondo.corenavigation.CreateEventDirection
import com.taekwondo.corenavigation.CreateFighterDirection
import com.taekwondo.corenavigation.ReadFighterDirection
import com.taekwondo.corenavigation.navigate
import com.taekwondo.coretheme.Dimen
import com.taekwondo.coreui.compose.string
import com.taekwondo.featuremain.R
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val fighters by viewModel.fighters.collectAsState()
    val event = viewModel.event.receiveAsFlow()
    event.observe {
        when (it) {
            MainViewModel.NavigateAuthState -> {
                navController.navigate(AuthDirection.path) {
                    popUpTo(AuthDirection.path) {
                        inclusive = true
                    }
                }
            }
        }
    }
    MainScreen(
        fighters = fighters,
        events = emptyList(),
        { navController.navigate(CreateFighterDirection) },
        { navController.navigate(CreateEventDirection) },
        { uid -> navController.navigate("${ReadFighterDirection.path}?uid=$uid") },
        viewModel::logOut
    )
}

@Composable
fun MainScreen(
    fighters: List<FighterModel>,
    events: List<EventModel>,
    createFighter: () -> Unit, createEvent: () -> Unit,
    updateFighter: (Int) -> Unit,
    logOut: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = Dimen.padding_12, start = Dimen.padding_16, end = Dimen.padding_16)
    ) {
        TextButton(onClick = logOut) {
            Text(
                text = string(id = R.string.button_log_out),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(Dimen.padding_2)) {
            Button(onClick = createFighter) {
                Text(
                    text = string(id = R.string.button_create_fighter),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Button(onClick = createEvent) {
                Text(
                    text = string(id = R.string.button_create_event),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(top = Dimen.padding_20)
        ) {
            Text(
                text = string(id = R.string.title_fighters, fighters.size),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(Dimen.padding_12),
                modifier = Modifier.padding(top = Dimen.padding_12)
            ) {
                items(fighters) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(Dimen.padding_4),
                        modifier = Modifier.clickable {
                            updateFighter(it.uid)
                        }) {
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
            Text(
                text = string(id = R.string.title_events, events.size),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = Dimen.padding_20)
            )
            LazyColumn(modifier = Modifier.padding(top = Dimen.padding_12)) {
                items(events) {
                    Text(text = it.name, modifier = Modifier.clickable {
                        updateFighter(it.uid)
                    })
                }
            }
        }
    }
}