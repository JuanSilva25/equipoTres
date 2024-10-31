package com.example.equipotres.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.equipotres.R
import com.example.equipotres.models.Reto

class RetoAdapter(private val retos: MutableList<Reto>) : RecyclerView.Adapter<RetoAdapter.RetoViewHolder>() {

    inner class RetoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(reto: Reto) {
            itemView.findViewById<TextView>(R.id.reto_description).text = reto.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RetoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reto, parent, false)
        return RetoViewHolder(view)
    }

    override fun onBindViewHolder(holder: RetoViewHolder, position: Int) {
        holder.bind(retos[position])
    }

    override fun getItemCount(): Int = retos.size

    fun addReto(reto: Reto) {
        retos.add(reto)
        notifyItemInserted(retos.size - 1)
    }

    fun updateRetos(newRetos: List<Reto>) {
        retos.clear()
        retos.addAll(newRetos)
        notifyDataSetChanged()
    }
}