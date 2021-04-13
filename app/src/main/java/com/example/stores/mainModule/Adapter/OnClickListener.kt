package com.example.stores.mainModule.Adapter

import com.example.stores.common.Entity.StoreEntity

interface OnClickListener {
    fun OnClickListener(informacion_id:Long)
    fun OnFavoritePut(informacion: StoreEntity)
    fun OnDeleteItem(informacion: StoreEntity)
}