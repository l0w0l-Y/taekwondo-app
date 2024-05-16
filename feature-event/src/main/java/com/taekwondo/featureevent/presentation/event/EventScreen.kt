package com.taekwondo.featureevent.presentation.event

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.taekwondo.corenavigation.UpdateEventFighterDirection
import com.taekwondo.coretheme.Dimen
import com.taekwondo.coreui.compose.string
import com.taekwondo.featureevent.R
import com.taekwondo.featureevent.presentation.model.EventModel

@Composable
fun EventScreen(navController: NavController, viewModel: EventViewModel = hiltViewModel()) {
    val event by viewModel.event.collectAsState()
    EventScreen(event, onUpdate = { navController.navigate(UpdateEventFighterDirection.path) })
}

@Composable
fun EventScreen(eventModel: EventModel?, onUpdate: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = Dimen.padding_16)
    ) {
        eventModel?.let {
            Text(text = it.name, style = MaterialTheme.typography.titleMedium)
            Text(text = it.date, style = MaterialTheme.typography.titleMedium)
            Text(text = it.place, style = MaterialTheme.typography.titleMedium)
            Row {
                Text(string(R.string.title_judges))
                Button(onClick = onUpdate) {
                    Text(text = string(R.string.button_update))
                }
            }
            LazyRow {
                items(it.users) {
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
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            Row {
                Text(text = string(R.string.title_fighters))
                Button(onClick = onUpdate) {
                    Text(text = string(R.string.button_update))
                }
            }
            LazyRow {
                items(it.fighters) {
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
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}