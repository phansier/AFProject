package ru.beryukhov.afprojet.film_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.tromian.game.afproject.R
import com.tromian.game.afproject.domain.MovieListType
import com.tromian.game.afproject.presentation.viewmodels.MoviesViewModel
import kotlinx.coroutines.flow.Flow
import ru.beryukhov.afprojet.Film

/*@Preview(device = Devices.PIXEL_4, backgroundColor = 0xff191926, showBackground = true)
@Preview(device = Devices.PIXEL_C, backgroundColor = 0xff191926, showBackground = true)
@Composable
fun MoviesPagePreview() =
    Scaffold(backgroundColor = colorResource(id = R.color.background)) {
        Column {
            MoviesPage(
                List(32) { FILM }
            )
        }
    }*/

@Composable
fun ColumnScope.MoviesPage(viewModel: MoviesViewModel, modifier: Modifier = Modifier, tempNavigationCallback: (Film) -> Unit = {}) {
    Column(modifier = modifier.weight(1f)) {
        ConstraintLayout(
            Modifier
                .padding(4.dp)
                .fillMaxWidth()
        ) {
            val (ivTitle, listTitle, ivListType, tvListType) = createRefs()
            Image(painter = painterResource(id = R.drawable.ic_locator),
                contentDescription = "",
                modifier = Modifier
                    .constrainAs(ivTitle) {
                        top.linkTo(parent.top, 24.dp)
                        start.linkTo(parent.start, 16.dp)
                    }
                    .width(16.dp)
                    .height(16.dp)
            )
            Text(
                text = stringResource(id = R.string.title_movies_list),
                color = colorResource(id = R.color.white_075),
                modifier = Modifier.constrainAs(listTitle) {
                    start.linkTo(ivTitle.end, 8.dp)
                    bottom.linkTo(ivTitle.bottom)
                    top.linkTo(ivTitle.top)
                    width = Dimension.wrapContent
                }
            )

            Image(painter = painterResource(id = R.drawable.ic_list_type_menu),
                contentDescription = "",
                modifier = Modifier
                    .constrainAs(ivListType) {
                        top.linkTo(ivTitle.top)
                        bottom.linkTo(ivTitle.bottom)
                        end.linkTo(parent.end, 16.dp)
                    }
                    .width(32.dp)
                    .height(32.dp)
            )
            Text(
                text = stringResource(id = R.string.tv_select_list),
                color = colorResource(id = R.color.white_075),
                modifier = Modifier.constrainAs(tvListType) {
                    bottom.linkTo(ivListType.bottom)
                    top.linkTo(ivListType.top)
                    linkTo(
                        start = listTitle.end,
                        startMargin = 24.dp,
                        end = ivListType.start,
                        endMargin = 8.dp,
                        bias = 1f
                    )
                    width = Dimension.wrapContent
                }
            )
        }
        MoviesGridVM(viewModel, tempNavigationCallback)

    }
}
@Composable
private fun MoviesGridClean(
    films: Flow<PagingData<Film>>,
    tempNavigationCallback: (Film) -> Unit
) {
    val filmListItems: LazyPagingItems<Film> = films.collectAsLazyPagingItems()

    LazyVerticalGrid(
        contentPadding = PaddingValues(4.dp),
        modifier = Modifier.fillMaxHeight(),
        columns = GridCells.Adaptive(minSize = 150.dp)
    ) {
        itemsIndexed(items = filmListItems,
            itemContent = { _, item ->
                item?.let {
                    FilmItem(
                        film = it,
                        onClick = { tempNavigationCallback(it) })
                }
            })

        filmListItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    //You can add modifier to manage load state when first time response page is loading
                }
                loadState.append is LoadState.Loading -> {
                    //You can add modifier to manage load state when next response page is loading
                }
                loadState.append is LoadState.Error -> {
                    //You can use modifier to show error message
                }
            }
        }
    }
}

@Composable
fun MoviesGridVM(viewModel: MoviesViewModel, tempNavigationCallback: (Film) -> Unit = {}) {
    MoviesGridClean(films = viewModel.loadFilmList(MovieListType.NOW_PLAYING), tempNavigationCallback = tempNavigationCallback)
}

