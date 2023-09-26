package com.example.ortseguros.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ortseguros.R
import com.example.ortseguros.entities.Poliza

class PolizaAdapter(private var polizaList: MutableList<Poliza>) :
    RecyclerView.Adapter<PolizaAdapter.PolizaHolder>() {

    class PolizaHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun setMarca(marca: String) {
            // Configura la vista para mostrar la marca
            var txtMarca : TextView = view.findViewById(R.id.txtMarca)
             txtMarca.text = marca
        }

        fun setPatente(patente: String) {
            // Configura la vista para mostrar la patente
             var txtPatente : TextView = view.findViewById(R.id.txtPatente)
             txtPatente.text = patente
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PolizaHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_poliza, parent, false)
        return PolizaHolder(view)
    }

    override fun onBindViewHolder(holder: PolizaHolder, position: Int) {
        val poliza = polizaList[position]

        holder.setMarca(poliza.marca)
        holder.setPatente(poliza.patente)
    }

    override fun getItemCount(): Int {
        return polizaList.size
    }
}
