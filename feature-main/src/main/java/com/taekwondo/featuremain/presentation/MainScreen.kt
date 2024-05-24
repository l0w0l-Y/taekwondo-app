package com.taekwondo.featuremain.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.taekwondo.corecommon.ext.observe
import com.taekwondo.corenavigation.AuthDirection
import com.taekwondo.corenavigation.CreateEventDirection
import com.taekwondo.corenavigation.CreateFighterDirection
import com.taekwondo.corenavigation.ReadEventDirection
import com.taekwondo.corenavigation.ReadFighterDirection
import com.taekwondo.corenavigation.navigate
import com.taekwondo.coretheme.Dimen
import com.taekwondo.coreui.compose.string
import com.taekwondo.featuremain.R
import com.taekwondo.featuremain.presentation.model.EventModel
import com.taekwondo.featuremain.presentation.model.FighterModel
import kotlinx.coroutines.flow.receiveAsFlow
import com.taekwondo.coreui.R as CoreUiR

@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = hiltViewModel()) {
    val fighters by viewModel.fighters.collectAsState()
    val events by viewModel.events.collectAsState()
    val archive by viewModel.archive.collectAsState()
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
        events = events,
        archive = archive,
        { navController.navigate(CreateFighterDirection) },
        { navController.navigate(CreateEventDirection) },
        { uid -> navController.navigate("${ReadFighterDirection.path}?uid=$uid") },
        viewModel::logOut,
        { uid -> navController.navigate("${ReadEventDirection.path}?uid=$uid") }
    )
}

@Composable
fun MainScreen(
    fighters: List<FighterModel>,
    events: List<EventModel>,
    archive: List<EventModel>,
    createFighter: () -> Unit,
    createEvent: () -> Unit,
    updateFighter: (Long) -> Unit,
    logOut: () -> Unit,
    onOpenEvent: (Long) -> Unit,
) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf(
        string(id = R.string.title_events, events.size),
        string(id = R.string.title_archive, archive.size)
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = Dimen.padding_12, horizontal = Dimen.padding_16)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            TextButton(onClick = logOut, modifier = Modifier.align(Alignment.TopEnd)) {
                Text(
                    text = string(id = R.string.button_log_out),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
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
                                    painter = painterResource(id = CoreUiR.drawable.ic_placeholder),
                                    contentDescription = null,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.widthIn(max = 100.dp)
                        )
                    }
                }
            }
            Column(modifier = Modifier.fillMaxWidth()) {
                TabRow(
                    selectedTabIndex = tabIndex,
                    containerColor = Color.Transparent,
                    divider = {}
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(text = { Text(title) },
                            selected = tabIndex == index,
                            onClick = { tabIndex = index }
                        )
                    }
                }
                when (tabIndex) {
                    0 -> {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = Dimen.padding_12),
                            verticalArrangement = Arrangement.spacedBy(Dimen.padding_4),
                        ) {
                            items(events) {
                                Text(
                                    text = it.name,
                                    modifier = Modifier
                                        .background(
                                            MaterialTheme.colorScheme.surface,
                                            RoundedCornerShape(Dimen.radius_8)
                                        )
                                        .clip(RoundedCornerShape(Dimen.radius_8))
                                        .clickable {
                                            onOpenEvent(it.uid)
                                        }
                                        .padding(Dimen.padding_12)
                                        .fillMaxWidth()
                                )
                            }
                        }
                    }

                    1 -> {
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = Dimen.padding_12),
                            verticalArrangement = Arrangement.spacedBy(Dimen.padding_4),
                        ) {
                            items(archive) {
                                Text(
                                    text = it.name,
                                    modifier = Modifier
                                        .background(
                                            MaterialTheme.colorScheme.surface,
                                            RoundedCornerShape(Dimen.radius_8)
                                        )
                                        .clip(RoundedCornerShape(Dimen.radius_8))
                                        .clickable {
                                            onOpenEvent(it.uid)
                                        }
                                        .padding(Dimen.padding_12)
                                        .fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}