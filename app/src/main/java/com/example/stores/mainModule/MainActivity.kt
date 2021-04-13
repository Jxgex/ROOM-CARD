package com.example.stores.mainModule

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.stores.EditModule.EditarFragment
import com.example.stores.R
import com.example.stores.StoreAplicacion
import com.example.stores.common.Entity.StoreEntity
import com.example.stores.common.mainaux
import com.example.stores.databinding.ActivityMainBinding
import com.example.stores.mainModule.Adapter.OnClickListener
import com.example.stores.mainModule.Adapter.StoreAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity(), OnClickListener, mainaux {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAdapter: StoreAdapter
    private lateinit var mGridLayout: GridLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.BtnNuevo.setOnClickListener {
            lauchEditFragment()
        }
        setupRecyclerView()
    }

    private fun lauchEditFragment(argumentos:Bundle? = null) {
        val fragment = EditarFragment()
        if(argumentos!=null){
            fragment.arguments = argumentos
        }
        val FManager = supportFragmentManager
        val FTransaction = FManager.beginTransaction()
        FTransaction.add(R.id.contenedor_padre,fragment)
        FTransaction.addToBackStack(null)
        FTransaction.commit()
        Ocultar_FABotom(false)
    }
    private fun getStores(){
        doAsync {
            val stores = StoreAplicacion.dataBase.storeDao().IgetAllStores()
            uiThread {
                mAdapter.setStores(stores)
            }
        }
    }
    private fun setupRecyclerView() {
        mAdapter = StoreAdapter(mutableListOf(),this)
        mGridLayout = GridLayoutManager(this,2)
        getStores()
        mBinding.RVItemStore.apply {
            setHasFixedSize(true)
            layoutManager = mGridLayout
            adapter = mAdapter
        }
    }
    /* Interfaz de OnClickListener */
    override fun OnClickListener(informacion: Long) {
        val argumento = Bundle()
        argumento.putLong("Id_Elemento",informacion)
        lauchEditFragment(argumento)
    }
    override fun OnFavoritePut(informacion: StoreEntity) {
        informacion.Favorita = !informacion.Favorita
        doAsync {
            StoreAplicacion.dataBase.storeDao().IupdateStore(informacion)
            uiThread {
                mAdapter.update(informacion)
            }
        }
    }
    override fun OnDeleteItem(informacion: StoreEntity) {
        val Items = arrayOf("Eliminar","Llamar","Ir al sitio")
        MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.acciones))
                .setItems(Items, { dialog, which ->
                    when (which) {
                        0 -> DeleteItemExtended(informacion)
                        1 -> IntentLlamar(informacion.Phone)
                        2 -> IntentNavegar(informacion.website)
                    }
                })
                .show()
    }
    private fun DeleteItemExtended(informacion: StoreEntity){
        MaterialAlertDialogBuilder(this)
                .setTitle(R.string.Dialog_Delete)
                .setPositiveButton(R.string.Dialog_Delete_Confirm, { dialog, which ->
                    doAsync {
                        StoreAplicacion.dataBase.storeDao().IdeleteStore(informacion)
                        uiThread {
                            mAdapter.delete(informacion)
                            Toast.makeText(application,"Eliminado correctamente", Toast.LENGTH_LONG).show()
                        }
                    }
                })
                .setNegativeButton(R.string.Dialog_Delete_Cancel,null)
                .show()
    }
    private fun IntentLlamar(Telefono:String){
        val Llamado = Intent().apply {
            action = Intent.ACTION_DIAL
            data = Uri.parse("tel: $Telefono")
        }
        if(Llamado.resolveActivity(packageManager)!=null) startActivity(Llamado)
        else Toast.makeText(this, "No se encontro ninguna app compatible", Toast.LENGTH_SHORT).show()
    }
    private fun IntentNavegar(Url:String){
        val IntentNavegar = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(Url)
        }
        if(IntentNavegar.resolveActivity(packageManager)!=null)startActivity(IntentNavegar)
        else Toast.makeText(this, "No se encontro ninguna app compatible", Toast.LENGTH_SHORT).show()
    }
    /* Método del interfaz auxiliar*/
    override fun Ocultar_FABotom(isVisible: Boolean) {
        if(isVisible) mBinding.BtnNuevo.show()
        else mBinding.BtnNuevo.hide()
    }
    override fun Agregar_Store(objeto: StoreEntity) {
        mAdapter.add(objeto)
    }
    override fun Actualizar_Store(objeto: StoreEntity) {
        mAdapter.update(objeto)
    }
}