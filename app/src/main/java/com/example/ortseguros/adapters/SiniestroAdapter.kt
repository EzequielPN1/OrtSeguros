package com.example.ortseguros.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.ortseguros.entities.Siniestro
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.ortseguros.R

class SiniestroAdapter(
    private var siniestroList: MutableList<Siniestro>,
    private var onClick : (Int) -> Unit
    ) : RecyclerView.Adapter<SiniestroAdapter.SiniestroHolder>() {

    class SiniestroHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v

       fun setFecha(fecha:String){
           val txtFecha: TextView = view.findViewById(R.id.txtFecha)
           txtFecha.text = fecha
       }

        fun setHora(hora:String){
            val txtHora: TextView = view.findViewById(R.id.txtHora)
            txtHora.text = hora
        }

        fun getCard():CardView{
            return view.findViewById(R.id.idCardViewSiniestro)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiniestroHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_siniestro, parent, false)
        return SiniestroHolder(view)
    }

    override fun onBindViewHolder(holder: SiniestroHolder, position: Int) {
        val siniestro = siniestroList[position]

        holder.setFecha(siniestro.fecha)
        holder.setHora(siniestro.hora)

        holder.getCard().setOnClickListener(){
            onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return siniestroList.size
    }


}