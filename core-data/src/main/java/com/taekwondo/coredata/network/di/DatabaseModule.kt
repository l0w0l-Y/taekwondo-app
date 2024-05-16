package com.taekwondo.coredata.network.di

import android.content.Context
import androidx.room.Room
import com.taekwondo.coredata.network.dao.AuthDao
import com.taekwondo.coredata.network.dao.EventDao
import com.taekwondo.coredata.network.dao.EventParticipantsDao
import com.taekwondo.coredata.network.dao.FighterDao
import com.taekwondo.coredata.network.database.DataStoreProvider
import com.taekwondo.coredata.network.database.Database
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): DataStoreProvider = DataStoreProvider(context, dispatcher)

    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): Database {
        return Room.databaseBuilder(
            context,
            Database::class.java,
            "taekwondo_database"
        ).build()
    }

    @Provides
    fun provideAuthDao(database: Database): AuthDao {
        return database.authDao()
    }

    @Provides
    fun provideFighterDao(database: Database): FighterDao {
        return database.fighterDao()
    }

    @Provides
    fun provideEventDao(database: Database): EventDao {
        return database.eventDao()
    }

    @Provides
    fun provideEventParticipantsDao(database: Database): EventParticipantsDao {
        return database.eventParticipantsDao()
    }
}