package com.sutechshop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.sutechshop.R
import com.sutechshop.model.Cart
import com.sutechshop.model.Product

class CartListAdapter(
    private val context: Context,
    private val productList: List<Product>,
    private val cartList: List<Cart>
) : RecyclerView.Adapter<CartListAdapter.CartListViewHolder>() {

    inner class CartListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.itemCartImageView)
        val title: TextView = view.findViewById(R.id.itemCartTvTitle)
        val available: TextView = view.findViewById(R.id.itemCartTvAvailable)
        val quantity: TextView = view.findViewById(R.id.itemCartTvQuantity)
        val price: TextView = view.findViewById(R.id.itemCartTvPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartListViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_cart, parent, false)
        return CartListViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartListViewHolder, position: Int) {
        val cartItem = cartList[position]
        val product = productList.filter { it.id == cartItem.productId }[0]

        holder.title.text = product.name
        holder.available.text = "این کالا موجود است"
        holder.quantity.text = "${cartItem.quantity} عدد"
        holder.price.text = "${product.price * cartItem.quantity} تومان"

        holder.image.load(product.urlToImage) {
            crossfade(true)
            placeholder(R.drawable.placeholder)
            error(R.drawable.placeholder)
            transformations(RoundedCornersTransformation(8f))
            scale(Scale.FILL)
        }
    }

    override fun getItemCount(): Int = cartList.size

}