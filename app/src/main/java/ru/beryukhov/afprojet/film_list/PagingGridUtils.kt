package ru.beryukhov.afprojet.film_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems

@OptIn(ExperimentalFoundationApi::class)
fun <T : Any> LazyGridScope.itemsIndexed(
    items: LazyPagingItems<T>,
    itemContent: @Composable LazyGridItemScope.(index: Int, value: T?) -> Unit
) {
    items(
        count = items.itemCount,
    ) { index ->
        itemContent(index, items[index])
    }
}