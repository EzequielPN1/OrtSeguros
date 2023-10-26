package com.example.ortseguros.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.ortseguros.entities.Siniestro
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.example.ortseguros.R
import com.example.ortseguros.entities.Mensaje

class SiniestroAdapter(
    private var siniestroList: MutableList<Siniestro>,
    private var onClick : (Int) -> Unit
    ) : RecyclerView.Adapter<SiniestroAdapter.SiniestroHolder>() {

    class SiniestroHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v


        fun tipoSiniestro(tipoSiniestro: String) {
            val txtTipoSiniestro: TextView = view.findViewById(R.id.txtTipoSiniestro)

            val textoMostrado = when (tipoSiniestro) {
                "granizo" -> "Tipo: Granizo"
                "danioTotal" -> "Tipo: Daño total"
                "respCivil" -> "Tipo: Responsabilidad civil"
                "roboParcial" -> "Tipo: Robo parcial"
                "danioTotal" -> "Tipo: Daño total"
                else -> tipoSiniestro
            }
            txtTipoSiniestro.text = textoMostrado
        }


        fun setImageTipoSiniestro(tipoSiniestro: String) {
            val imageViewTipoSiniestro: ImageView = view.findViewById(R.id.imageViewTipoSiniestro)

            val imagenId = when (tipoSiniestro) {
                "granizo" -> R.drawable.icon_granizo
                "danioTotal" -> R.drawable.icon_danio_total
                "respCivil" -> R.drawable.icon_resp_civil
                "roboParcial" -> R.drawable.icon_robo_parcial
                "roboTotal"-> R.drawable.icon_robo_total
                else -> R.drawable.car_frontal
            }

            imageViewTipoSiniestro.setImageResource(imagenId)
        }


        fun setNumSiniestro(numSiniestro:String){
            val txtNumSiniestro: TextView = view.findViewById(R.id.txtNumSiniestro)
            txtNumSiniestro.text = "Número: $numSiniestro"
        }

       fun setFecha(fecha:String){
           val txtFecha: TextView = view.findViewById(R.id.txtFechaVencimientoCardView)
           txtFecha.text = "Fecha: $fecha"
       }


        fun setPatente(patente:String){
            val txtPatente: TextView = view.findViewById(R.id.txtAbonadoCardView)
            txtPatente.text = "Patente: $patente"
        }

        fun setNotificacion(mensajes: List<Mensaje>) {
            val notificacionImageView: ImageView = view.findViewById(R.id.imageViewMensaje)

            val mensajesNoLeidos = mensajes.filter { !it.estado }

            if (mensajesNoLeidos.isNotEmpty()) {
                notificacionImageView.visibility = View.VISIBLE
            } else {
                notificacionImageView.visibility = View.GONE
            }
        }



        fun getCard():CardView{
            return view.findViewById(R.id.idCardViewPago)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SiniestroHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_siniestro, parent, false)
        return SiniestroHolder(view)
    }

    override fun onBindViewHolder(holder: SiniestroHolder, position: Int) {
        val siniestro = siniestroList[position]


        holder.tipoSiniestro(siniestro.tipoSiniestro)
        holder.setNumSiniestro(siniestro.numSiniestro)
        holder.setFecha(siniestro.fecha)
        holder.setPatente(siniestro.patente)
        holder.setImageTipoSiniestro(siniestro.tipoSiniestro)
        holder.setNotificacion(siniestro.mensajes)




        holder.getCard().setOnClickListener {
            onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return siniestroList.size
    }




}