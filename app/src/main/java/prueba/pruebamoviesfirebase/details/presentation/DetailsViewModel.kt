package prueba.pruebamoviesfirebase.details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import prueba.pruebamoviesfirebase.movieList.domain.repository.MovieListRepository
import prueba.pruebamoviesfirebase.movieList.util.Resource
import javax.inject.Inject

/**
 * ViewModel encargado de gestionar la lógica de presentación y el estado relacionado con los detalles de una película.
 *
 * @property movieListRepository Repositorio que proporciona acceso a los datos relacionados con las películas.
 * @property savedStateHandle Maneja y proporciona acceso al estado guardado entre configuraciones (por ejemplo, rotaciones de pantalla).
 */
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    /**
     * Identificador de la película actualmente mostrada en los detalles.
     */
    private val movieId = savedStateHandle.get<Int>("movieId")

    /**
     * Un [MutableStateFlow] que representa el estado de los detalles de la película.
     */
    private var _detailsState = MutableStateFlow(DetailsState())
    val detailsState = _detailsState.asStateFlow()

    /**
     * Inicializa el ViewModel obteniendo los detalles de la película actual.
     */
    init {
        getMovie(movieId ?: -1)
    }

    /**
     * Obtiene los detalles de una película específica del repositorio.
     *
     * @param id El identificador de la película.
     */
    private fun getMovie(id: Int) {
        viewModelScope.launch {
            _detailsState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovie(id).collectLatest { result ->
                when (result) {
                    is Resource.Error -> {
                        _detailsState.update {
                            it.copy(isLoading = false)
                        }
                    }

                    is Resource.Loading -> {
                        _detailsState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }

                    is Resource.Success -> {
                        result.data?.let { movie ->
                            _detailsState.update {
                                it.copy(movie = movie)
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}