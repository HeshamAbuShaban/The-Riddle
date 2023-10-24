package dev.training.the_riddle.di

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.training.the_riddle.R
import dev.training.the_riddle.app_system.constants.AppConstants
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGlide(
        @ApplicationContext context: Context,
    ): RequestManager = Glide.with(context)
        .applyDefaultRequestOptions(
            RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_foreground)
        )


    @Singleton
    @Provides
    fun provideSharedPreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences =
        context.getSharedPreferences(
            AppConstants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE
        )

    /*@Singleton
    @Provides
    fun provideFragmentsList(): ArrayList<Fragment> = arrayListOf(

    )*/

}