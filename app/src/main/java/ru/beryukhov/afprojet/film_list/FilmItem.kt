package ru.beryukhov.afprojet.film_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberImagePainter
import com.tromian.game.afproject.R
import kotlin.random.Random
import ru.beryukhov.afprojet.FILM
import ru.beryukhov.afprojet.Film
import ru.beryukhov.afprojet.film_details.Stars

@Preview
@Preview(device = Devices.PIXEL_C)
@Composable
fun FilmItemPreview() = FilmItem(FILM, onClick = {})

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilmItem(film: Film, modifier: Modifier = Modifier, isLiked: Boolean = randomBoolean(), onClick: () -> Unit) {
    var liked: Boolean by remember { mutableStateOf(isLiked) }
    Card(
        onClick = onClick,
        backgroundColor = colorResource(R.color.background_card),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        ConstraintLayout(Modifier.padding(4.dp)) {
            val (ivBackgroundPoster, ageBg, tvAge, tvTitle, tvTag, tvReviewsCount, likedItem, ratingBar) = createRefs()

            Box(modifier = Modifier.constrainAs(ivBackgroundPoster) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(tvTitle.top, 8.dp)
                height = Dimension.value(200.dp)
            }) {
                Image(
                    painter = rememberImagePainter(
                        data = film.imageUrl,
                        builder = {
                            placeholder(R.drawable.film_placeholder)
                        }
                    ),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )
                Image(
                    painter = painterResource(id = R.drawable.mask),
                    contentScale = ContentScale.FillWidth,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(BottomCenter)
                )
            }

            Image(painter = painterResource(id = R.drawable.ic_rectangle),
                contentDescription = "",
                modifier = Modifier
                    .constrainAs(ageBg) {
                        top.linkTo(parent.top, 8.dp)
                        start.linkTo(parent.start, 8.dp)
                    }
                    .width(24.dp)
                    .height(24.dp)
            )
            Text(
                text = film.age,
                color = colorResource(id = R.color.white),
                modifier = Modifier.constrainAs(tvAge) {
                    start.linkTo(ageBg.start)
                    end.linkTo(ageBg.end)
                    bottom.linkTo(ageBg.bottom)
                    top.linkTo(ageBg.top)
                    width = Dimension.fillToConstraints
                },
                textAlign = TextAlign.Center,
                fontSize = 12.sp
            )
            Text(
                text = film.title,
                color = colorResource(id = R.color.heading_white),
                modifier = Modifier.constrainAs(tvTitle) {
                    linkTo(
                        start = parent.start,
                        end = parent.end,
                        bias = 0f,
                        startMargin = 8.dp,
                        endMargin = 8.dp
                    )
                    bottom.linkTo(parent.bottom, 8.dp)

                    width = Dimension.wrapContent
                },
                fontSize = 14.sp
            )

            Text(
                text = film.genres,
                color = colorResource(id = R.color.categories),
                modifier = Modifier.constrainAs(tvTag) {
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                    bottom.linkTo(ivBackgroundPoster.bottom, 24.dp)
                    width = Dimension.fillToConstraints
                },
                fontSize = 11.sp
            )

            Text(
                text = film.reviews,
                color = colorResource(id = R.color.white_050),
                modifier = Modifier.constrainAs(tvReviewsCount) {
                    start.linkTo(ratingBar.end, 8.dp)
                    top.linkTo(ratingBar.top)
                    bottom.linkTo(ratingBar.bottom)
                    width = Dimension.fillToConstraints
                },
                fontSize = 11.sp
            )
            IconButton(
                onClick = { liked = !liked },
                modifier = Modifier.constrainAs(likedItem) {
                    top.linkTo(tvAge.top)
                    end.linkTo(parent.end, 8.dp)
                    bottom.linkTo(tvAge.bottom)
                }
            ) {
                Image(
                    painter = painterResource(id = if (liked) R.drawable.ic_heart_liked else R.drawable.ic_heart),
                    contentDescription = "",
                )
            }

            Stars(
                rate = film.rate,
                modifier = Modifier
                    .constrainAs(ratingBar) {
                        top.linkTo(tvTag.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 8.dp)
                    }
                    .width(52.dp)
                    .height(11.dp)
            )
        }
    }
}

private fun randomBoolean(): Boolean {
    return Random.nextInt() % 4 == 0
}
