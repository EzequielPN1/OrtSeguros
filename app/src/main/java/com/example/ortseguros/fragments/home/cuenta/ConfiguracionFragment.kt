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
import androidx.navigation.fragment.findNavController
import com.example.ortseguros.R
import com.example.ortseguros.activities.MainActivity

class ConfiguracionFragment : Fragment() {


    private lateinit var viewModel: ConfiguracionViewModel
    lateinit var v: View
    lateinit var btnEditarCuenta : Button
    lateinit var btnPreguntasFrecuentes : Button
    lateinit var btnAsistenciaMecanica : Button
    lateinit var btnContacto : Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(R.layout.fragment_configuracion, container, false)
        btnEditarCuenta = v.findViewById(R.id.btnEditarCuenta)
        btnPreguntasFrecuentes = v.findViewById(R.id.btnPreguntasFrecuentes)
        btnAsistenciaMecanica = v.findViewById(R.id.btnAsisMecanica)
        btnContacto = v.findViewById(R.id.btnContacto)
        return v

    }

    override fun onStart() {
        super.onStart()


        btnEditarCuenta.setOnClickListener{
            val action = ConfiguracionFragmentDirections.actionConfiguracionFragmentToEditarCuentaFragment()
            findNavController().navigate(action)
        }
        btnPreguntasFrecuentes.setOnClickListener{
            val action = ConfiguracionFragmentDirections.actionConfiguracionFragmentToPreguntasFrecuentesFragment()
            findNavController().navigate(action)
        }
        btnAsistenciaMecanica.setOnClickListener{
            makePhoneCall("1122334455")
        }
        btnContacto.setOnClickListener {
            makePhoneCall("1166778899")
        }
    }

    private fun makePhoneCall(number: String) {

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CALL_PHONE), 1)
        }else {
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