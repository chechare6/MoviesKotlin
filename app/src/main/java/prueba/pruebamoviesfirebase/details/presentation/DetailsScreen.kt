package prueba.pruebamoviesfirebase.details.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import prueba.pruebamoviesfirebase.R
import prueba.pruebamoviesfirebase.login.utils.AuthManager
import prueba.pruebamoviesfirebase.movieList.data.remote.MovieApi
import prueba.pruebamoviesfirebase.movieList.util.RatingBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(fromPopular: Boolean) {
    //-------
    var cmn by rememberSaveable {mutableStateOf("")}
    var comentarios by rememberSaveable { mutableStateOf(listOf<String>()) }
    val auth = AuthManager(LocalContext.current)
    val user = auth.getCurrentUser()
    //--------


    val detailsViewModel = hiltViewModel<DetailsViewModel>()
    val detailsState = detailsViewModel.detailsState.collectAsState().value

    val backDropImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApi.IMAGE_BASE_URL + detailsState.movie?.backdrop_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    val posterImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApi.IMAGE_BASE_URL + detailsState.movie?.poster_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        if (backDropImageState is AsyncImagePainter.State.Error) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(70.dp),
                    imageVector = Icons.Rounded.ImageNotSupported,
                    contentDescription = detailsState.movie?.title
                )
            }
        }

        if (backDropImageState is AsyncImagePainter.State.Success) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                painter = backDropImageState.painter,
                contentDescription = detailsState.movie?.title,
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(240.dp)
            ) {
                if (posterImageState is AsyncImagePainter.State.Error) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(70.dp),
                            imageVector = Icons.Rounded.ImageNotSupported,
                            contentDescription = detailsState.movie?.title
                        )
                    }
                }

                if (posterImageState is AsyncImagePainter.State.Success) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp)),
                        painter = posterImageState.painter,
                        contentDescription = detailsState.movie?.title,
                        contentScale = ContentScale.Crop
                    )
                }
            }

            detailsState.movie?.let { movie ->
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = movie.title,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp)
                    ) {
                        RatingBar(
                            starsModifier = Modifier.size(18.dp),
                            rating = movie.vote_average / 2
                        )

                        Text(
                            modifier = Modifier.padding(start = 4.dp),
                            text = movie.vote_average.toString().take(3),
                            color = Color.LightGray,
                            fontSize = 14.sp,
                            maxLines = 1,
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(R.string.language) + movie.original_language
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = stringResource(R.string.release_date) + movie.release_date
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = movie.vote_count.toString() + stringResource(R.string.votes)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Icon(
                        imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = "Fav Icon",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable {
                                /* TODO AQUI HAY QUE HACER LA FUNCION */
                            }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(R.string.overview),
            fontSize = 19.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        detailsState.movie?.let {
            Text(
                modifier = Modifier.padding(start = 16.dp),
                text = it.overview,
                fontSize = 16.sp,
            )
        }
        Spacer(modifier = Modifier.height(32.dp))

        // Mostrar comentarios
        comentarios.forEach { comentario ->
            val cadena = "${user?.email} ha dicho: $comentario"
            Text(text = cadena)
            Spacer(modifier = Modifier.height(4.dp))
        }

        if(!fromPopular){
            Text(
                modifier = Modifier.padding(10.dp),
                text = "Cines cercanos donde ver: \n${detailsState.movie?.title}",
                fontSize = 16.sp
            )
            Card(
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.inverseOnSurface),
                modifier = Modifier.height(400.dp)
            ){
                Map()
            }
        } else {
            Card(
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = "Deja tu comentario",
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    TextField(
                        value = cmn,
                        onValueChange = {
                            cmn = it
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    //Agregamos el boton
                    Button(
                        onClick = {
                            if (cmn.isNotBlank()) {
                                comentarios = listOf(cmn) + comentarios
                                cmn = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(text = "Publicar")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun Map(){
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        properties = MapProperties(isMyLocationEnabled = true)
    )
}


