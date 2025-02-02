package ru.beryukhov.afprojet.film_details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Dimension.Companion.fillToConstraints
import coil.compose.rememberImagePainter
import com.tromian.game.afproject.R
import kotlinx.collections.immutable.toPersistentList
import ru.beryukhov.afprojet.FILM
import ru.beryukhov.afprojet.Film


@Preview(device = Devices.PIXEL_C, backgroundColor = 0xff191926, showBackground = true)
@Preview(backgroundColor = 0xff191926, showBackground = true)
@Composable
fun FilmPagePreview() {
    Column {
        FilmPage(
            film = FILM,
            onClick = {}
        )
    }
}

@Composable
fun ColumnScope.FilmPage(
    film: Film,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    rememberScrollState(0)
    LazyColumn(
        modifier = modifier.weight(1f)
    ) {
        item {
            ConstraintLayout(Modifier) {
                val (imageView2, tvAge, tvTitle, tvTag, ivStars,
                    tvReviews, view, imageView7, tvBack, storyline, storylineText,
                    cast, list_actors) = createRefs()

                Box(modifier = Modifier.constrainAs(imageView2) {
                    height = Dimension.value(298.dp)
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                    Image(
                        painter = rememberImagePainter(
                            data = film.imageUrl,
                            builder = {
                                placeholder(R.drawable.film_placeholder)
                            }
                        ),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Image(
                        painter = painterResource(id = R.drawable.mask),
                        contentDescription = "",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    )
                }

                Text(text = film.age,
                    color = colorResource(R.color.white),
                    modifier = Modifier.constrainAs(tvAge) {
                        bottom.linkTo(tvTitle.top, margin = 12.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
                )
                Text(text = film.title,
                    color = colorResource(R.color.heading_white),
                    fontSize = 36.sp,
                    modifier = Modifier.constrainAs(tvTitle) {
                        linkTo(
                            start = parent.start, end = parent.end,
                            startMargin = 16.dp, endMargin = 16.dp, bias = 0f
                        )
                        top.linkTo(parent.top, margin = 252.dp)
                    }
                )
                Text(text = film.genres,
                    color = colorResource(R.color.categories),
                    modifier = Modifier.constrainAs(tvTag) {
                        linkTo(
                            start = parent.start, end = parent.end,
                            startMargin = 16.dp, endMargin = 16.dp, bias = 0f
                        )
                        top.linkTo(tvTitle.bottom, margin = 4.dp)
                    }
                )
                Stars(
                    rate = film.rate,
                    modifier = Modifier.constrainAs(ivStars) {
                        top.linkTo(tvTag.bottom, margin = 8.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
                )
                Text(
                    text = film.reviews,
                    color = colorResource(R.color.disabled_text),
                    modifier = Modifier.constrainAs(tvReviews) {
                        linkTo(
                            start = ivStars.end, end = parent.end,
                            startMargin = 4.dp, endMargin = 16.dp, bias = 0f
                        )
                        centerVerticallyTo(ivStars)
                    }
                )
                Box(modifier = Modifier
                    .constrainAs(view) {
                        linkTo(
                            start = parent.start, end = parent.end
                        )
                        top.linkTo(parent.top, margin = 44.dp)
                    }
                    .height(36.dp)
                )
                IconButton(
                    onClick = onClick,
                    modifier = Modifier.constrainAs(imageView7) {
                        linkTo(
                            top = view.top,
                            bottom = view.bottom
                        )
                        start.linkTo(parent.start, margin = 16.dp)
                    }
                        .testTag("FilmPageBack")
                ) {
                    Icon(painter = painterResource(id = R.drawable.back), contentDescription = "")
                }
                TextButton(
                    onClick = onClick,
                    modifier = Modifier.constrainAs(tvBack) {
                        linkTo(
                            top = view.top,
                            bottom = view.bottom
                        )
                        linkTo(
                            start = imageView7.end, end = parent.end,
                            startMargin = -12.dp, endMargin = 16.dp, bias = 0f
                        )
                    }
                ) {
                    Text(
                        text = stringResource(R.string.text_back),
                        color = colorResource(R.color.heading_white),
                        fontSize = 16.sp,
                    )
                }
                Text(text = stringResource(R.string.movie_storyline_header),
                    color = colorResource(R.color.heading_white),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.constrainAs(storyline) {
                        linkTo(
                            start = parent.start, end = parent.end,
                            startMargin = 16.dp, endMargin = 16.dp, bias = 0f
                        )
                        top.linkTo(tvReviews.bottom, margin = 24.dp)
                    }
                )
                Text(text = film.storyLine,
                    color = colorResource(R.color.heading_white),
                    fontSize = 14.sp,
                    modifier = Modifier.constrainAs(storylineText) {
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                        top.linkTo(storyline.bottom, margin = 4.dp)
                        width = fillToConstraints
                    }
                )
                Text(text = stringResource(R.string.movie_cast_header),
                    color = colorResource(R.color.heading_white),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.constrainAs(cast) {
                        linkTo(
                            start = parent.start, end = parent.end,
                            startMargin = 16.dp, endMargin = 16.dp, bias = 0f
                        )
                        top.linkTo(storylineText.bottom, margin = 20.dp)
                    }
                )
                Actors(modifier = Modifier.constrainAs(list_actors) {
                    linkTo(
                        start = parent.start, end = parent.end,
                        startMargin = 12.dp, endMargin = 12.dp, bias = 0f
                    )
                    top.linkTo(cast.bottom, margin = 6.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    width = fillToConstraints
                }, actors = film.actors.toPersistentList())
            }
        }
    }
}

