package com.example.equipotres.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.equipotres.R
import com.example.equipotres.model.Reto

class RetoAdapter(
    private val retos: MutableList<Reto>,
    private val onEditClick: (Reto) -> Unit,
    private val onDeleteClick: (Reto) -> Unit
) : RecyclerView.Adapter<RetoAdapter.RetoViewHolder>() {

    inner class RetoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val descriptionTextView = itemView.findViewById<TextView>(R.id.reto_description)
        private val editButton = itemView.findViewById<ImageButton>(R.id.edit_button)
        private val deleteButton = itemView.findViewById<ImageButton>(R.id.delete_button)

        fun bind(reto: Reto) {
            descriptionTextView.text = reto.description
            editButton.setOnClickListener { onEditClick(reto) }
            deleteButton.setOnClickListener { onDeleteClick(reto) }
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
        retos.add(0, reto)
        notifyItemInserted(0)
    }

    fun updateRetos(newRetos: List<Reto>) {
        retos.clear()
        retos.addAll(newRetos)
        notifyDataSetChanged()
    }

    fun updateReto(updatedReto: Reto) {
        val index = retos.indexOfFirst { it.id == updatedReto.id }
        if (index != -1) {
            retos[index] = updatedReto
            notifyItemChanged(index)
        }
    }

    fun deleteReto(reto: Reto) {
        val index = retos.indexOfFirst { it.id == reto.id }
        if (index != -1) {
            retos.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
