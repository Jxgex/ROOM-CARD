package com.example.stores.common.DataBase
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.stores.common.Entity.StoreEntity

@Database(entities = arrayOf(StoreEntity::class), version = 1)
abstract class StoreDataBase: RoomDatabase() {
    abstract fun storeDao(): StoreDAO
}