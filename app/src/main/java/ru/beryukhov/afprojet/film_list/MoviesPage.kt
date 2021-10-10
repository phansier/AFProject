package ru.beryukhov.afprojet.film_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tromian.game.afproject.R
import ru.beryukhov.afprojet.FILM
import ru.beryukhov.afprojet.Film

@Preview(backgroundColor = 0xff191926, showBackground = true)
@Preview(device = Devices.PIXEL_C, backgroundColor = 0xff191926, showBackground = true)
@Composable
fun MoviesPagePreview() =
    Scaffold(backgroundColor = colorResource(id = R.color.background)) {
        Column {
            MoviesPage(
                List(32) { FILM }
            )
        }
    }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColumnScope.MoviesPage(films: List<Film>) {
    Column(modifier = Modifier.weight(1f)) {
        ConstraintLayout(
            Modifier
                .padding(4.dp)
                .fillMaxWidth()) {
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
        LazyVerticalGrid(
            contentPadding = PaddingValues(4.dp),
            modifier = Modifier.fillMaxHeight(),
            cells = GridCells.Adaptive(minSize = 500.dp)
        ) {
            itemsIndexed(items = films,
                itemContent = { index, item ->
                    FilmItem(film = item.copy(age = index.toString() ))
                })
        }

    }
}