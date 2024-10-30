package com.example.equipotres.ui.instrucciones

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.example.equipotres.R

class InstruccionesFragment : DialogFragment() {
    private var isAudioOn: Boolean = false

    interface InstruccionesListener {
        fun onDialogShown()
        fun onDialogDismissed()
    }

    private var listener: InstruccionesListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_instrucciones, container, false)
        isAudioOn = arguments?.getBoolean("isAudioOn") ?: false

        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        toolbar.title = "Reglas del Juego"
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_revert)
        toolbar.setNavigationOnClickListener {
            dismiss() // Cierra el DialogFragment
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(MATCH_PARENT, MATCH_PARENT)
        listener?.onDialogShown()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listener?.onDialogDismissed()
    }

    fun setInstruccionesListener(listener: InstruccionesListener) {
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
    }
}
