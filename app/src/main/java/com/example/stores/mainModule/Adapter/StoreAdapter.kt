package com.example.stores.mainModule.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.stores.R
import com.example.stores.common.Entity.StoreEntity
import com.example.stores.databinding.ItemStoreBinding

class StoreAdapter(private var store:MutableList<StoreEntity>, private var listener: OnClickListener):
        RecyclerView.Adapter<StoreAdapter.ViewHolder>() {
    private lateinit var mContext:Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_store,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var start = store.get(position)
        with(holder){
            Glide.with(mContext)
                .load(start.ImgURL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.imgFoto)
            binding.NombreTienda.text = start.Nombre
            setOnclickListener(start)
            binding.CheckFav.isChecked = start.Favorita
        }
    }

    override fun getItemCount(): Int = store.size

    fun add(store_param: StoreEntity) {
        if(!store.contains(store_param)){
            store.add(store_param)
            notifyItemInserted(store.size-1)
        }
    }

    fun setStores(stores: MutableList<StoreEntity>) {
        this.store = stores
        notifyDataSetChanged()
    }

    fun update(informacion: StoreEntity) {
        val index = store.indexOf(informacion)
        if(index != -1){
            store.set(index,informacion)
            notifyItemChanged(index)
        }
    }

    fun delete(informacion: StoreEntity) {
        val index = store.indexOf(informacion)
        if(index != -1){
            store.removeAt(index)
            notifyItemChanged(index)
        }
    }

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val binding = ItemStoreBinding.bind(view)

        fun setOnclickListener(store: StoreEntity){
            with(binding.root){
                setOnClickListener { listener.OnClickListener(store.id) }
                setOnLongClickListener { listener.OnDeleteItem(store)
                true
                }
            }
            binding.CheckFav.setOnClickListener {
                listener.OnFavoritePut(store)
            }
        }
    }
}