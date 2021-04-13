package com.example.stores.common.DataBase
import androidx.room.*
import com.example.stores.common.Entity.StoreEntity

@Dao
interface StoreDAO{
    @Query("SELECT * FROM StoreEntity")
    fun IgetAllStores():MutableList<StoreEntity>
    @Insert
    fun IaddStore(storeEntity: StoreEntity): Long
    @Update
    fun IupdateStore(storeEntity: StoreEntity)
    @Delete
    fun IdeleteStore(storeEntity: StoreEntity)
    @Query("SELECT * FROM StoreEntity WHERE id = :id")
    fun IgetStoreId(id:Long): StoreEntity
}