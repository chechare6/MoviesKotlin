package prueba.pruebamoviesfirebase.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import prueba.pruebamoviesfirebase.movieList.data.repository.MovieListRepositoryImpl
import prueba.pruebamoviesfirebase.movieList.domain.repository.MovieListRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // Método abstracto que vincula la implementación de MovieListRepository a su interfaz.
    @Binds
    @Singleton
    abstract fun bindMovieListRepository(
        movieListRepositoryImpl: MovieListRepositoryImpl
    ): MovieListRepository

}