package prueba.pruebamoviesfirebase.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import prueba.pruebamoviesfirebase.details.presentation.comments.dao.CommentDao
import prueba.pruebamoviesfirebase.details.presentation.comments.dao.CommentDatabase
import prueba.pruebamoviesfirebase.movieList.data.local.movie.MovieDatabase
import prueba.pruebamoviesfirebase.movieList.data.remote.MovieApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Interceptor para realizar logging de las solicitudes HTTP.
    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Cliente OkHttpClient con el interceptor añadido.
    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    // Método para proveer una instancia de MovieApi utilizando Retrofit.
    @Provides
    @Singleton
    fun providesMovieApi() : MovieApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(MovieApi.BASE_URL)
            .client(client)
            .build()
            .create(MovieApi::class.java)
    }

    // Método para proveer una instancia de MovieDatabase utilizando Room.
    @Provides
    @Singleton
    fun providesMovieDatabase(app: Application): MovieDatabase {
        return Room.databaseBuilder(
            app,
            MovieDatabase::class.java,
            "moviedb.db"
        ).build()
    }

    // Método para proveer una instancia de CommentDao utilizando Room.
    @Provides
    @Singleton
    fun provideCommentDao(database: CommentDatabase): CommentDao {
        return database.dao
    }

    // Método para proveer una instancia de CommentDatabase utilizando Room.
    @Provides
    @Singleton
    fun providesCommentDatabase(app: Application): CommentDatabase {
        return Room.databaseBuilder(
            app,
            CommentDatabase::class.java,
            "comments.db"
        ).build()
    }
}