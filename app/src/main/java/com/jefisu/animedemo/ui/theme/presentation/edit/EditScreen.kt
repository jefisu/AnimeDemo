package com.jefisu.animedemo.ui.theme.presentation.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.hilt.navigation.compose.hiltViewModel
import com.jefisu.animedemo.data.dto.model.Identify
import com.jefisu.animedemo.ui.theme.LocalSpacing
import com.jefisu.animedemo.ui.theme.presentation.edit.components.TransparentHintTextField
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collect

@Destination(navArgsDelegate = Identify::class)
@Composable
fun EditScreen(
    navigator: DestinationsNavigator,
    viewModel: EditViewModel = hiltViewModel()
) {
    val space = LocalSpacing.current
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is EditViewModel.UiEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is EditViewModel.UiEvent.AddUpdateAnime -> navigator.navigateUp()
            }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(EditEvent.SaveAnime)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Save"
                )
            }
        },
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(space.medium)
        ) {
            TransparentHintTextField(
                text = viewModel.name.text,
                hint = viewModel.name.hint,
                onValueChange = {
                    viewModel.onEvent(EditEvent.EnteredName(it))
                },
                onFocusChange = {
                    viewModel.onEvent(EditEvent.ChangeNameFocus(it))
                },
                isHintVisible = viewModel.name.isHintVisible,
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = MaterialTheme.typography.h5.fontSize,
                    letterSpacing = MaterialTheme.typography.h5.letterSpacing
                )
            )
        }
    }
}