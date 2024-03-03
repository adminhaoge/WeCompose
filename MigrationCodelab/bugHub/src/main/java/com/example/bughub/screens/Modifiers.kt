package com.example.bughub.screens

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.toIntRect

fun Modifier.photoGridDrayGesturesHandler(
    lazyGridState: LazyGridState,
    selectedIds: MutableState<Set<Int>>,
    autoScrollSped: MutableState<Float>,
    autoScrollThreshold: Float
) = pointerInput(Unit) {
    var initialKey: Int? = null
    var currentKey: Int? = null
    detectDragGesturesAfterLongPress(
        onDragStart = { offset ->
            lazyGridState.gridItemKeyAtPosition(offset)?.let { key ->
                if (!selectedIds.value.contains(key)) {
                    initialKey = key
                    currentKey = key
                    selectedIds.value.plus(key) // #3
                }
            }
        },
        onDragCancel = { initialKey = null; autoScrollSped.value = 0f },
        onDragEnd = { initialKey = null; autoScrollSped.value = 0f },
        onDrag = { change, _ ->
            if (initialKey != null) {
                //TODO 1
                // Add or remove photos from selection based on drag position
                lazyGridState.gridItemKeyAtPosition(change.position)?.let { key ->
                    if (currentKey != key) {
                        selectedIds.value = selectedIds.value
                            .minus(initialKey!!..currentKey!!)
                            .minus(currentKey!!..initialKey!!)
                            .plus(initialKey!!..key)
                            .plus(key..initialKey!!)
                        currentKey = key
                    }
                }

                //TODO 2
                val distFromBottom =
                lazyGridState.layoutInfo.viewportSize.height - change.position.y
                val disFromTop = change.position.y
                //获得到区块(阈值)
                autoScrollSped.value = when { //when关键字作用
                    distFromBottom < autoScrollThreshold -> autoScrollThreshold - distFromBottom
                    disFromTop < autoScrollThreshold -> -(autoScrollThreshold - disFromTop)
                    else -> 0f
                }
            }
        }
    )
}

fun LazyGridState.gridItemKeyAtPosition(hitPoint: Offset): Int? =
    layoutInfo.visibleItemsInfo.find {
        it.size.toIntRect().contains(hitPoint.round() - it.offset)
    }?.key as? Int