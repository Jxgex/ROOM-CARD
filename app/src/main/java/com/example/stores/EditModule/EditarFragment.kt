package com.example.stores.EditModule
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.stores.R
import com.example.stores.StoreAplicacion
import com.example.stores.common.Entity.StoreEntity
import com.example.stores.databinding.FragmentEditarBinding
import com.example.stores.mainModule.MainActivity
import com.google.android.material.textfield.TextInputLayout
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class EditarFragment : Fragment() {
    private lateinit var mBinding: FragmentEditarBinding
    private var mActivity: MainActivity? = null
    private var mIsEditMode: Boolean = false
    private var mStoreEntity: StoreEntity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = FragmentEditarBinding.inflate(inflater,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getLong("Id_Elemento", 0)
        if(id!= null && id!=0L){
            mIsEditMode = true
            getElemento(id)
        }
        else{
            mIsEditMode = false
            mStoreEntity = StoreEntity(Nombre = "", Phone = "", ImgURL = "")
        }
        ActionBarFun()
        Cambios_Validacion()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_guardado,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                Ocultar_Teclado()
                mActivity?.onBackPressed()
                true
            }

            R.id.action_guardar -> {
                if (mStoreEntity != null && Validar_Campos(mBinding.LayoutImgUrl,mBinding.LayoutDireccionWeb,
                                mBinding.LayoutTelefono,mBinding.LayoutNombre)) {
                    with(mStoreEntity!!) {
                        Nombre = mBinding.TxtNombreTienda.text.toString().trim()
                        Phone = mBinding.TxtNumero.text.toString().trim()
                        website = mBinding.TxtUrl.text.toString().trim()
                        ImgURL = mBinding.TxtUrlImg.text.toString().trim()
                    }
                    doAsync {
                        if(mIsEditMode) StoreAplicacion.dataBase.storeDao().IupdateStore(mStoreEntity!!)
                        else mStoreEntity!!.id = StoreAplicacion.dataBase.storeDao().IaddStore(mStoreEntity!!)
                        uiThread {
                            Ocultar_Teclado()
                            if(mIsEditMode){
                               mActivity?.Actualizar_Store(mStoreEntity!!)
                                Toast.makeText(activity, "Actualizado correctamente",Toast.LENGTH_LONG).show()
                            }
                            else{
                                mActivity?.Agregar_Store(mStoreEntity!!)
                                Toast.makeText(activity,"Insertado Correctamente",Toast.LENGTH_LONG).show()
                            }
                            mActivity?.onBackPressed()
                        }
                    }
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mActivity?.supportActionBar?.title = getString(R.string.app_name)
        setHasOptionsMenu(false)
        mActivity?.Ocultar_FABotom(true)
        super.onDestroy()
    }

    /**
     * Métodos Propios
     * */
    private fun Ocultar_Teclado(){
        val teclado = mActivity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if(view !=null){
            teclado.hideSoftInputFromWindow(view!!.windowToken, 0)
        }
    }

    private fun Cambios_Validacion() {
        with(mBinding){
            TxtNombreTienda.addTextChangedListener {
                Validar_Campos(LayoutNombre)
            }
            TxtNumero.addTextChangedListener {
                Validar_Campos(LayoutTelefono)
            }
            TxtUrl.addTextChangedListener{
                Validar_Campos(LayoutDireccionWeb)
            }
            TxtUrlImg.addTextChangedListener {
                Validar_Campos(LayoutImgUrl)
                LoadImg(it.toString().trim())
            }
        }
    }

    private fun Validar_Campos(vararg Campos:TextInputLayout):Boolean{
        var isValid =  true
        for(campos in Campos){
            if(campos.editText?.text.toString().trim().isEmpty()){
                campos.error = getString(R.string.Validacion)
                isValid = false
            }
            else{
                campos.error = null
            }
        }
        return isValid
    }

    private fun Validar_Campos():Boolean{
        var isValid = true
        if(mBinding.TxtUrlImg.text.toString().trim().isEmpty()){
            mBinding.LayoutImgUrl.error = getString(R.string.Validacion)
            mBinding.TxtUrlImg.error = getString(R.string.Validacion)
            isValid = false
        }
        if(mBinding.TxtUrl.text.toString().trim().isEmpty()){
            mBinding.LayoutDireccionWeb.error = getString(R.string.Validacion)
            mBinding.TxtUrl.requestFocus()
            isValid = false
        }
        if(mBinding.TxtNumero.text.toString().trim().isEmpty()){
            mBinding.LayoutTelefono.error = getString(R.string.Validacion)
            mBinding.TxtNumero.requestFocus()
            isValid = false
        }
        if(mBinding.TxtNombreTienda.text.toString().isEmpty()){
            mBinding.LayoutNombre.error = getString(R.string.Validacion)
            mBinding.TxtNombreTienda.requestFocus()
            isValid = false
        }
        if(mBinding.TxtUrlImg.text.toString().trim().isEmpty()
                && mBinding.TxtUrl.text.toString().trim().isEmpty()
                && mBinding.TxtNumero.text.toString().trim().isEmpty()
                && mBinding.TxtNombreTienda.text.toString().trim().isEmpty()){
            mBinding.LayoutNombre.error = getString(R.string.Validacion)
            mBinding.TxtNombreTienda.requestFocus()
            isValid = false
        }
        return isValid
    }

    private fun getElemento(id: Long) {
        doAsync {
            mStoreEntity = StoreAplicacion.dataBase.storeDao().IgetStoreId(id)
            uiThread {
                if (mStoreEntity!=null){
                    with(mBinding){
                        TxtNombreTienda.setText(mStoreEntity!!.Nombre)
                        TxtNumero.setText(mStoreEntity!!.Phone)
                        TxtUrl.setText(mStoreEntity!!.website)
                        TxtUrlImg.setText(mStoreEntity!!.ImgURL)
                    }
                }
            }
        }
    }

    private fun LoadImg(url:String){
        Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(mBinding.ImgUrl)
    }

    private fun ActionBarFun() {
        mActivity = activity as? MainActivity
        mActivity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if(mIsEditMode){
            mActivity?.supportActionBar?.title = getString(R.string.LUpdateTienda)
        }
        else{
            mActivity?.supportActionBar?.title = getString(R.string.LcrearTienda)
        }
        setHasOptionsMenu(true)
    }
}