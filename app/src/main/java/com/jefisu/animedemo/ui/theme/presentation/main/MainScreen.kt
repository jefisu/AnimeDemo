package com.jefisu.animedemo.ui.theme.presentation.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jefisu.animedemo.data.util.UiEvent
import com.jefisu.animedemo.ui.theme.LocalSpacing
import com.jefisu.animedemo.ui.theme.presentation.destinations.EditScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collect

@Destination(
    start = true
)
@Composable
fun MainScreen(
    navigator: DestinationsNavigator,
    viewModel: MainViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val state = viewModel.state.value
    val space = LocalSpacing.current

    LaunchedEffect(key1 = state.animes) {
        viewModel.onEvent(MainEvent.GetAnime)
    }
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is UiEvent.AddedDeletedAnime -> {
                    viewModel.onEvent(MainEvent.GetAnime)
                }
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .padding(space.small)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(state.animes) { anime ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(space.medium)
                            .clickable {
                                navigator.navigate(
                                    EditScreenDestination(
                                        name = anime.name,
                                        id = anime.id
                                    )
                                )
                            }
                    ) {
                        Text(text = anime.name, style = MaterialTheme.typography.h4)
                        IconButton(onClick = {
                            viewModel.onEvent(MainEvent.DeleteAnime(anime))
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                modifier = Modifier.size(35.dp)
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(space.small),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    value = viewModel.name,
                    onValueChange = { viewModel.onEvent(MainEvent.ChangeValueName(it)) },
                    placeholder = { Text(text = "Write name here...") },
                    shape = RoundedCornerShape(space.medium),
                    singleLine = true
                )
                IconButton(onClick = { viewModel.onEvent(MainEvent.SaveAnime) }) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        }
    }
}