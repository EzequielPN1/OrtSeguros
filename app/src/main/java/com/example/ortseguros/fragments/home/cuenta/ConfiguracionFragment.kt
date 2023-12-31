package com.example.ortseguros.fragments.home.cuenta


import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.Manifest
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ortseguros.R


class ConfiguracionFragment : Fragment() {

    private lateinit var viewModelConfiguracion: ConfiguracionViewModel
    private lateinit var v: View
    private lateinit var btnEditarCuenta: Button
    private lateinit var btnPreguntasFrecuentes: Button
    private lateinit var btnAsistenciaMecanica: Button
    private lateinit var btnContacto: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModelConfiguracion =  ViewModelProvider(this)[ConfiguracionViewModel::class.java]
        v = inflater.inflate(R.layout.fragment_configuracion, container, false)
        btnEditarCuenta = v.findViewById(R.id.btnEditarCuenta)
        btnPreguntasFrecuentes = v.findViewById(R.id.btnPreguntasFrecuentes)
        btnAsistenciaMecanica = v.findViewById(R.id.btnAsisMecanica)
        btnContacto = v.findViewById(R.id.btnContacto)
        return v
    }

    override fun onStart() {
        super.onStart()


        btnEditarCuenta.setOnClickListener {
            val action =
                ConfiguracionFragmentDirections.actionConfiguracionFragmentToEditarCuentaFragment()
            findNavController().navigate(action)
        }
        btnPreguntasFrecuentes.setOnClickListener {
            val action =
                ConfiguracionFragmentDirections.actionConfiguracionFragmentToPreguntasFrecuentesFragment()
            findNavController().navigate(action)
        }

        btnAsistenciaMecanica.setOnClickListener {
            viewModelConfiguracion.obtenerTelefonoAsistenciaMecanica { telefono ->
                if (telefono != null) {
                    makePhoneCall(telefono)
                }
            }
        }

        btnContacto.setOnClickListener {
            viewModelConfiguracion.obtenerTelefonoContacto { telefono ->
                if (telefono != null) {
                    makePhoneCall(telefono)
                }
            }
        }
    }


    private fun makePhoneCall(number: String) {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CALL_PHONE),
                1
            )
        } else {
            val dialIntent = Intent(Intent.ACTION_CALL)
            dialIntent.data = Uri.parse("tel:$number")
            startActivity(dialIntent)
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

}