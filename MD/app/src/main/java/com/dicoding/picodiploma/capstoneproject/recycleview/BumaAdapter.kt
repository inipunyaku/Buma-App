package com.dicoding.picodiploma.capstoneproject.recycleview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.capstoneproject.R

class BumaAdapter(private var results: ArrayList<Buma.Result>, val listener: OnAdapterListener) :
    RecyclerView.Adapter<BumaAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val imgBuma = view.findViewById<ImageView>(R.id.img_item_photo)
        val tvName = view.findViewById<TextView>(R.id.tv_tags)
        val tvContent = view.findViewById<TextView>(R.id.tv_content)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_buma,
            parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val result = results[position]
        holder.tvName.text = result.title
        holder.tvContent.text = result.tags
        Glide.with(holder.imgBuma)
            .load(result.image)
            .placeholder(R.drawable.loading)
            .error(R.drawable.error)
            .circleCrop()
            .into(holder.imgBuma)
        holder.itemView.setOnClickListener {
            val name = result.tags
            Toast.makeText(holder.itemView.context, "${name} ", Toast.LENGTH_SHORT).show()
        }

        holder.view.setOnClickListener {
            listener.onClick(result)
        }
    }

    override fun getItemCount(): Int {
        if (results != null) {
            return results.size
        }
        return 0
    }

    fun setData(data: List<Buma.Result>) {
        results.clear()
        results.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onClick(result: Buma.Result)
    }
}

