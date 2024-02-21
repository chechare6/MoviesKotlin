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

/**
 * ViewModel para gestionar el estado y la lógica relacionada con la lista de películas.
 *
 * @param movieListRepository Repositorio para acceder a los datos de la lista de películas.
 */
@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository,
    /* FAVORITO
    private val movieDao: MovieDao */
) : ViewModel() {

    // Estado mutable de la lista de películas
    private var _movieListState = MutableStateFlow(MovieListState())
    // Estado inmutable de la lista de películas expuesto a la interfaz de usuario
    val movieListState = _movieListState.asStateFlow()

    // Inicialización, se obtienen las listas de películas populares y en reproducción actual
    init {
        getPopularMovieList(false)
        getnowPlayingMovieList(false)
    }

    /**
     * Método para manejar eventos relacionados con la lista de películas.
     *
     * @param event El evento que se está manejando.
     */
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

    /**
     * Método para obtener la lista de películas populares.
     *
     * @param forceFetchFromRemote Indica si se debe forzar la obtención de datos de forma remota.
     */
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

    /**
     * Método para obtener la lista de películas en reproducción actual.
     *
     * @param forceFetchFromRemote Indica si se debe forzar la obtención de datos de forma remota.
     */
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
    // Métodos para agregar y quitar películas de favoritos
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