package com.teamwork.javasresto.adapter

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.teamwork.javasresto.R
import com.teamwork.javasresto.activity.DetailActivity
import com.teamwork.javasresto.models.ProductModels
import java.security.AccessController.getContext

class ProductAdapter(private val productList : List<ProductModels>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_row_product, parent, false)

        return ProductViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val currentItem = productList[position]

        holder.ivProduct.setImageResource(currentItem.productImageDrawable)
        holder.tvNameProduct.text = currentItem.productName
        holder.tvKategoriProduct.text = currentItem.productKat
        holder.tvDeskripsiProduct.text = currentItem.productKet
        holder.priceProduct.text = currentItem.productPrice.toString()
        holder.listRow.setOnClickListener {

            val bundle = Bundle()
            bundle.putInt("image", currentItem.productImageDrawable)
            bundle.putString("name", currentItem.productName)
            bundle.putString("kat", currentItem.productKat)
            bundle.putString("ket", currentItem.productKet)
            bundle.putString("price", currentItem.productPrice.toString())

            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtras(bundle)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount() = productList.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ivProduct : ImageView = itemView.findViewById(R.id.ivProduct)
        val tvNameProduct : TextView = itemView.findViewById(R.id.tvNameProduct)
        val tvKategoriProduct : TextView = itemView.findViewById(R.id.tvKategoriProduct)
        val tvDeskripsiProduct : TextView = itemView.findViewById(R.id.tvDeskripsiProduct)
        val priceProduct : TextView = itemView.findViewById(R.id.priceProduct)
        val listRow : LinearLayout = itemView.findViewById(R.id.list_row_movie)
    }

}