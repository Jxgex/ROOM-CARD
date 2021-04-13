package com.example.stores
import android.app.Application
import androidx.room.Room
import com.example.stores.common.DataBase.StoreDataBase

class StoreAplicacion: Application() {
    companion object{
        lateinit var dataBase: StoreDataBase
    }

    override fun onCreate() {
        super.onCreate()
        /*val MIGRATION_1_2 = object : Migration (1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE StoreEntity ADD COLUMN ImgUrl TEXT NOT NULL DEFAULT ''")
            }
        }*/
        dataBase = Room.
        databaseBuilder(this, StoreDataBase::class.java,"StoreDataBase")
                //.addMigrations(MIGRATION_1_2)
                .build()
    }
}