package com.example.ortseguros.adapters


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ortseguros.R
import com.example.ortseguros.entities.Poliza
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class PolizaAdapter(
    private var polizaList: MutableList<Poliza>,
    private var onClick : (Int) -> Unit) :
    RecyclerView.Adapter<PolizaAdapter.PolizaHolder>() {

    class PolizaHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private var imagePoliza: ImageView = v.findViewById(R.id.ImagePoliza)
        private var imageMarca: ImageView = v.findViewById(R.id.imageMarca)
        var imageCambio:ImageView =v.findViewById(R.id.imageCambio)


        fun setMarca(marca: String) {
            val txtMarca: TextView = view.findViewById(R.id.txtMarcaModeloCardView)
            txtMarca.text = marca

            if (marca.contains(view.context.getString(R.string.volkswagen), ignoreCase = true)) {
                imageMarca.setImageResource(R.drawable.logo_marca_vw)
            } else if (marca.contains(view.context.getString(R.string.fiat), ignoreCase = true)) {
                imageMarca.setImageResource(R.drawable.logo_marca_fiat)
            }else if (marca.contains(view.context.getString(R.string.ford), ignoreCase = true)) {
                imageMarca.setImageResource(R.drawable.logo_marca_ford)
            }
        }


        fun setPatente(patente: String) {
            val txtPatente: TextView = view.findViewById(R.id.txtPatente)
            val patenteText = view.context.getString(R.string.patente_text, patente)
            txtPatente.text = patenteText
        }


        fun setNroPoliza(nroPoliza: String) {
            val txtNroPoliza: TextView = view.findViewById(R.id.txtNumPoliza)
            val nroPolizaText = view.context.getString(R.string.nro_poliza_text, nroPoliza)
            txtNroPoliza.text = nroPolizaText
        }



        fun setNotificacion(actualizada:Boolean) {
            val notificacionImageView: ImageView = view.findViewById(R.id.imageUpdate)

            if (actualizada) {
                notificacionImageView.visibility = View.VISIBLE
            } else {
                notificacionImageView.visibility = View.GONE
            }
        }


        fun loadPolizaImage(pathToImage: String) {
            val storage = Firebase.storage
            val storageRef = storage.reference
            val imageRef = storageRef.child(pathToImage)

            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()

                Glide.with(itemView.context)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imagePoliza)
            }.addOnFailureListener { exception ->
                Log.e("MiApp", "Error al obtener la URL de descarga de la imagen: $exception")
            }
        }

        fun actualizarUriImagePredeterminadaEnFirestore(poliza: Poliza, nuevaUriImagePredeterminada: String) {
            val db = Firebase.firestore
            db.collection("polizas").document(poliza.id)
                .update("uriImagePredeterminada", nuevaUriImagePredeterminada)
                .addOnSuccessListener {
                    poliza.uriImagePredeterminada = nuevaUriImagePredeterminada
                }
                .addOnFailureListener { exception ->
                    Log.e("MiApp", "Error al actualizar la URI de imagen predeterminada: ${exception.message}")
                }
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
        holder.setNroPoliza(poliza.numPoliza)
        holder.setNotificacion(poliza.actualizada)
        holder.loadPolizaImage(poliza.uriImagePredeterminada)


        val imageCambio = holder.imageCambio



        var imageCounter = 0

        imageCambio.setOnClickListener {
            when (imageCounter) {
                0 -> {
                    holder.actualizarUriImagePredeterminadaEnFirestore(poliza, poliza.uriImageLatIzq)
                    holder.loadPolizaImage(poliza.uriImageLatIzq)
                }
                1 -> {
                    holder.actualizarUriImagePredeterminadaEnFirestore(poliza, poliza.uriImageLatDer)
                    holder.loadPolizaImage(poliza.uriImageLatDer)
                }
                2 -> {
                    holder.actualizarUriImagePredeterminadaEnFirestore(poliza, poliza.uriImagePosterior)
                    holder.loadPolizaImage(poliza.uriImagePosterior)
                }
                3 -> {
                    holder.actualizarUriImagePredeterminadaEnFirestore(poliza, poliza.uriImageFrente)
                    holder.loadPolizaImage(poliza.uriImageFrente)
                }
            }

            imageCounter = (imageCounter + 1) % 4
        }




        holder.getCard().setOnClickListener {
            onClick(position)
        }
    }




    override fun getItemCount(): Int {
        return polizaList.size
    }
}
