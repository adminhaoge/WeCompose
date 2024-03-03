package com.example.bughub.screens

import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.onLongClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Preview
@Composable
private fun PhotoGrid() {
    val photos by rememberSaveable {
        mutableStateOf(List(100) { Photo(it, randomSampleImageUrl()) })
    }
    val selectedIds = rememberSaveable {
        mutableStateOf(emptySet<Int>())
    }

    val insSelectionMode by remember {
        derivedStateOf {
            selectedIds.value.isNotEmpty()
        }
    }

    val autoScrollSped = remember {
        mutableFloatStateOf(0f)
    }

    val state = rememberLazyGridState()

    LaunchedEffect(autoScrollSped.floatValue) {
        if (autoScrollSped.floatValue != 0f) {
            while (isActive) {
                state.scrollBy(autoScrollSped.floatValue)
                delay(10)
            }
        }
    }

    LazyVerticalGrid(
        state = state,
        columns = GridCells.Adaptive(minSize = 128.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        modifier = Modifier.photoGridDrayGesturesHandler(
            state,
            selectedIds,
            autoScrollSped,
            with(LocalDensity.current) {40.dp.toPx()}
        )
    ) {
        items(photos, key = { it }) { photo ->
            val selected = selectedIds.value.contains(photo.id)

            ImageItem(photo, selected, insSelectionMode,
                Modifier
                    .semantics {
                        if (!insSelectionMode) {
                            onLongClick("select") {
                                selectedIds.value += photo.id
                                true
                            }
                        }
                    }
                    then(
                        if (insSelectionMode) {
                            Modifier.toggleable(value = selected, interactionSource = remember {
                                MutableInteractionSource()
                            }, indication = null, onValueChange = {
                                if (it) {
                                    selectedIds.value += photo.id
                                } else {
                                    selectedIds.value -= photo.id
                                }
                            })
                        } else Modifier
                    )
                    then(Modifier.clickable {
                        selectedIds.value = if (selected) {
                            selectedIds.value.minus(photo.id)
                        } else {
                            selectedIds.value.plus(photo.id)
                        }
                    })
            )

        }
    }
}


@Composable
private fun ImageItem(
    photo: Photo,
    selected: Boolean,
    insSelectionMode: Boolean,
    modifier: Modifier
) {
    Surface(
        elevation = 100.dp,
        contentColor = MaterialTheme.colors.primary,
        modifier = modifier.aspectRatio(1f)
    ) {
        //TODO 1
//        if (insSelectionMode) {
//            if (selected) {
//                Icon(Icons.Default.CheckCircle, null)
//            } else {
//                Icon(Icons.Default.Check, null)
//            }
//        }

        //TODO 2
        val transition = updateTransition(selected, label = "selected")
        
    }
}

private class Photo(val id: Int, val url: String)

fun randomSampleImageUrl() = "https://picsum.photos/seed/${(0..100000).random()}/256/256"