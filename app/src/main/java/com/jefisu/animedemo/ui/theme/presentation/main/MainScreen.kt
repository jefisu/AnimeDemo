package com.jefisu.animedemo.ui.theme.presentation.main

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jefisu.animedemo.ui.theme.LocalSpacing
import com.jefisu.animedemo.ui.theme.presentation.destinations.EditScreenDestination
import com.jefisu.animedemo.ui.theme.presentation.main.components.Item
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collect

@Destination(start = true)
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
                is MainViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is MainViewModel.UiEvent.DeleteAnime -> {
                    viewModel.onEvent(MainEvent.GetAnime)
                }
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = { navigator.navigate(EditScreenDestination()) }) {
                Icon(imageVector = Icons.Default.AddCircle, contentDescription = "")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(space.medium)
        ) {
            Text(
                text = "Anilist",
                fontSize = 48.sp,
                letterSpacing = 2.sp,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(space.medium))
            LazyColumn {
                items(state.animes) { anime ->
                    Item(
                        name = anime.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(space.medium))
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = RoundedCornerShape(space.medium)
                            )
                            .clickable {
                                navigator.navigate(
                                    EditScreenDestination(id = anime.id)
                                )
                            },
                        onDeleteClick = {
                            viewModel.onEvent(MainEvent.DeleteAnime(anime))
                        }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}