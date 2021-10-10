package ru.beryukhov.afprojet.bb

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.tromian.game.afproject.R
import ru.beryukhov.afprojet.FILM
import ru.beryukhov.afprojet.Film
import ru.beryukhov.afprojet.aa.Stars

@Preview
@Preview(device = Devices.PIXEL_C)
@Composable
fun FilmItemPreview() = FilmItem(FILM)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilmItem(film: Film, isLiked: Boolean = false) {
    Card(
        onClick = { /*TODO*/ },
        backgroundColor = colorResource(R.color.background_card),
        shape = RoundedCornerShape(8.dp)
    ) {
        ConstraintLayout(Modifier.padding(4.dp)) {
            val (ivBackgroundPoster, ivMask, ageBg, tvAge, tvTitle, tvTag, tvReviewsCount, likedItem, ratingBar) = createRefs()
            Image(painter = painterResource(id = film.photo),
                contentScale = ContentScale.Crop,
                contentDescription = "",
                modifier = Modifier.constrainAs(ivBackgroundPoster) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(tvTitle.top, 8.dp)
                    width = Dimension.fillToConstraints
                }
            )
            Image(painter = painterResource(id = R.drawable.mask),
                contentScale = ContentScale.FillWidth,
                contentDescription = "",
                modifier = Modifier.constrainAs(ivMask) {
                    linkTo(
                        start = ivBackgroundPoster.start,
                        end = ivBackgroundPoster.end,
                        top = ivBackgroundPoster.top,
                        bottom = ivBackgroundPoster.bottom,
                        verticalBias = 1f
                    )
                    width = Dimension.fillToConstraints
                }
            )

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

            Image(painter = painterResource(id = if (isLiked) R.drawable.ic_heart_liked else R.drawable.ic_heart),
                contentDescription = "",
                modifier = Modifier.constrainAs(likedItem) {
                    top.linkTo(tvAge.top)
                    end.linkTo(parent.end, 8.dp)
                    bottom.linkTo(tvAge.bottom)
                }
            )

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