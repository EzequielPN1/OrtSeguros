package com.example.ortseguros.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.ortseguros.R
import com.example.ortseguros.entities.Pago


class PagoAdapter (private var pagoList: MutableList<Pago>,
                   private var onClick : (Int) -> Unit):
    RecyclerView.Adapter<PagoAdapter.PagoHolder>(){

    class PagoHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v


        fun setNumeroPago(numPago: String) {
            val txtNumPago : TextView = view.findViewById(R.id.txtNumPago)
            val mensaje="Numero: "
            txtNumPago.text = "$mensaje $numPago"
        }

        fun setFechaVencimiento(fechaVencimiento: String) {
                val txtFechaVencimiento : TextView = view.findViewById(R.id.txtFechaVencimientoCardView)
                val mensaje="Fecha de vencimiento: "
                txtFechaVencimiento.text = "$mensaje $fechaVencimiento"
        }

        fun setFechaPago(fechaPago: String) {
                val txtfechaPago : TextView = view.findViewById(R.id.txtFechaPagoCardView)
                val mensaje="Fecha de pago: "
                txtfechaPago.text = "$mensaje $fechaPago"
        }

        fun setAbonado(abonado: Boolean) {
            val txtAbonado: TextView = view.findViewById(R.id.txtAbonadoCardView)
            val mensaje = if (abonado) "Abonado" else "Impago"
            txtAbonado.text = mensaje
        }





        fun getCard(): CardView {
            return view.findViewById(R.id.cardViewPago)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pago, parent, false)
        return PagoHolder(view)
    }

    override fun getItemCount(): Int {
        return pagoList.size
    }

    override fun onBindViewHolder(holder: PagoHolder, position: Int) {
        val pago = pagoList[position]

         holder.setNumeroPago(pago.numeroPago)
         holder.setFechaVencimiento(pago.fechaVencimiento)
         holder.setFechaPago(pago.fechaPago)
         holder.setAbonado(pago.abonado)


        holder.getCard().setOnClickListener(){
            onClick(position)
        }
    }


}