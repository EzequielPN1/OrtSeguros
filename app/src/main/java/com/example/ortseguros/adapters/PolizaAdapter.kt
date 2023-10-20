package com.example.ortseguros.adapters

import android.net.Uri
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
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class PolizaAdapter(
    private var polizaList: MutableList<Poliza>,
    private var onClick : (Int) -> Unit) :
    RecyclerView.Adapter<PolizaAdapter.PolizaHolder>() {

    class PolizaHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        var imagePoliza: ImageView = v.findViewById(R.id.ImagePoliza)


        fun setMarca(marca: String) {

            val txtMarca : TextView = view.findViewById(R.id.txtMarcaModeloCardView)
            txtMarca.text = marca
        }

        fun setPatente(patente: String) {

            val txtPatente : TextView = view.findViewById(R.id.txtPatente)
            txtPatente.text = "Patente: $patente"
        }

        fun setNroPoliza(nroPoliza: String){
            val txtNroPoliza : TextView = view.findViewById(R.id.txtNumPoliza)
            txtNroPoliza.text = "Nro de poliza: $nroPoliza"
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

        // Reemplaza esta línea con la ubicación correcta de tu imagen en Firebase Storage
        val pathToImage = "${poliza.uriImageFrente}"

        Log.e("MiApp", pathToImage)

        val storage = Firebase.storage
        val storageRef = storage.reference

        // Obtén una referencia a la imagen en Firebase Storage
        val imageRef = storageRef.child(pathToImage)

        // Obtén la URL de descarga de la imagen
        imageRef.downloadUrl.addOnSuccessListener { uri ->
            // La URI contiene la URL de descarga de la imagen
            val imageUrl = uri.toString()

            // Cargar la imagen con Glide
            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Opcional: puedes ajustar la estrategia de caché según tus necesidades
                .into(holder.imagePoliza)
            Log.e("MiApp", "Ok")
        }.addOnFailureListener { exception ->
            // Maneja cualquier error que ocurra al obtener la URL de descarga
            Log.e("MiApp", "Error al obtener la URL de descarga de la imagen: $exception")
        }

        holder.setNroPoliza(poliza.numPoliza)

        holder.getCard().setOnClickListener {
            onClick(position)
        }
    }




    override fun getItemCount(): Int {
        return polizaList.size
    }
}
