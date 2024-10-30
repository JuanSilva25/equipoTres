package com.example.equipotres

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.equipotres.databinding.ActivityMainBinding
import com.example.equipotres.ui.instrucciones.InstruccionesFragment
import com.example.equipotres.ui.retos.RetosActivity

class MainActivity : AppCompatActivity(), InstruccionesFragment.InstruccionesListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var countdownTimer: CountDownTimer
    private lateinit var mediaPlayer: MediaPlayer
    private var isAudioPlaying = true // Estado inicial del audio

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupToolbar()
        setupAudioButton()

        mediaPlayer = MediaPlayer.create(this, R.raw.sound_main)
        mediaPlayer.isLooping = true // Hace que el sonido se repita en bucle
        mediaPlayer.start() // Inicia el sonido de fondo

        // Iniciar el contador regresivo
        startCountdown()

        // Configurar el botón "Presióname" para que parpadee
        makeButtonBlink(binding.bButton)

        // Función para abrir la ventana de retos
        val btnAdd = findViewById<ImageButton>(R.id.btn_add)
        btnAdd.setOnClickListener {
            mediaPlayer.pause()
            val intent = Intent(this, RetosActivity::class.java)
            startActivity(intent)
        }

        // Función para abrir la ventana de instrucciones
        val btnGame = findViewById<ImageButton>(R.id.btn_game)
        btnGame.setOnClickListener {
            val instruccionesFragment = InstruccionesFragment()
            instruccionesFragment.setInstruccionesListener(this)
            instruccionesFragment.show(supportFragmentManager, "InstruccionesFragment")
        }

        // Función para el botón de compartir
        val btnShare = findViewById<ImageButton>(R.id.btnShare)
        btnShare.setOnClickListener {
            shareContent()
        }
    }

    private fun setupToolbar() {
        val toolbar = binding.contentToolbar.toolbar
        setSupportActionBar(toolbar)
        val btnStar = toolbar.findViewById<ImageButton>(R.id.btn_star)
        btnStar.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=es")
            startActivity(intent)
        }
    }

    private fun setupAudioButton() {
        val audioButton = binding.contentToolbar.toolbar.findViewById<ImageButton>(R.id.btn_audio_on)
        audioButton.setOnClickListener {
            if (isAudioPlaying) {
                mediaPlayer.pause()
                audioButton.setImageResource(R.drawable.ico_volume_off)
                isAudioPlaying = false
            } else {
                mediaPlayer.start()
                audioButton.setImageResource(R.drawable.ico_volume_on)
                isAudioPlaying = true
            }
        }
    }

    private fun shareContent() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Este es el contenido que quiero compartir.")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Compartir con"))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity() // Cierra todas las actividades y sale de la aplicación
    }

    private fun startCountdown() {
        binding.contador.text = "3"
        countdownTimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = (millisUntilFinished / 1000).toInt()
                binding.contador.text = secondsLeft.toString()
            }

            override fun onFinish() {
                binding.contador.text = "0"
                binding.contador.visibility = View.GONE
            }
        }.start()
    }

    private fun makeButtonBlink(button: Button) {
        val animator = ObjectAnimator.ofFloat(button, "alpha", 0f, 1f)
        animator.repeatCount = ValueAnimator.INFINITE
        animator.repeatMode = ValueAnimator.REVERSE
        animator.duration = 500
        animator.start()
    }

    override fun onPause() {
        super.onPause()
        if (isAudioPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isAudioPlaying) {
            mediaPlayer.start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun onDialogShown() {
        if (isAudioPlaying) {
            mediaPlayer.pause()
        }
    }

    override fun onDialogDismissed() {
        if (isAudioPlaying) {
            mediaPlayer.start()
        }
    }
}
