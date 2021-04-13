package com.example.stores.common

import com.example.stores.common.Entity.StoreEntity

/*
* Nombre: mainaux.kt
* Fecha: 11/04/2021
* Autor: Jorge Eduardo Martínez Mohedano
* Version: 1.0
* Descripción: Interfaz de comunicación de eventos entre objetos de la clase main y la interfaz.
*/
interface mainaux {
    fun Ocultar_FABotom(isVisible : Boolean = true)
    fun Agregar_Store(objeto: StoreEntity)
    fun Actualizar_Store(objeto: StoreEntity)
}