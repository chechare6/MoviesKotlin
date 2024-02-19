package prueba.pruebamoviesfirebase.movieList.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import prueba.pruebamoviesfirebase.movieList.domain.repository.MovieListRepository
import prueba.pruebamoviesfirebase.movieList.util.Category
import prueba.pruebamoviesfirebase.movieList.util.Resource
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository,
    /* FAVORITO
    private val movieDao: MovieDao */
) : ViewModel() {

    //view model y parametros que utilizamos
    private var _movieListState = MutableStateFlow(MovieListState())
    val movieListState = _movieListState.asStateFlow()

    init {
        getPopularMovieList(false)
        getnowPlayingMovieList(false)
    }

    fun onEvent(event: MovieListUiEvent) {
        when (event) {
            is MovieListUiEvent.Paginate -> {
                if (event.category == Category.POPULAR) {
                    getPopularMovieList(true)
                } else if (event.category == Category.now_playing) {
                    getnowPlayingMovieList(true)
                }
            }

            is MovieListUiEvent.Navigate -> {
                _movieListState.update {
                    it.copy(isScreen = event.page)
                }
            }
        }
    }

    //GET de las peliculas populares - comprueba si tenemos que coger la info de la API o podemos desde la BBDD interna (room)
    private fun getPopularMovieList(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovieList(
                forceFetchFromRemote,
                Category.POPULAR,
                movieListState.value.popularMovieListPage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { popularList ->
                            _movieListState.update { movieList ->
                                movieList.copy(
                                    popularMovieList = movieListState.value.popularMovieList
                                            + popularList.sortedByDescending { it.vote_average }, //ESTO ORDENA SEGÚN EL PARÁMETRO QUE QUERAMOS
                                    popularMovieListPage = movieListState.value.popularMovieListPage + 1
                                )
                            }
                        }
                    }

                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                }
            }
        }
    }

    //GET de las próximas peliculas  - comprueba si tenemos que coger la info de la API o podemos desde la BBDD interna (room)
    private fun getnowPlayingMovieList(forceFetchFromRemote: Boolean) {
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovieList(
                forceFetchFromRemote,
                Category.NOW_PLAYING,
                movieListState.value.nowPlayingMovieListPage
            ).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { now_playingList ->
                            _movieListState.update { movieList ->
                                movieList.copy(
                                    nowPlayingMovieList = movieListState.value.nowPlayingMovieList
                                            + now_playingList.sortedByDescending{ it.release_date }, //ESTO ORDENA SEGÚN EL PARÁMETRO QUE QUERAMOS
                                    nowPlayingMovieListPage = movieListState.value.nowPlayingMovieListPage + 1
                                )
                            }
                        }
                    }

                    is Resource.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                }
            }
        }
    }

    /* FAVORITO
    fun addToFavorite(movie: MovieEntity) {
        viewModelScope.launch {
            movieDao.insertFavoriteMovie(movie)
        }
    }

    // FAVORITO
    fun removeFromFavorite(movie: MovieEntity) {
        viewModelScope.launch {
            movieDao.deleteFavoriteMovie(movie)
        }
    } */

}