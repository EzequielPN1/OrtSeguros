package com.example.ortseguros.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ortseguros.R
import com.example.ortseguros.entities.Poliza

class PolizaAdapter(
    private var polizaList: MutableList<Poliza>,
    private var onClick : (Int) -> Unit) :
    RecyclerView.Adapter<PolizaAdapter.PolizaHolder>() {

    class PolizaHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun setMarca(marca: String) {
            // Configura la vista para mostrar la marca
            val txtMarca : TextView = view.findViewById(R.id.txtFecha)
            txtMarca.text = marca
        }

        fun setPatente(patente: String) {
            // Configura la vista para mostrar la patente
            val txtPatente : TextView = view.findViewById(R.id.txtPatente)
            txtPatente.text = patente
        }

        fun getCard(): CardView {
            return view.findViewById(R.id.idCardViewPoliza)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PolizaHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_poliza, parent, false)
        return PolizaHolder(view)
    }

    override fun onBindViewHolder(holder: PolizaHolder, position: Int) {
        val poliza = polizaList[position]

        holder.setMarca(poliza.marcaModelo)
        holder.setPatente(poliza.patente)

        holder.getCard().setOnClickListener(){
            onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return polizaList.size
    }
}
