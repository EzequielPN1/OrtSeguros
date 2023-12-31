package com.example.ortseguros.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
            val txtNumPago: TextView = view.findViewById(R.id.txtNumPago)
            val mensaje = view.context.getString(R.string.numero_pago_text, numPago)
            txtNumPago.text = mensaje
        }


        fun setFechaVencimiento(fechaVencimiento: String) {
            val txtFechaVencimiento: TextView = view.findViewById(R.id.txtFechaVencimientoCardView)
            val mensaje = view.context.getString(R.string.fecha_vencimiento_text, fechaVencimiento)
            txtFechaVencimiento.text = mensaje
        }


        fun setFechaPago(fechaPago: String, abonado: Boolean) {
            val txtFechaPago: TextView = view.findViewById(R.id.txtFechaPagoCardView)

            if (abonado) {
                val mensaje = view.context.getString(R.string.fecha_pago_text, fechaPago)
                txtFechaPago.text = mensaje
                txtFechaPago.visibility = View.VISIBLE
            } else {
                txtFechaPago.visibility = View.GONE
                txtFechaPago.text = ""
            }
        }



        fun setAbonado(abonado: Boolean) {
            val imagePago: ImageView = view.findViewById(R.id.imagePago)

            if (abonado) {
                imagePago.visibility = View.VISIBLE
                imagePago.setImageResource(R.drawable.icon_pagado)
            } else {
                imagePago.visibility = View.GONE
            }
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
        holder.setFechaPago(pago.fechaPago, pago.abonado)
        holder.setAbonado(pago.abonado)

        holder.getCard().setOnClickListener {
            onClick(position)
        }
    }



}