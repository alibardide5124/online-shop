package com.sutechshop.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.google.gson.Gson
import com.sutechshop.ui.DetailActivity
import com.sutechshop.R
import com.sutechshop.model.Product

class ProductListAdapter(
    private val context: Context,
    private val productList: List<Product>
) : RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder>() {

    inner class ProductListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.itemProductImageView)
        val title: TextView = view.findViewById(R.id.itemProductTextViewTitle)
        val available: TextView = view.findViewById(R.id.itemProductTextViewAvailable)
        val price: TextView = view.findViewById(R.id.itemProductTextViewPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_product, parent, false)
        return ProductListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        val item = productList[position]
        holder.title.text = item.name
        holder.available.text = "این کالا موجود است"
        holder.price.text = "${item.price} تومان"

        if (item.quantity == 0) {
            holder.available.text = "اتمام موجودی"
            holder.available.setTextColor(Color.parseColor("#F44336"))
            holder.price.visibility = View.INVISIBLE
        }
        holder.image.load(item.urlToImage) {
            crossfade(true)
            placeholder(R.drawable.placeholder)
            error(R.drawable.placeholder)
            transformations(RoundedCornersTransformation(8f))
            scale(Scale.FILL)
        }

        holder.itemView.setOnClickListener {
            val productData = Gson().toJson(item)
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("product", productData)
            (context as Activity).startActivity(intent)
        }
    }

    override fun getItemCount(): Int = productList.size

}